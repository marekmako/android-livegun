package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;

public final class MachineGunWeaponData extends WeaponDataParcel {

    public MachineGunWeaponData() {
        super("Machine Gun",
                R.drawable.profile_machinegun,
                R.drawable.anim_machine_gun,
                R.drawable.anim_one_shot,
                R.raw.rifleshot,
                10,
                0);
    }
}
