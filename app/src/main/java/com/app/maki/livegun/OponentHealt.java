package com.app.maki.livegun;

import android.content.Context;
import android.widget.ProgressBar;

import java.util.Random;

public final class OponentHealt {

    private Context mContext;

    private ProgressBar mProgressBar;

    private boolean isAlive = true;

    public DeathListener deathListener = null;

    private int[] mDeathSoundResourcePool = new int[] {
            R.raw.death_sound1,
            R.raw.death_sound2,
            R.raw.death_sound3,
    };

    public OponentHealt(Context context, ProgressBar progressBar) {
        mContext = context;
        mProgressBar = progressBar;
        mProgressBar.setMax(100);
        mProgressBar.setProgress(mProgressBar.getMax());
    }

    public void onHit(Weapon weapon) {
        int currProgressValue = mProgressBar.getProgress() - weapon.getDemageIndex();
        if (currProgressValue <= 0) {
            currProgressValue = 0;

            if (isAlive) {
                onDeath();
            }

            if (deathListener != null) {
                deathListener.onDeath();
            }
        }
        mProgressBar.setProgress(currProgressValue);
    }

    private void onDeath() {
        isAlive = false;
        // play death sound
        final Random random = new Random();
        final int index = random.nextInt(mDeathSoundResourcePool.length);
        SharedSoundPool.getInstance().playEffectSound(mDeathSoundResourcePool[index], mContext);
    }


    public interface DeathListener {
        void onDeath();
    }
}
