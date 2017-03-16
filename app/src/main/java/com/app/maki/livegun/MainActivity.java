package com.app.maki.livegun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.vision.CameraSource;

public class MainActivity extends AppCompatActivity {

    private static final int K_WEAPON_RESULT_CODE = 1;

    public static final String K_SELECTED_WEAPON = "k_selected_weapon";

    private int mCameraFacing = CameraSource.CAMERA_FACING_BACK;
    public static final String K_CAMERA_FACING = "K_CAMERA_FACING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedSoundPool.getInstance().preloadSound(getApplicationContext());

        Button oponentModeButton = (Button) findViewById(R.id.b_oponent_mode);
        oponentModeButton.setOnClickListener(oponentModeButtonClickListener);

        Button scuicideModeButton = (Button) findViewById(R.id.b_scuicide_mode);
        scuicideModeButton.setOnClickListener(scuicideModeButtonClickListener);
    }

    private View.OnClickListener oponentModeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, WeaponSlideActivity.class);
            startActivityForResult(intent, K_WEAPON_RESULT_CODE);
            mCameraFacing = CameraSource.CAMERA_FACING_BACK;
        }
    };

    private View.OnClickListener scuicideModeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, WeaponSlideActivity.class);
            startActivityForResult(intent, K_WEAPON_RESULT_CODE);
            mCameraFacing = CameraSource.CAMERA_FACING_FRONT;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == K_WEAPON_RESULT_CODE && resultCode == RESULT_OK) {
            WeaponDataParcel weaponData = data.getExtras().getParcelable(WeaponSlideActivity.K_WEAPON_RESULT);
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra(K_SELECTED_WEAPON, weaponData);
            intent.putExtra(K_CAMERA_FACING, mCameraFacing);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(K_CAMERA_FACING, mCameraFacing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCameraFacing = savedInstanceState.getInt(K_CAMERA_FACING);
    }
}
