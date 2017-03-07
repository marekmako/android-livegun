package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;


public class FG42WeaponData extends WeaponDataParcel {

    public FG42WeaponData() {
        super("FG 42",
                R.drawable.profile_fg42,
                R.drawable.anim_fg42,
                R.drawable.anim_one_shot,
                R.raw.rifleshot,
                10,
                0);
    }
}
