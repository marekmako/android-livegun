package com.app.maki.livegun;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;

public class Weapon {

    private Context mContext;

    private AnimationDrawable mShotAnimation;

    private MediaPlayer mShotMediaPlayer;

    public Weapon(Context context, WeaponDataParcel weaponParcel) {
        mContext = context;

        mShotAnimation = (AnimationDrawable) mContext.getResources().getDrawable(weaponParcel.getDrawableShootAnimation());
        mShotMediaPlayer = MediaPlayer.create(mContext, weaponParcel.getRawShootSound());
    }

    public AnimationDrawable getWeaponShotAnimation() {
        return mShotAnimation;
    }

    public void onShot() {
        shotAnimate();
        shotPlaySound();
    }

    private void shotAnimate() {
        int duration = 10;
        for (int i = 0; i < mShotAnimation.getNumberOfFrames(); i++) {
            duration += mShotAnimation.getDuration(i);
        }

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mShotAnimation.stop();
                mShotAnimation.selectDrawable(0);
            }
        };
        handler.postDelayed(runnable, duration);
        mShotAnimation.start();
    }

    private void shotPlaySound() {
        mShotMediaPlayer.start();
    }


}
