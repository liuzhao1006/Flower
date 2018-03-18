package com.lz.flower.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.lz.flower.fragment.RecognizeContract;
import com.lz.flower.utils.Utils;

import java.io.File;

public class RecognizePresenter implements RecognizeContract.Presenter {


    private static final int FIX_SIDE = 299;

    private final RecognizeContract.View mRecognizeView;



    private String type;

    public RecognizePresenter(@NonNull RecognizeContract.View view, String type) {
        mRecognizeView = view;
        mRecognizeView.setPresenter(this);
        this.type=type;
    }

    /**
     * 停止加载网络情况
     */
    @Override
    public void cancelLoading() {

    }


    /**
     * 请求网络,接收结果
     * @param bitmap
     */
    @Override
    public void requestImageInfo(@NonNull Bitmap bitmap) {
        mRecognizeView.showDiscernStep("正在识别...", null);
    }

    @Override
    public void start() {
    }

    /**
     * 裁剪图片
     * @param uri
     */
    @Override
    public void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", FIX_SIDE);
        intent.putExtra("outputY", FIX_SIDE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoCropUri());
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }

    /**
     * 请求结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void result(int requestCode, int resultCode, Intent data) {


    }

    /**
     * 保存状态
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    /**
     * 获取URI路径
     * @return
     */
    private Uri photoCropUri() {
        File file = new File(Utils.getFlowerCropDir(), "select_crop.jpg");
        return Uri.fromFile(file);
    }
}
