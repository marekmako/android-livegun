package com.app.maki.livegun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERM_CODE = 2;

    private CameraSource mCameraSource;

    private CameraPreview mCameraPreview;

    private EffectOverlaySurface mCameraEffectsOverlay;

    private ImageView mAimImageView;
    private Rect mAimRect;

    private EffectsFaceTracker mFaceTracker;
    private FaceGraphics mFaceGraphics;

    private Weapon mWeapon;

    private OponentHealt mOponentHealt;

    private int mCameraFacing;

//    private TextView mKillsTextView;

    private Score mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        WeaponDataParcel weaponParcelPrototype = intent.getParcelableExtra(MainActivity.K_SELECTED_WEAPON);
        mWeapon = new Weapon(getApplicationContext(), weaponParcelPrototype);
        mCameraFacing = intent.getIntExtra(MainActivity.K_CAMERA_FACING, CameraSource.CAMERA_FACING_BACK);

        mCameraPreview = (CameraPreview) findViewById(R.id.sv_camera_preview);
        mCameraEffectsOverlay = (EffectOverlaySurface) findViewById(R.id.sv_effect_overlay);
        mAimImageView = (ImageView) findViewById(R.id.iv_aim);

        ImageView weaponImageView = (ImageView) findViewById(R.id.iv_weapon);
        weaponImageView.setImageDrawable(mWeapon.getWeaponAnimation());
        weaponImageView.setOnClickListener(onShotListener);

        ImageView mShotImageView = (ImageView) findViewById(R.id.iv_shot);
        mShotImageView.setImageDrawable(mWeapon.getShotAnimation());
        mWeapon.setShotImageView(mShotImageView);
        mShotImageView.setVisibility(View.INVISIBLE);

        mScore = new Score(getApplicationContext());

//        mKillsTextView = (TextView) findViewById(R.id.tv_kills);

        mFaceGraphics = new FaceGraphics(getApplicationContext(), mCameraEffectsOverlay);

        mFaceTracker = new EffectsFaceTracker(
                mCameraEffectsOverlay,
                mFaceGraphics);

        mOponentHealt = new OponentHealt(getApplicationContext(), (ProgressBar) findViewById(R.id.pb_healt));
        mOponentHealt.deathListener = new OponentHealt.DeathListener() {
            @Override
            public void onDeath() {
                mFaceGraphics.onDeath();
                // TODO: 12/03/2017 podskoci obrazovka
//                mKillsTextView.setText(String.valueOf(mScore.countKills()));
            }
        };

        // check camera permission
        int cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            createAndAssignCameraSource();

        } else {
            requestPermission();
        }
    }

    private View.OnClickListener onShotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mWeapon.onShot();

            if (hitTarget()) {
                mCameraEffectsOverlay.onHit();
                mFaceGraphics.onHit();
                mOponentHealt.onHit(mWeapon);
            }
        }
        private boolean hitTarget() {
            final Rect faceRect = mFaceTracker.getFaceRect();
            if (faceRect != null) {
                if (mAimRect.centerX() >= faceRect.left && mAimRect.centerX() <= faceRect.right &&
                        mAimRect.centerY() >= faceRect.top && mAimRect.centerY() <= faceRect.bottom) {
                    return true;
                }
            }

            return false;
        }
    };

    private void requestPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERM_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != CAMERA_PERM_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        } else if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createAndAssignCameraSource();

        } else {
            // TODO: upozornenie na zamietnute prava na kamere
        }
    }

    private void createAndAssignCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setClassificationType(FaceDetector.NO_CLASSIFICATIONS)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .setProminentFaceOnly(true)
                .setTrackingEnabled(false)
                .setMinFaceSize(0.10f)
                .build();

        detector.setProcessor(new LargestFaceFocusingProcessor.Builder(detector, mFaceTracker).build());

        if (!detector.isOperational()) {
            // TODO: kniznica nebola stiahnuta
        }

       mCameraSource = new CameraSource.Builder(getApplicationContext(), detector)
                .setFacing(mCameraFacing)
                .setAutoFocusEnabled(true)
                .setRequestedFps(30f)
                .setRequestedPreviewSize(640, 480)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        mKillsTextView.setText(String.valueOf(mScore.countKills()));

        if (mCameraSource != null) {
            try {
                mCameraPreview.start(mCameraSource, mCameraEffectsOverlay);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mAimRect = new Rect();
        mAimRect.left = mAimImageView.getLeft();
        mAimRect.top = mAimImageView.getTop();
        mAimRect.right = mAimImageView.getRight();
        mAimRect.bottom = mAimImageView.getBottom();
    }
}
