package com.lz.flower.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lz.flower.app.FlowerApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 作者: 刘朝.
 * 时间: 2018/3/18 下午2:22
 */

public class Utils {

    /**
     * bitmap转base64
     * @param scaledBitmap
     * @return
     */
    public static String convertToBase64(Bitmap scaledBitmap) {
        if (scaledBitmap == null || scaledBitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        byte[] bytes = out.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static final String TAG = Utils.class.getSimpleName();
    public static final String HBZ_ROOT_DIR = "com.tld.company";

    private static String getFlowerDir() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + HBZ_ROOT_DIR + File.separator + "花卉识别");
        if (file.exists()) {
            return file.getPath();
        }
        if (file.mkdirs()) {
            return file.getPath();
        }
        Log.e(TAG, "获取花卉识别目录失败");
        return null;
    }

    public static String getFlowerSrcDir() {
        return getFlowerDir();
    }

    public static String getFlowerCropDir() {
        String flowerDir = getFlowerDir();
        if (flowerDir == null) {
            return null;
        }
        File file = new File(flowerDir + File.separator + "crop");
        if (file.exists()) {
            return file.getPath();
        }
        if (file.mkdirs()) {
            return file.getPath();
        }
        Log.e(TAG, "获取花卉识别裁剪目录失败");
        return null;
    }

    /**
     * 简单判断当前是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean result = false;
        if (context != null) {
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return result;
            }
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
            if (networkInfo == null) {
                return result;
            }
            for (NetworkInfo aNetworkInfo : networkInfo) {
                // 判断当前网络状态是否为连接状态
                if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 强制隐藏软键盘
     */
    public static void hideSoftInputFromWindow(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private static Toast mToastShort = null;

    public static void showShortToast(CharSequence charSequence) {
        if (mToastShort == null) {
            mToastShort = Toast.makeText(FlowerApplication.getInstance(), charSequence, Toast.LENGTH_SHORT);
        } else {
            mToastShort.setText(charSequence);
        }
        mToastShort.show();
    }

}
