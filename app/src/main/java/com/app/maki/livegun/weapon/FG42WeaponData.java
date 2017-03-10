package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;


public class FG42WeaponData extends WeaponDataParcel {

    public FG42WeaponData() {
        super("FG 42",
                R.drawable.profile_fg42,
                R.drawable.anim_fg42,
                R.drawable.anim_shot_five,
                R.raw.fg42_sound,
                10,
                0);
    }
}
