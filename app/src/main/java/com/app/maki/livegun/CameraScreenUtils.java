package com.app.maki.livegun;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;

public class CameraScreenUtils {

    /// https://github.com/googlesamples/android-vision/issues/23
    public static Rect layout(Rect oldRect, CameraSource cameraSource, boolean isPortrait) {
        int previewWidth = 320;
        int previewHeight = 240;
        if (cameraSource != null) {
            Size size = cameraSource.getPreviewSize();
            if (size != null) {
                previewWidth = size.getWidth();
                previewHeight = size.getHeight();
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortrait) {
            int tmp = previewWidth;
            previewWidth = previewHeight;
            previewHeight = tmp;
        }

        final int viewWidth = oldRect.right - oldRect.left;
        final int viewHeight = oldRect.bottom - oldRect.top;

        int childWidth;
        int childHeight;
        int childXOffset = 0;
        int childYOffset = 0;
        float widthRatio = (float) viewWidth / (float) previewWidth;
        float heightRatio = (float) viewHeight / (float) previewHeight;

        // To fill the view with the camera preview, while also preserving the correct aspect ratio,
        // it is usually necessary to slightly oversize the child and to crop off portions along one
        // of the dimensions.  We scale up based on the dimension requiring the most correction, and
        // compute a crop offset for the other dimension.
        if (widthRatio > heightRatio) {
            childWidth = viewWidth;
            childHeight = (int) ((float) previewHeight * widthRatio);
            childYOffset = (childHeight - viewHeight) / 2;
        } else {
            childWidth = (int) ((float) previewWidth * heightRatio);
            childHeight = viewHeight;
            childXOffset = (childWidth - viewWidth) / 2;
        }

        return new Rect(-1 * childXOffset, -1 * childYOffset,
                childWidth - childXOffset, childHeight - childYOffset);
    }

    public static boolean isPortraitMode(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }

        return false;
    }
}
