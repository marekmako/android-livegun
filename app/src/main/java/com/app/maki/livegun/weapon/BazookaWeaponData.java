package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;


public class BazookaWeaponData extends WeaponDataParcel {

    public BazookaWeaponData() {
        super("Bazooka",
                R.drawable.profile_bazooka,
                R.drawable.anim_bazooka,
                R.drawable.anim_one_shot,
                R.raw.rifleshot,
                10,
                0);
    }
}
