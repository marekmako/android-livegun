package com.app.maki.livegun;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

public class Weapon {

    private Context mContext;

    private AnimationDrawable mWaponAnimation;

    private AnimationDrawable mShotAnimation;

    private MediaPlayer mShotMediaPlayer;

    @Nullable private ImageView mShotImageView;

    public Weapon(Context context, WeaponDataParcel weaponParcel) {
        mContext = context;

        mWaponAnimation = (AnimationDrawable) mContext.getResources().getDrawable(weaponParcel.getDrawableWeaponAnimation());
        mShotAnimation = (AnimationDrawable) mContext.getResources().getDrawable(weaponParcel.getDrawableShotAnimation());
        mShotMediaPlayer = MediaPlayer.create(mContext, weaponParcel.getRawShootSound());
    }

    public void setShotImageView(ImageView iv) {
        mShotImageView = iv;
    }

    public AnimationDrawable getWeaponAnimation() {
        return mWaponAnimation;
    }

    public AnimationDrawable getShotAnimation() {
        return mShotAnimation;
    }

    public void onShot() {
        shotAnimate();
        weaponAnimate();
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
                if (mShotImageView != null) {
                    mShotImageView.setVisibility(View.INVISIBLE);
                }
            }
        };
        if (mShotImageView != null) {
            mShotImageView.setVisibility(View.VISIBLE);
        }
        handler.postDelayed(runnable, duration);
        mShotAnimation.start();
    }

    private void weaponAnimate() {
        int duration = 10;
        for (int i = 0; i < mWaponAnimation.getNumberOfFrames(); i++) {
            duration += mWaponAnimation.getDuration(i);
        }

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWaponAnimation.stop();
                mWaponAnimation.selectDrawable(0);
            }
        };
        handler.postDelayed(runnable, duration);
        mWaponAnimation.start();
    }

    private void shotPlaySound() {
        mShotMediaPlayer.start();
    }


}
