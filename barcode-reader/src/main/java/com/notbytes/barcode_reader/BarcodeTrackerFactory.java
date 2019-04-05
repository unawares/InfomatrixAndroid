package com.notbytes.barcode_reader;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.camera.GraphicOverlay;
import com.notbytes.barcode_reader.design.OverlayFocusImageView;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private OverlayFocusImageView mOverlayFocusImageView;
    private BarcodeGraphicTracker.BarcodeGraphicTrackerListener listener;

    BarcodeTrackerFactory(OverlayFocusImageView overlayFocusImageView, GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay, BarcodeGraphicTracker.BarcodeGraphicTrackerListener listener) {
        mOverlayFocusImageView = overlayFocusImageView;
        mGraphicOverlay = barcodeGraphicOverlay;
        this.listener = listener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        return new BarcodeGraphicTracker(mOverlayFocusImageView, mGraphicOverlay, graphic, listener);
    }

}

