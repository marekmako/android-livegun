package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;

public final class MachineGunWeaponData extends WeaponDataParcel {

    public MachineGunWeaponData() {
        super("Machine Gun",
                R.drawable.profile_machinegun,
                R.drawable.anim_machine_gun,
                R.drawable.anim_shot_ten,
                R.raw.machinegun_sound,
                10,
                0);
    }
}
