package com.app.maki.livegun;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private boolean surfaceAvailable;
    private boolean startRequested;

    private CameraSource cameraSource;

    private CameraEffectsOverlay overlay;

    public CameraPreview(Context context) {
        super(context);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        surfaceAvailable = false;
        startRequested = false;
        getHolder().addCallback(this);
    }

    public void start(@NonNull CameraSource cameraSource, @NonNull CameraEffectsOverlay overlay) throws IOException {
        startRequested = true;
        this.overlay = overlay;
        this.cameraSource = cameraSource;

        startIfReady();
    }

    private void startIfReady() throws IOException {
        if (startRequested && surfaceAvailable) {
            cameraSource.start(getHolder());

            if (overlay != null) {
                overlay.setCameraSource(cameraSource);
//                overlay.clear();
            }
            startRequested = false;
        }
    }



    /// SurfaceHolder.Callback



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceAvailable = true;
        try {
            startIfReady();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceAvailable = false;
    }



    /// lifecycle



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            Rect rect = CameraScreenUtils.layout(
                    new Rect(left, top, right, bottom),
                    cameraSource,
                    CameraScreenUtils.isPortraitMode(getContext()));

            layout(rect.left, rect.top, rect.right, rect.bottom);

            try {
                startIfReady();
            } catch (IOException e) {
            }
        }
    }
}
