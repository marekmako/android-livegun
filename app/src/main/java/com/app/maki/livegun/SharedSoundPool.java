package com.app.maki.livegun;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

final class SharedSoundPool {

    private SoundPool mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    private Map<Integer, Integer> mSoundPoolResourceCache = new HashMap<Integer, Integer>();

    private static SharedSoundPool sInstance = null;

    synchronized static SharedSoundPool getInstance() {
        if (sInstance == null) {
            sInstance = new SharedSoundPool();
        }
        return sInstance;
    }

    private SharedSoundPool() {}

    void preloadSound(final Context context) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadSound(context, R.raw.effect_hovno_na_hlave_sound);
                loadSound(context, R.raw.effect_rana_nos_sound);
                loadSound(context, R.raw.effect_modrina_oko_lave_sound);
                loadSound(context, R.raw.effect_rana_celo_stred_sound);
                loadSound(context, R.raw.effect_sekera_celo_prave_sound);
                loadSound(context, R.raw.effect_noz_celo_lave);
                loadSound(context, R.raw.effect_vrtacka_lave_lico_sound);
                loadSound(context, R.raw.effect_krvave_usta_sound);
                loadSound(context, R.raw.effect_krvave_prave_oko_sound);

                loadSound(context, R.raw.bazooka_sound);
                loadSound(context, R.raw.fg42_sound);
                loadSound(context, R.raw.flame_machine);
                loadSound(context, R.raw.machinegun_sound);
                loadSound(context, R.raw.nambu_shot);
                loadSound(context, R.raw.p99_shot);
                loadSound(context, R.raw.rpg_sound);
                loadSound(context, R.raw.xpr50_sound);

                loadSound(context, R.raw.death_sound1);
                loadSound(context, R.raw.death_sound2);
                loadSound(context, R.raw.death_sound3);
            }
        };
        handler.post(runnable);
    }

    private void loadSound(Context context, int resource) {
        Integer sampleId = mSoundPool.load(context, resource, 1);
        mSoundPoolResourceCache.put(resource, sampleId);
    }

    void playEffectSound(int resource, Context context) {
        Integer sampleId = mSoundPoolResourceCache.get(resource);
        if (sampleId != null) {
            mSoundPool.play(sampleId, 1, 1, 1, 0, 1);

        }
    }
}
