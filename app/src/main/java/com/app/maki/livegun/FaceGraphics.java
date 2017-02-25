package com.app.maki.livegun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

public class FaceGraphics extends CameraEffectsOverlay.Graphic {

    private Context context;
    private Face face;


    public FaceGraphics(@NonNull Context context, @NonNull CameraEffectsOverlay overlay) {
        super(overlay);
        this.context = context;
    }

    public void updateFace(Face face) {
        this.face = face;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        if (face == null) {
            return;
        }

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);


        Rect faceRect = getFaceRect();
        if (faceRect != null) {
            canvas.drawRect(faceRect.left, faceRect.top, faceRect.right, faceRect.bottom, paint);
        }

//        Point leftEyePoint = getFacePoint(Landmark.LEFT_EYE);
//        if (leftEyePoint != null) {
//            canvas.drawRect(leftEyePoint.x, leftEyePoint.y, leftEyePoint.x + 10, leftEyePoint.y + 10, paint);
//        }
//
//        Point rightEyePoint = getFacePoint(Landmark.RIGHT_EYE);
//        if (rightEyePoint != null) {
//            canvas.drawRect(rightEyePoint.x, rightEyePoint.y, rightEyePoint.x + 10, rightEyePoint.y + 10, paint);
//        }
//
//        Point nose = getFacePoint(Landmark.NOSE_BASE);
//        if (nose != null) {
//            canvas.drawRect(nose.x, nose.y, nose.x + 10, nose.y + 10, paint);
//        }
//
//        Point bottomMouth = getFacePoint(Landmark.BOTTOM_MOUTH);
//        if (bottomMouth != null) {
//            canvas.drawRect(bottomMouth.x, bottomMouth.y, bottomMouth.x + 10, bottomMouth.y + 10, paint);
//        }

        Point nose = getFacePoint(Landmark.NOSE_BASE);
        if (nose != null) {
            Drawable bloodEffect = context.getResources().getDrawable(R.drawable.ic_blood_effect);

            int bloodEffectWidth = faceRect.width() / 4;
            int bloodEffectHeight = faceRect.height() / 4;

            int left = nose.x - bloodEffectWidth / 2;
            int top = nose.y;
            int right = left + bloodEffectWidth;
            int bottom = top + bloodEffectHeight;

            bloodEffect.setBounds(new Rect(left, top, right, bottom));
            bloodEffect.draw(canvas);
        }
    }

    @Nullable
    private Point getFacePoint(int landmarkType) {
        Landmark landmark = getLandmark(landmarkType);
        if (landmark != null) {
            return new Point(
                    (int) translateX(landmark.getPosition().x),
                    (int) translateY(landmark.getPosition().y)
            );
        }

        return null;
    }

    @Nullable
    private Landmark getLandmark(int landmarkType) {
        if (face != null) {
            for (Landmark landmark : face.getLandmarks()) {
                if (landmark.getType() == landmarkType) {
                    return landmark;
                }
            }
        }
        return null;
    }

    @Nullable
    public Rect getFaceRect() {
        if (face == null) return null;

        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);

        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2);
        float yOffset = scaleY(face.getHeight() / 2);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;

        return new Rect((int)left, (int)top, (int)right, (int)bottom);
    }
}
