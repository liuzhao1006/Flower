package com.lz.flower.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lz.flower.R;
import com.lz.flower.bean.HblFlowerInfo;
import com.lz.flower.camera.SimpleCamera;
import com.lz.flower.presenter.RecognizePresenter;
import com.lz.flower.utils.Utils;

import java.io.File;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者: 刘朝.
 * 时间: 2018/3/18 下午2:14
 */

public class RecognizeFragment extends Fragment implements RecognizeContract.View {


    @BindView(R.id.cameraStub)
    ViewStub cameraStub;
    @BindView(R.id.fd_backView)
    ImageView fdBackView;
    @BindView(R.id.fd_loadingView)
    ProgressBar fdLoadingView;
    @BindView(R.id.take_pic)
    ImageView takePic;
    @BindView(R.id.fd_bottomCenterContainer)
    FrameLayout fdBottomCenterContainer;
    @BindView(R.id.ldl_recyclerView)
    RecyclerView ldlRecyclerView;
    @BindView(R.id.showStep)
    TextView showStep;
    @BindView(R.id.responseImgView)
    ImageView responseImgView;
    @BindView(R.id.responseHtmlTextView)
    TextView responseHtmlTextView;
    @BindView(R.id.responseInfoContainer)
    RelativeLayout responseInfoContainer;
    @BindView(R.id.msgView)
    FrameLayout msgView;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    Unbinder unbinder;
    private RecognizeContract.Presenter mPresenter;
    private String type;

    private SimpleCamera mSimpleCamera;
    private Bitmap newestCropBitmap;
    private BlurTransformation mBlurTransformation;
    private String mCropPicPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recognize, container, false);

        if (savedInstanceState != null) {
            if (mPresenter == null) {
                mPresenter = new RecognizePresenter(this, type);
            }
            mPresenter.onRestoreInstanceState(savedInstanceState);
        }

        if ("Weeds".equals(type)) {
//            discernTitleTextView.setText(R.string.take_photo_of_weeds);
        }

        mBlurTransformation = new BlurTransformation(getActivity(), 10);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        if (mSimpleCamera != null) {
            return;
        }
        if (cameraStub != null && mSimpleCamera == null) {
            mSimpleCamera = (SimpleCamera) cameraStub.inflate();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBlurTransformation != null) {
            mBlurTransformation.destroy();
            mBlurTransformation = null;
        }

        if (mSimpleCamera != null) {
            mSimpleCamera.releaseCamera();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PLANT_TYPE", type);
        mPresenter.onSaveInstanceState(outState);
    }


    /*-------------------------------自定义接口的实现--------------------------*/
    @Override
    public void setPresenter(RecognizeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showDiscernStep(String stepMsg, String nextStepMsg) {

    }

    @Override
    public void showUnknownUI(String msg) {

    }

    @Override
    public void showTakePicUI() {
        if (!isVisible()) {
            return;
        }
        takePic.setVisibility(View.VISIBLE);
        mSimpleCamera.setVisibility(View.VISIBLE);

        fdBackView.setVisibility(View.GONE);
        fdLoadingView.setVisibility(View.GONE);
        ldlRecyclerView.setVisibility(View.GONE);
        msgView.setVisibility(View.GONE);
        showStep.setVisibility(View.GONE);
        responseInfoContainer.setVisibility(View.GONE);
    }

    @Override
    public void showFrameView(String path) {

    }

    @Override
    public void showResponseInfo(List<HblFlowerInfo> list) {

    }

    @Override
    public void setCropPath(String path) {
        mCropPicPath = path;
    }

    @Override
    public void showErrorToast(String msg) {
        Utils.showShortToast(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void startCameraPreview() {
        mPresenter.cancelLoading();
        if (mSimpleCamera != null) {
            mSimpleCamera.startPreview();
        }
        if (newestCropBitmap != null && !newestCropBitmap.isRecycled()) {
            newestCropBitmap.recycle();
            newestCropBitmap = null;
        }
        showTakePicUI();
    }

    @OnClick({R.id.cameraStub, R.id.fd_backView, R.id.fd_loadingView, R.id.take_pic, R.id.fd_bottomCenterContainer, R.id.ldl_recyclerView, R.id.showStep, R.id.responseImgView, R.id.responseHtmlTextView, R.id.responseInfoContainer, R.id.msgView, R.id.back_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cameraStub:
                break;
            case R.id.fd_backView:
                break;
            case R.id.fd_loadingView:
                break;
            case R.id.take_pic:

                if (mSimpleCamera == null || mSimpleCamera.getAlpha() < 0.99f) {
                    return;
                }
                showDiscernStep("正在取景...", null);
                try {
                    mSimpleCamera.takePicture(bitmap -> {
                        newestCropBitmap = bitmap;
                        mPresenter.requestImageInfo(bitmap);
                    }, Utils.getFlowerSrcDir() + File.separator + UUID.randomUUID() + ".jpg");
                } catch (Exception e) {
                    showErrorToast("取景失败，请重新拍摄");
                    startCameraPreview();
                }
                break;
            case R.id.fd_bottomCenterContainer:
                break;
            case R.id.ldl_recyclerView:
                break;
            case R.id.showStep:
                break;
            case R.id.responseImgView:
                break;
            case R.id.responseHtmlTextView:
                break;
            case R.id.responseInfoContainer:
                break;
            case R.id.msgView:
                break;

            case R.id.back_btn:
                getActivity().finish();
                break;
        }
    }

    public static RecognizeFragment newInstance() {
        return new RecognizeFragment();
    }

    public void setType(String type) {
        this.type = type;
    }


}
