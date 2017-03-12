package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;


public class BazookaWeaponData extends WeaponDataParcel {

    public BazookaWeaponData() {
        super("Bazooka",
                R.drawable.profile_bazooka,
                R.drawable.anim_bazooka,
                R.drawable.anim_shot_explosion,
                R.raw.bazooka_sound,
                10,
                60);
    }
}
