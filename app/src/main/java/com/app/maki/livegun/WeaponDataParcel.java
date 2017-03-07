package com.app.maki.livegun;


import android.os.Parcel;
import android.os.Parcelable;

public class WeaponDataParcel implements Parcelable {

    private String mName;
    private int mDrawableProfile;
    private int mDrawableWeaponAnimation;
    private int mDrawableShotAnimation;
    private int mRawShootSound;
    private int mHitRandomIndex;
    private int mDemageIndex;

    public WeaponDataParcel(String name,
                            int drawableProfile,
                            int drawableWeaponAnimation,
                            int drawableShotAnimation,
                            int rawShotSound,
                            int hitRandomIndex,
                            int demageIndex) {
        mName = name;
        mDrawableProfile = drawableProfile;
        mDrawableWeaponAnimation = drawableWeaponAnimation;
        mDrawableShotAnimation = drawableShotAnimation;
        mRawShootSound = rawShotSound;
        mHitRandomIndex = hitRandomIndex;
        mDemageIndex = demageIndex;
    }

    private WeaponDataParcel(Parcel source) {
        mName = source.readString();
        mDrawableProfile = source.readInt();
        mDrawableWeaponAnimation = source.readInt();
        mDrawableShotAnimation = source.readInt();
        mRawShootSound = source.readInt();
        mHitRandomIndex = source.readInt();
        mDemageIndex = source.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mDrawableProfile);
        dest.writeInt(mDrawableWeaponAnimation);
        dest.writeInt(mDrawableShotAnimation);
        dest.writeInt(mRawShootSound);
        dest.writeInt(mHitRandomIndex);
        dest.writeInt(mDemageIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<WeaponDataParcel> CREATOR = new Creator<WeaponDataParcel>() {
        @Override
        public WeaponDataParcel createFromParcel(Parcel source) {
            return new WeaponDataParcel(source);
        }

        @Override
        public WeaponDataParcel[] newArray(int size) {
            return new WeaponDataParcel[size];
        }
    };

    public String getName() {
        return mName;
    }

    public int getDrawableProfile() {
        return mDrawableProfile;
    }

    public int getDrawableWeaponAnimation() {
        return mDrawableWeaponAnimation;
    }

    public int getDrawableShotAnimation() {
        return mDrawableShotAnimation;
    }

    public int getRawShootSound() {
        return mRawShootSound;
    }

    public int getHitRandomIndex() {
        return mHitRandomIndex;
    }

    public int getDemageIndex() {
        return mDemageIndex;
    }
}
