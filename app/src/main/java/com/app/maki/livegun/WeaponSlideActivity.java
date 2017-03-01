package com.app.maki.livegun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.app.maki.livegun.weapon.BazookaWeaponData;
import com.app.maki.livegun.weapon.FG42WeaponData;
import com.app.maki.livegun.weapon.FlameMachineWeaponData;
import com.app.maki.livegun.weapon.MachineGunWeaponData;
import com.app.maki.livegun.weapon.NambuWeaponData;
import com.app.maki.livegun.weapon.P99WeaponData;
import com.app.maki.livegun.weapon.RPGWeaponData;
import com.app.maki.livegun.weapon.XPR50WeaponData;

public class WeaponSlideActivity extends FragmentActivity {

    public static final String K_WEAPON_RESULT = "k_weapon_result";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weapon_slide);

        ViewPager mPager = (ViewPager) findViewById(R.id.vp_weapon_slide);
        mPager.setAdapter(new WeaponSlideAdapter(getSupportFragmentManager()));
    }

    public void onWeaponDidSelect(@NonNull WeaponDataParcel weaponData) {
        Intent intent = new Intent();
        intent.putExtra(K_WEAPON_RESULT, weaponData);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class WeaponSlideAdapter extends FragmentStatePagerAdapter {

        private final Class[] weaponsData = new Class[] {
                NambuWeaponData.class,
                P99WeaponData.class,
                FG42WeaponData.class,
                XPR50WeaponData.class,
                FlameMachineWeaponData.class,
                MachineGunWeaponData.class,
                RPGWeaponData.class,
                BazookaWeaponData.class,
        };

        public WeaponSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            WeaponDataParcel weaponData = null;
            try {
                weaponData = (WeaponDataParcel) weaponsData[position].newInstance();
                return WeaponSlidePageFragment.newInstance(weaponData);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public int getCount() {
            return weaponsData.length;
        }
    }
}
