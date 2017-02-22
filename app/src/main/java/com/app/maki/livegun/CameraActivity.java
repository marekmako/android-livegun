package com.app.maki.livegun;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private CameraSource cameraSource;

    private CameraPreview cameraPreview;
    private CameraEffectsOverlay cameraEffectsOverlay;

    private static final int CAMERA_PERM_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraPreview = (CameraPreview) findViewById(R.id.sv_camera_preview);
        cameraEffectsOverlay = (CameraEffectsOverlay) findViewById(R.id.v_camera_effects_overlay);

        // check camera permission
        int cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            createAndAssignCameraSource();

        } else {
            requestPermission();
        }
    }

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

        EffectsFaceTracker tracker = new EffectsFaceTracker(cameraEffectsOverlay);
        detector.setProcessor(new LargestFaceFocusingProcessor.Builder(detector, tracker).build());

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
}
