package com.app.maki.livegun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class FaceGraphics extends EffectOverlaySurface.Graphic {

    final private Object mLock = new Object();

    private Context mContext;

    private Face mFace;

    final private Random mRandom = new Random();

    private ArrayList<String> mAvailableEffectMethods = new ArrayList<String>();

    private ArrayList<String> mUsedEffects = new ArrayList<String>();

    private SharedSoundPool mSoundPool = SharedSoundPool.getInstance();

    FaceGraphics(@NonNull Context context, @NonNull EffectOverlaySurface overlay) {
        super(overlay);
        this.mContext = context;
        mAvailableEffectMethods.add("drawNozCeloLave");
        mAvailableEffectMethods.add("drawVrtackaLaveLico");
        mAvailableEffectMethods.add("drawSekeraCeloPrave");
        mAvailableEffectMethods.add("drawKrvaveUsta");
        mAvailableEffectMethods.add("drawModrinaOkoLave");
        mAvailableEffectMethods.add("drawRanaNos");
        mAvailableEffectMethods.add("drawKrvavePraveOko");
        mAvailableEffectMethods.add("drawRanaCeloStred");
}

    public void updateFace(Face face) {
        this.mFace = face;
        postInvalidate();
    }

    void onHit() {
        synchronized (mLock) {
            if (mAvailableEffectMethods.size() > 0) {
                int index = mRandom.nextInt(mAvailableEffectMethods.size());

                final String effectMethodName = mAvailableEffectMethods.get(index);
                final String soundMethodName = effectMethodName + "Sound";

                mUsedEffects.add(effectMethodName);
                mAvailableEffectMethods.remove(index);


                // play sound if available
                try {
                    Method method = this.getClass().getMethod(soundMethodName);
                    method.invoke(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mFace == null) {
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

        synchronized (mLock) {
            for (String methodName : mUsedEffects) {
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
    }

    public void  drawRanaNos(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.NOSE_BASE);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.rana_nos);
            Dimension dimension = scale(
                    new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                    new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht / 4;
            float effectHeight = dimension.mHeight / 4;
            float left = facePart.x - effectWidth / 2;
            float top = facePart.y - effectHeight / 1.2f;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawRanaNosSound() {
        playEffectSound(R.raw.effect_rana_nos_sound);
    }

    public void drawModrinaOkoLave(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.LEFT_EYE);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.modrina);
            Dimension dimension = scale(
                    new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                    new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht / 4;
            float effectHeight = dimension.mHeight / 4;
            float left = facePart.x - effectWidth / 2;
            float top = facePart.y + effectHeight / 6;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawModrinaOkoLaveSound() {
        playEffectSound(R.raw.effect_modrina_oko_lave_sound);
    }

    public void drawRanaCeloStred(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.RIGHT_EYE);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.rana_lico);
            Dimension dimension = scale(
                    new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                    new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht / 2;
            float effectHeight = dimension.mHeight / 2;
            float left = facePart.x - effectWidth / 5;
            float top = facePart.y - effectHeight;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawRanaCeloStredSound() {
        playEffectSound(R.raw.effect_rana_celo_stred_sound);
    }

    public void drawSekeraCeloPrave(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.RIGHT_EYE);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.sekera);
            Dimension dimension = scale(
                    new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                    new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht / 1.5f;
            float effectHeight = dimension.mHeight / 1.5f;
            float left = facePart.x - effectWidth;
            float top = facePart.y - effectHeight * 1.5f;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawSekeraCeloPraveSound() {
        playEffectSound(R.raw.effect_sekera_celo_prave_sound);
    }

    public void drawNozCeloLave(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.LEFT_EYE);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.noz_celo);
            Dimension dimension = scale(
                    new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                    new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht / 1.5f;
            float effectHeight = dimension.mHeight / 1.5f;
            float left = facePart.x;
            float top = facePart.y - effectHeight;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawNozCeloLaveSound() {
        playEffectSound(R.raw.effect_noz_celo_lave);
    }

    public void drawVrtackaLaveLico(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.LEFT_CHEEK);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.vrtcka_ucho);
            Dimension dimension = scale(
                    new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                    new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht;
            float effectHeight = dimension.mHeight;
            float left = facePart.x + effectWidth / 10;
            float top = facePart.y - effectHeight / 2;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawVrtackaLaveLicoSound() {
        playEffectSound(R.raw.effect_vrtacka_lave_lico_sound);
    }

    public void drawKrvaveUsta(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.BOTTOM_MOUTH);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.usta);
            Dimension dimension = scale(
                    new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                    new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht / 3;
            float effectHeight = dimension.mHeight / 3;
            float left = facePart.x - effectWidth / 2;
            float top = facePart.y - effectHeight / 1.5f;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawKrvaveUstaSound() {
        playEffectSound(R.raw.effect_krvave_usta_sound);
    }

    public void drawKrvavePraveOko(@NonNull Canvas canvas) {
        Rect faceRect = getFaceRect();
        Point facePart = getFacePoint(Landmark.RIGHT_EYE);
        if (faceRect != null && facePart != null) {
            Drawable effect = mContext.getResources().getDrawable(R.drawable.oko);
            Dimension dimension = scale(
                new Dimension(effect.getIntrinsicWidth(), effect.getIntrinsicHeight()),
                new Dimension(faceRect.width(), faceRect.height()));

            float effectWidth = dimension.mWidht / 4;
            float effectHeight = dimension.mHeight / 4;
            float left = facePart.x - effectWidth / 2;
            float top = facePart.y - effectHeight / 4;
            float right = left + effectWidth;
            float bottom = top + effectHeight;

            RectF rectF = new RectF(left, top, right, bottom);
            Rect rect = new Rect();
            rectF.round(rect);

            effect.setBounds(rect);
            effect.draw(canvas);
        }
    }

    public void drawKrvavePraveOkoSound() {
        playEffectSound(R.raw.effect_krvave_prave_oko_sound);
    }

    private void playEffectSound(int resource) {
        mSoundPool.playEffectSound(resource, mContext);
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
        if (mFace != null) {
            for (Landmark landmark : mFace.getLandmarks()) {
                if (landmark.getType() == landmarkType) {
                    return landmark;
                }
            }
        }
        return null;
    }

    @Nullable
    public Rect getFaceRect() {
        if (mFace == null) return null;

        float x = translateX(mFace.getPosition().x + mFace.getWidth() / 2);
        float y = translateY(mFace.getPosition().y + mFace.getHeight() / 2);

        // Draws a bounding box around the mFace.
        float xOffset = scaleX(mFace.getWidth() / 2);
        float yOffset = scaleY(mFace.getHeight() / 2);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;

        return new Rect((int)left, (int)top, (int)right, (int)bottom);
    }



    private class Dimension {
        private float mWidht;
        private float mHeight;

        Dimension(float widht, float height) {
            mWidht = widht;
            mHeight = height;
        }
    }
    ///  http://stackoverflow.com/questions/10245220/java-image-resize-maintain-aspect-ratio
    private Dimension scale(Dimension imageSize, Dimension boundary) {
        float original_width = imageSize.mWidht;
        float original_height = imageSize.mHeight;
        float bound_width = boundary.mWidht;
        float bound_height = boundary.mHeight;
        float new_width = original_width;
        float new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}
