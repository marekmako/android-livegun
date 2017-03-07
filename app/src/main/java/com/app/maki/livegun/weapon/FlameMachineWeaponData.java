package com.app.maki.livegun.weapon;

import com.app.maki.livegun.R;
import com.app.maki.livegun.WeaponDataParcel;


public class FlameMachineWeaponData extends WeaponDataParcel {

    public FlameMachineWeaponData() {
        super("Flame Machine",
                R.drawable.profile_flamemachine,
                R.drawable.anim_flame_machine,
                R.drawable.anim_one_shot,
                R.raw.rifleshot,
                10,
                0);
    }
}
