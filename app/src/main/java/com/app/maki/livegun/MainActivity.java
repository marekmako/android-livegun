package com.app.maki.livegun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int K_WEAPON_RESULT_CODE = 1;

    public static final String K_SELECTED_WEAPON = "k_selected_weapon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectWeaponButton = (Button) findViewById(R.id.b_select_weapon);
        selectWeaponButton.setOnClickListener(selectWeaponClickListener);
    }

    private View.OnClickListener selectWeaponClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, WeaponSlideActivity.class);
            startActivityForResult(intent, K_WEAPON_RESULT_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == K_WEAPON_RESULT_CODE && resultCode == RESULT_OK) {
            WeaponDataParcel weaponData = data.getExtras().getParcelable(WeaponSlideActivity.K_WEAPON_RESULT);
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra(K_SELECTED_WEAPON, weaponData);
            startActivity(intent);
        }
    }
}
