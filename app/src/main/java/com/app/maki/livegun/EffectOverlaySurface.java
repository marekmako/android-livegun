package com.app.maki.livegun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.Face;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public final class EffectOverlaySurface extends SurfaceView implements SurfaceHolder.Callback {

    private DrawTread mDrawThread;

    private CameraSource mCameraSource;

    private int mCameraPreviewWidth;
    private int mCameraPreviewHeight;

    private float mWidthScaleFactor;
    private float mHeightScaleFactor;

    private Set<EffectOverlaySurface.Graphic> graphics = new HashSet<>();

    private Paint mTestPaint = new Paint();
    private Random mRandom = new Random();

    public EffectOverlaySurface(Context context) {
        super(context);
        init();
    }

    public EffectOverlaySurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EffectOverlaySurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setZOrderOnTop(true);
        }
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSPARENT);

        mTestPaint.setColor(Color.GREEN);
        mTestPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTestPaint.setStrokeWidth(1);
    }

    public void setCameraSource(@NonNull CameraSource cameraSource) {
        mCameraSource = cameraSource;

        Size size = cameraSource.getPreviewSize();
        int min = Math.min(size.getWidth(), size.getHeight());
        int max = Math.max(size.getWidth(), size.getHeight());
        if (CameraScreenUtils.isPortraitMode(getContext())) {
            // Swap width and height sizes when in portrait, since it will be rotated by
            // 90 degrees
            mCameraPreviewWidth = min;
            mCameraPreviewHeight = max;
        } else {
            mCameraPreviewWidth = max;
            mCameraPreviewHeight = min;
        }

    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mDrawThread = new DrawTread(getHolder());
        mDrawThread.setIsRunning(true);
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mDrawThread.setIsRunning(false);
        while (retry) {
            try {
                mDrawThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    /**
     * Adds a graphic to the overlay.
     */
    public void add(EffectOverlaySurface.Graphic graphic) {
        graphics.add(graphic);
    }

    public void remove(EffectOverlaySurface.Graphic graphic) {
        graphics.remove(graphic);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            Rect rect = CameraScreenUtils.layout(
                    new Rect(left, top, right, bottom),
                    mCameraSource,
                    CameraScreenUtils.isPortraitMode(getContext()));

            layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }


    private class DrawTread extends Thread {

        final private Object mLock = new Object();

        private SurfaceHolder mSurfaceHolder;

        private boolean mIsRunning = false;

        public DrawTread(@NonNull SurfaceHolder surfaceHolder) {
            mSurfaceHolder = surfaceHolder;
        }

        public void setIsRunning(boolean isRunning) {
            this.mIsRunning = isRunning;
        }

        @Override
        public void run() {
            super.run();

            Canvas canvas;
            while (mIsRunning) {
                canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas();

                    if (canvas != null) {

                        synchronized (mLock) {
                            draw(canvas);
                        }
                    }

                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void draw(Canvas canvas) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);

            if ((mCameraPreviewWidth != 0) && (mCameraPreviewHeight != 0)) {
                mWidthScaleFactor = (float) canvas.getWidth() / (float) mCameraPreviewWidth;
                mHeightScaleFactor = (float) canvas.getHeight() / (float) mCameraPreviewHeight;


                for (EffectOverlaySurface.Graphic graphic : graphics) {
                    graphic.draw(canvas);
                }
            }
        }
    }




    public static abstract class Graphic {

        private EffectOverlaySurface overlay;

        public Graphic(EffectOverlaySurface overlay) {
            this.overlay = overlay;
        }

        public abstract void draw(Canvas canvas);

        public abstract Rect getFaceRect();

        public abstract void updateFace(Face face);

        /**
         * Adjusts a horizontal value of the supplied value from the preview scale to the view
         * scale.
         */
        public float scaleX(float horizontal) {
            return horizontal * overlay.mWidthScaleFactor;
        }

        /**
         * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
         */
        public float scaleY(float vertical) {
            return vertical * overlay.mHeightScaleFactor;
        }

        /**
         * Adjusts the x coordinate from the preview's coordinate system to the view coordinate
         * system.
         */
        public float translateX(float x) {
//            if (mOverlay.mFacing == CameraSource.CAMERA_FACING_FRONT) {
//                return mOverlay.getWidth() - scaleX(x);
//            } else {
//                return scaleX(x);
//            }
            return  scaleX(x);
        }

        /**
         * Adjusts the y coordinate from the preview's coordinate system to the view coordinate
         * system.
         */
        public float translateY(float y) {
            return scaleY(y);
        }

        // TODO: toto je blbost, pretoze overlay je uz surface view kde draw funguje inak, preverit!
        public void postInvalidate() {
            overlay.postInvalidate();
        }
    }
}
