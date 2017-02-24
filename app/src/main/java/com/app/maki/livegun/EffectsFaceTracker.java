package com.app.maki.livegun;

import android.graphics.Rect;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class EffectsFaceTracker extends Tracker<Face> {

    private CameraEffectsOverlay overlay;
    private FaceGraphics graphics;

    public EffectsFaceTracker(CameraEffectsOverlay overlay) {
        this.overlay = overlay;
        graphics = new FaceGraphics(overlay);
    }

    @Override
    public void onNewItem(int i, Face face) {
        super.onNewItem(i, face);
    }

    @Override
    public void onUpdate(Detector.Detections<Face> detections, Face face) {
        super.onUpdate(detections, face);
        overlay.add(graphics);
        graphics.updateFace(face);
    }

    @Override
    public void onMissing(Detector.Detections<Face> detections) {
        super.onMissing(detections);
        overlay.remove(graphics);
    }

    @Override
    public void onDone() {
        super.onDone();
        overlay.remove(graphics);
    }

    public Rect getFaceRect() {
        return graphics.getFaceRect();
    }
}
