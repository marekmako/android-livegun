package com.app.maki.livegun;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

final class SharedSoundPool implements SoundPool.OnLoadCompleteListener {

    private SoundPool mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

    private Map<Integer, Integer> mSoundPoolResourceCache = new HashMap<Integer, Integer>();

    private static SharedSoundPool sInstance = null;

    synchronized static SharedSoundPool getInstance() {
        if (sInstance == null) {
            sInstance = new SharedSoundPool();
        }
        return sInstance;
    }

    private SharedSoundPool() {
        mSoundPool.setOnLoadCompleteListener(this);
    }

    void playEffectSound(int resource, Context context) {
        Integer sampleId = mSoundPoolResourceCache.get(resource);
        if (sampleId != null) {
            mSoundPool.play(sampleId, 1, 1, 1, 0, 1);

        } else {
            sampleId = mSoundPool.load(context, resource, 1);
            mSoundPoolResourceCache.put(resource, sampleId);
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            soundPool.play(sampleId, 1, 1, 1, 0, 1);
        }
    }
}
