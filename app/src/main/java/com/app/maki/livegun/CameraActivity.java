package com.app.maki.livegun;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERM_CODE = 2;

    private CameraSource cameraSource;

    private CameraPreview cameraPreview;

    private EffectOverlaySurface cameraEffectsOverlay;

    private ImageView aimImageView;
    private Rect aimRect;

    private EffectsFaceTracker faceTracker;
    private FaceGraphics faceGraphics;

    private Weapon mWeaponPrototype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        WeaponDataParcel weaponParcelPrototype = intent.getParcelableExtra(MainActivity.K_SELECTED_WEAPON);
        mWeaponPrototype = new Weapon(getApplicationContext(), weaponParcelPrototype);

        cameraPreview = (CameraPreview) findViewById(R.id.sv_camera_preview);
        cameraEffectsOverlay = (EffectOverlaySurface) findViewById(R.id.sv_effect_overlay);
        aimImageView = (ImageView) findViewById(R.id.iv_aim);

        ImageView weaponImageView = (ImageView) findViewById(R.id.iv_weapon);
        weaponImageView.setImageDrawable(mWeaponPrototype.getWeaponShotAnimation());
        weaponImageView.setOnClickListener(onShotListener);


        faceGraphics = new FaceGraphics(getApplicationContext(), cameraEffectsOverlay);

        faceTracker = new EffectsFaceTracker(
                cameraEffectsOverlay,
                faceGraphics);

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
            mWeaponPrototype.onShot();

            if (hitTarget()) {
                faceGraphics.onHit();
            }
        }
        private boolean hitTarget() {
            final Rect faceRect = faceTracker.getFaceRect();
            if (faceRect != null) {
                if (aimRect.centerX() >= faceRect.left && aimRect.centerX() <= faceRect.right &&
                        aimRect.centerY() >= faceRect.top && aimRect.centerY() <= faceRect.bottom) {
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

        detector.setProcessor(new LargestFaceFocusingProcessor.Builder(detector, faceTracker).build());

        if (!detector.isOperational()) {
            // TODO: kniznica nebola stiahnuta
        }

       cameraSource = new CameraSource.Builder(getApplicationContext(), detector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedFps(30f)
                .setRequestedPreviewSize(640, 480)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cameraSource != null) {
            try {
                cameraPreview.start(cameraSource, cameraEffectsOverlay);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
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
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        aimRect = new Rect();
        aimRect.left = aimImageView.getLeft();
        aimRect.top = aimImageView.getTop();
        aimRect.right = aimImageView.getRight();
        aimRect.bottom = aimImageView.getBottom();
    }
}
