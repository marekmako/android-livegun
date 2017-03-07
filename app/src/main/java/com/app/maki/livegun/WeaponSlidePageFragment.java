package com.app.maki.livegun;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link WeaponSlidePageFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link WeaponSlidePageFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class WeaponSlidePageFragment extends Fragment {
//    private OnFragmentInteractionListener mListener;

    private static final String K_WEAPON_DATA = "k_weapon_data";

    private WeaponDataParcel mWeaponData;

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

        Button selectedWeaponButton = (Button) view.findViewById(R.id.b_selected_weapon);
        selectedWeaponButton.setOnClickListener(selectedWeaponButtonClickListener);

        return view;
    }

    private View.OnClickListener selectedWeaponButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() instanceof WeaponSlideActivity) {
                WeaponSlideActivity weaponSlideActivity = (WeaponSlideActivity) getActivity();
                weaponSlideActivity.onWeaponDidSelect(mWeaponData);
            }
        }
    };

    //
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment WeaponSlidePageFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static WeaponSlidePageFragment newInstance(String param1, String param2) {
//        WeaponSlidePageFragment fragment = new WeaponSlidePageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }



//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
