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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FaceGraphics extends CameraEffectsOverlay.Graphic {

    private Context context;

    private Face face;

    final private Random random = new Random();

    private ArrayList<String> availableEffectMethods = new ArrayList<String>();

    private ArrayList<String> usedEffects = new ArrayList<String>();


    public FaceGraphics(@NonNull Context context, @NonNull CameraEffectsOverlay overlay) {
        super(overlay);
        this.context = context;

        availableEffectMethods.add("drawBloodEffect");
    }

    public void updateFace(Face face) {
        this.face = face;
        postInvalidate();
    }

    public void onHit() {
        if (availableEffectMethods.size() > 0) {
            int index = random.nextInt(availableEffectMethods.size());
            usedEffects.add(availableEffectMethods.get(index));
            availableEffectMethods.remove(index);
        }
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

        for (String methodName : usedEffects) {
            try {
                Method method = this.getClass().getMethod(methodName, Canvas.class);
                method.invoke(this, canvas);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawBloodEffect(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point noseRect = getFacePoint(Landmark.NOSE_BASE);
        if (faceRect != null && noseRect != null) {
            Drawable bloodEffect = context.getResources().getDrawable(R.drawable.ic_blood_effect);

            int bloodEffectWidth = faceRect.width() / 4;
            int bloodEffectHeight = faceRect.height() / 4;

            int left = noseRect.x - bloodEffectWidth / 2;
            int top = noseRect.y;
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
