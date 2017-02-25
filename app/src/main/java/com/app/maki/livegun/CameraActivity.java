package com.app.maki.livegun;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private CameraEffectsOverlay cameraEffectsOverlay;

    private ImageView aimImageView;
    private Rect aimRect;

    private AnimationDrawable weaponAnimation;

    private EffectsFaceTracker faceTracker;
    private FaceGraphics faceGraphics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraPreview = (CameraPreview) findViewById(R.id.sv_camera_preview);
        cameraEffectsOverlay = (CameraEffectsOverlay) findViewById(R.id.v_camera_effects_overlay);
        aimImageView = (ImageView) findViewById(R.id.iv_aim);

        ImageView weaponImageView = (ImageView) findViewById(R.id.iv_weapon);
        weaponAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.rifle_animation);
        weaponImageView.setImageDrawable(weaponAnimation);
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
            animate();

            if (hitTarget()) {
                Log.d("cam", "hit");
            }
        }

        private void animate() {
            int duration = 10;
            for (int i = 0; i < weaponAnimation.getNumberOfFrames(); i++) {
                duration += weaponAnimation.getDuration(i);
            }

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    weaponAnimation.stop();
                    weaponAnimation.selectDrawable(0);
                }
            };
            handler.postDelayed(runnable, duration);
            weaponAnimation.start();
        }

        private boolean hitTarget() {
            Rect faceRect = faceTracker.getFaceRect();
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
                .setMinFaceSize(0.15f)
                .build();

        detector.setProcessor(new LargestFaceFocusingProcessor.Builder(detector, faceTracker).build());

        if (!detector.isOperational()) {
            // TODO: kniznica nebola stiahnuta
        }

       cameraSource = new CameraSource.Builder(getApplicationContext(), detector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedFps(60f)
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
