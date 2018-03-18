package com.lz.flower.camera;

import android.content.Context;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;

public class OrientationEventAdapter extends OrientationEventListener {
    private int mRotation = Surface.ROTATION_0;
    private RotateDetector.OrientationChangedListener mListener;

    public OrientationEventAdapter(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        int tmpRotation;
        if (orientation >= 50 && orientation < 130) {
            tmpRotation = Surface.ROTATION_270;
        } else if (orientation >= 140 && orientation < 220) {
            tmpRotation = Surface.ROTATION_180;
        } else if (orientation >= 230 && orientation < 310) {
            tmpRotation = Surface.ROTATION_90;
        } else if (orientation >= 330 || orientation < 40) {
            tmpRotation = Surface.ROTATION_0;
        } else {
            return;
        }

        if (tmpRotation != mRotation) {
            mRotation = tmpRotation;
            if (mListener != null) {
                mListener.onOrientationChanged(tmpRotation);
                Log.i("刘朝","我的屏幕角度是 : tmpRotation = " + tmpRotation);
            }
        }
    }

    public void setRotationChangedListener(RotateDetector.OrientationChangedListener listener) {
        mListener = listener;
    }

    public int getRotation() {
        return mRotation;
    }
}
