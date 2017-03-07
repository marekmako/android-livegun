package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;

public final class RPGWeaponData extends WeaponDataParcel {

    public RPGWeaponData() {
        super("RPG",
                R.drawable.pofile_rpg,
                R.drawable.anim_rpg,
                R.drawable.anim_one_shot,
                R.raw.rifleshot,
                10,
                0);
    }
}
