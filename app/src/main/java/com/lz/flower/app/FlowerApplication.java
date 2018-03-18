package com.lz.flower.app;

import android.app.Application;

/**
 * 作者: 刘朝.
 * 时间: 2018/3/18 下午1:56
 */

public class FlowerApplication extends Application {


    public static FlowerApplication flowerApplication;
    @Override
    public void onCreate() {
        super.onCreate();

        flowerApplication = this;


    }

    public static FlowerApplication getInstance(){
        return flowerApplication;
    }
}
