package com.app.maki.livegun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;

import java.util.HashSet;
import java.util.Set;

public class CameraEffectsOverlay extends View {

    private CameraSource cameraSource;

    final private Object lock = new Object();

    private int cameraPreviewWidth;
    private int cameraPreviewHeight;

    private float widthScaleFactor;
    private float heightScaleFactor;

    private Rect layoutRect;

    private Set<Graphic> graphics = new HashSet<>();

    public CameraEffectsOverlay(Context context) {
        super(context);
        init();
    }

    public CameraEffectsOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraEffectsOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
    }

    public void setCameraSource(@NonNull CameraSource cameraSource) {
        synchronized (lock) {
            this.cameraSource = cameraSource;

            Size size = cameraSource.getPreviewSize();
            int min = Math.min(size.getWidth(), size.getHeight());
            int max = Math.max(size.getWidth(), size.getHeight());
            if (CameraScreenUtils.isPortraitMode(getContext())) {
                // Swap width and height sizes when in portrait, since it will be rotated by
                // 90 degrees
                cameraPreviewWidth = min;
                cameraPreviewHeight = max;
            } else {
                cameraPreviewWidth = max;
                cameraPreviewHeight = min;
            }
        }
        postInvalidate();
    }

    /**
     * Adds a graphic to the overlay.
     */
    public void add(Graphic graphic) {
        synchronized (lock) {
            graphics.add(graphic);
        }
        postInvalidate();
    }

    public void remove(Graphic graphic) {
        synchronized (lock) {
            graphics.remove(graphic);
        }
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            Rect rect = CameraScreenUtils.layout(
                    new Rect(left, top, right, bottom),
                    cameraSource,
                    CameraScreenUtils.isPortraitMode(getContext()));

            layout(rect.left, rect.top, rect.right, rect.bottom);
            layoutRect = rect;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(1);



        synchronized (lock) {
            if ((cameraPreviewWidth != 0) && (cameraPreviewHeight != 0)) {
                widthScaleFactor = (float) canvas.getWidth() / (float) cameraPreviewWidth;
                heightScaleFactor = (float) canvas.getHeight() / (float) cameraPreviewHeight;


//                final float left = (layoutRect.centerX() - 100);
//                final float top = (layoutRect.height() / 4 - 100);
//                final float right = left + 200;
//                final float bottom = top + 200;
//                canvas.drawRect(left, top, right, bottom, paint);


                for (Graphic graphic : graphics) {
                    graphic.draw(canvas);
                }
            }
        }
    }

    public static abstract class Graphic {

        private CameraEffectsOverlay overlay;

        public Graphic(CameraEffectsOverlay overlay) {
            this.overlay = overlay;
        }

        /**
         * Draw the graphic on the supplied canvas.  Drawing should use the following methods to
         * convert to view coordinates for the graphics that are drawn:
         * <ol>
         * <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of
         * the supplied value from the preview scale to the view scale.</li>
         * <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
         * coordinate from the preview's coordinate system to the view coordinate system.</li>
         * </ol>
         *
         * @param canvas drawing canvas
         */
        public abstract void draw(Canvas canvas);

        public abstract Rect getFaceRect();

        /**
         * Adjusts a horizontal value of the supplied value from the preview scale to the view
         * scale.
         */
        public float scaleX(float horizontal) {
            return horizontal * overlay.widthScaleFactor;
        }

        /**
         * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
         */
        public float scaleY(float vertical) {
            return vertical * overlay.heightScaleFactor;
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

        public void postInvalidate() {
            overlay.postInvalidate();
        }
    }
}
