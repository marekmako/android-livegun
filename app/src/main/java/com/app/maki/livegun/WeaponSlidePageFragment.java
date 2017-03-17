package com.app.maki.livegun;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeaponSlidePageFragment extends Fragment {

    private static final String K_WEAPON_DATA = "k_weapon_data";

    private WeaponAdsListener mWeaponAdsListener;

    private WeaponDataParcel mWeaponData;

    private Button mSelectWeaponButton;

    private ProgressBar mLoadingWeaponProgressBar;

    private Score mScore;

    private boolean mWeaponUnlockedByAd = false;

    private View.OnClickListener mSelectedWeaponButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isWeaponUnlocked() && getActivity() instanceof WeaponSlideActivity) {
                weaponSelected();

            } else {
                // TODO: 16/03/2017 dokoncit alert pre reklamu 
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setTitle("Title");
                alertBuilder.setMessage("Message");
                alertBuilder.setPositiveButton("Watch to unlock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getActivity() instanceof WeaponSlideActivity) {
                            ((WeaponSlideActivity) getActivity()).showAdd();
                        }
                    }
                });
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.show();
            }
        }
    };

    public WeaponSlidePageFragment() {
        // Required empty public constructor
    }

    public static WeaponSlidePageFragment newInstance(WeaponDataParcel weaponData) {
        WeaponSlidePageFragment fragment = new WeaponSlidePageFragment();
        Bundle args = new Bundle();
        args.putParcelable(K_WEAPON_DATA, weaponData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScore = new Score(getContext());
        if (getArguments() != null) {
            mWeaponData = getArguments().getParcelable(K_WEAPON_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weapon_slide_page, container, false);

        TextView weaponNameTextView = (TextView) view.findViewById(R.id.tv_weapon_name);
        ImageView weaponImageView = (ImageView) view.findViewById(R.id.iv_weapon_profile);

        if (mWeaponData != null) {
            weaponNameTextView.setText(mWeaponData.getName());
            weaponImageView.setImageDrawable(getResources().getDrawable(mWeaponData.getDrawableProfile()));
        }

        mSelectWeaponButton = (Button) view.findViewById(R.id.b_selected_weapon);
        mSelectWeaponButton.setOnClickListener(mSelectedWeaponButtonClickListener);
        mSelectWeaponButton.setEnabled(false);

        mLoadingWeaponProgressBar = (ProgressBar) view.findViewById(R.id.pb_weapon_loading);


        if (isWeaponUnlocked() || mWeaponAdsListener.isAddLoaded()) {
            weaponActive();
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof WeaponAdsListener) {
            mWeaponAdsListener = (WeaponAdsListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mWeaponAdsListener = null;
    }

    public void weaponActive() {
        mSelectWeaponButton.setEnabled(true);
        mLoadingWeaponProgressBar.setVisibility(View.GONE);
    }

    public void weaponInactive() {
        if (!isWeaponUnlocked()) {
            mSelectWeaponButton.setEnabled(false);
            mLoadingWeaponProgressBar.setVisibility(View.VISIBLE);
        }
    }

    void setWeaponUnlockedByAd() {
        mWeaponUnlockedByAd = true;
    }

    private boolean isWeaponUnlocked() {
        return (mScore.countKills() >= mWeaponData.getRequiredKillsForUnlock() || mWeaponUnlockedByAd);
    }

    void weaponSelected() {
        WeaponSlideActivity weaponSlideActivity = (WeaponSlideActivity) getActivity();
        weaponSlideActivity.onWeaponDidSelect(mWeaponData);
    }

    interface WeaponAdsListener {
        boolean isAddLoaded();
    }
}
