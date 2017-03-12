package com.app.maki.livegun;

import android.content.Context;
import android.content.SharedPreferences;

public class Score {

    private static final String K_SCORE_FILE = "K_SCORE_FILE";

    private static final String K_KILLS = "K_KILLS";

    private SharedPreferences mSharedPreferences;

    public Score(Context context) {
        mSharedPreferences = context.getSharedPreferences(K_SCORE_FILE, Context.MODE_PRIVATE);
    }

    void addKill() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(K_KILLS, 1 + countKills());
        editor.commit();
    }

    int countKills() {
        return mSharedPreferences.getInt(K_KILLS, 0);
    }
}
