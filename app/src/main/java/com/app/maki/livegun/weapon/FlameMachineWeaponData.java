package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;


public class FlameMachineWeaponData extends WeaponDataParcel {

    public FlameMachineWeaponData() {
        super("Flame Machine",
                R.drawable.profile_flamemachine,
                R.drawable.anim_flame_machine,
                R.drawable.anim_shot_flame,
                R.raw.flame_machine,
                10,
                0,
                75);
    }
}
