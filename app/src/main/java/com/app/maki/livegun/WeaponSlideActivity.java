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
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.app.maki.livegun.weapon.BazookaWeaponData;
import com.app.maki.livegun.weapon.FG42WeaponData;
import com.app.maki.livegun.weapon.FlameMachineWeaponData;
import com.app.maki.livegun.weapon.MachineGunWeaponData;
import com.app.maki.livegun.weapon.NambuWeaponData;
import com.app.maki.livegun.weapon.P99WeaponData;
import com.app.maki.livegun.weapon.RPGWeaponData;
import com.app.maki.livegun.weapon.XPR50WeaponData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class WeaponSlideActivity extends FragmentActivity implements WeaponSlidePageFragment.WeaponAdsListener {

    public static final String K_WEAPON_RESULT = "k_weapon_result";

    private RewardedVideoAd mAd;

    private boolean mWeaponIsLoaded = false;

    private WeaponSlideAdapter mAdapter;

    private int mCurrPagePosition = 0;

    private RewardedVideoAdListener mRewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            Log.d("ADS", "onRewardedVideoAdLoaded");
            mWeaponIsLoaded = true;
            mAdapter.getFragmentAt(mCurrPagePosition).weaponActive();
        }

        @Override
        public void onRewardedVideoAdOpened() {
            Log.d("ADS", "onRewardedVideoAdOpened");
        }

        @Override
        public void onRewardedVideoStarted() {
            Log.d("ADS", "onRewardedVideoStarted");
        }

        @Override
        public void onRewardedVideoAdClosed() {
            Log.d("ADS", "onRewardedVideoAdClosed");
            mWeaponIsLoaded = false;
            mAdapter.getFragmentAt(mCurrPagePosition).weaponInactive();
            mAd.loadAd(getResources().getString(R.string.add_video_weapon_id), new AdRequest.Builder().build());
        }

        @Override
        public void onRewarded(RewardItem rewardItem) {
            Log.d("ADS", "onRewarded");
            WeaponSlidePageFragment fragment = mAdapter.getFragmentAt(mCurrPagePosition);
            fragment.setWeaponUnlockedByAd();
            fragment.weaponSelected();
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            Log.d("ADS", "onRewardedVideoAdLeftApplication");
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {
            Log.d("ADS", "onRewardedVideoAdFailedToLoad");
            mWeaponIsLoaded = true;
            mAdapter.getFragmentAt(mCurrPagePosition).weaponActive();
        }
    };

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mCurrPagePosition = position;
            if (isAddLoaded()) {
                mAdapter.getFragmentAt(position).weaponActive();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weapon_slide);

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(mRewardedVideoAdListener);
        mAd.loadAd(getResources().getString(R.string.add_video_weapon_id), new AdRequest.Builder().build());

        ViewPager mPager = (ViewPager) findViewById(R.id.vp_weapon_slide);
        mAdapter = new WeaponSlideAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(mPageChangeListener);
    }

    public void onWeaponDidSelect(@NonNull WeaponDataParcel weaponData) {
        Intent intent = new Intent();
        intent.putExtra(K_WEAPON_RESULT, weaponData);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showAdd() {
        mAd.show();
    }



    private class WeaponSlideAdapter extends FragmentStatePagerAdapter {

        private SparseArray<WeaponSlidePageFragment> mFragments = new SparseArray<>();

        private final Class[] weaponsData = new Class[] {
                NambuWeaponData.class,
                XPR50WeaponData.class,
                P99WeaponData.class, // 10 kills
                FG42WeaponData.class, // 25
                MachineGunWeaponData.class, // 50
                FlameMachineWeaponData.class, // 75
                RPGWeaponData.class, // 100
                BazookaWeaponData.class, //150
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WeaponSlidePageFragment fragment = (WeaponSlidePageFragment) super.instantiateItem(container, position);
            mFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public WeaponSlidePageFragment getFragmentAt(int position) {
            return mFragments.get(position);
        }
    }




    @Override
    public boolean isAddLoaded() {
        return mWeaponIsLoaded;
    }
}
