package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;

public final class RPGWeaponData extends WeaponDataParcel {

    public RPGWeaponData() {
        super("RPG",
                R.drawable.pofile_rpg,
                R.drawable.anim_rpg,
                R.drawable.anim_shot_explosion,
                R.raw.rpg_sound,
                10,
                0,
                100);
    }
}
