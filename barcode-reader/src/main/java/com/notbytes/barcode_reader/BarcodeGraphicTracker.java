package com.notbytes.barcode_reader;

import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.camera.CameraSource;
import com.notbytes.barcode_reader.camera.GraphicOverlay;
import com.notbytes.barcode_reader.design.OverlayFocusImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generic tracker which is used for tracking or reading a barcode (and can really be used for
 * any type of item).  This is used to receive newly detected items, add a graphical representation
 * to an overlay, update the graphics as the item changes, and remove the graphics when the item
 * goes away.
 */
class BarcodeGraphicTracker extends Tracker<Barcode> {
    private OverlayFocusImageView mOverlayFocusImageView;
    private GraphicOverlay<BarcodeGraphic> mOverlay;
    private BarcodeGraphic mGraphic;
    private BarcodeGraphicTrackerListener listener;
    private Set<Integer> barcodeIdsSet;

    BarcodeGraphicTracker(OverlayFocusImageView overlayFocusImageView, GraphicOverlay<BarcodeGraphic> overlay, BarcodeGraphic graphic, BarcodeGraphicTrackerListener listener) {
        mOverlayFocusImageView = overlayFocusImageView;
        mOverlay = overlay;
        mGraphic = graphic;
        this.listener = listener;
        barcodeIdsSet = new HashSet<>();
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    @Override
    public void onNewItem(int id, Barcode item) {
        mGraphic.setId(id);
        Log.e("XX", "barcode detected: " + item.displayValue + ", listener: " + listener);
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */

    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {
        boolean focusContains = focusContainsBarcode(item);
        boolean setContains = barcodeIdsSet.contains(mGraphic.getId());
        if (focusContains && !setContains) {
            barcodeIdsSet.add(mGraphic.getId());
            mOverlay.add(mGraphic);
            listener.onScanned(item);
        } else if (focusContains) {
            mGraphic.updateItem(item);
            listener.onUpdated(item);
        } else {
            barcodeIdsSet.remove(mGraphic.getId());
            mOverlay.remove(mGraphic);
        }

        List<Barcode> filtered = new ArrayList<>();
        if (detectionResults != null) {
            for (Barcode barcode : asList(detectionResults.getDetectedItems())) {
                if (focusContainsBarcode(barcode)) {
                    filtered.add(barcode);
                }
            }
        }

        if (filtered.size() > 1) {
            Log.e("XX", "Multiple items detected");
            Log.e("XX", "onUpdate: " + filtered.size());

            if (listener != null) {
                listener.onScannedMultiple(filtered);
            }
        }
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    /**
     * Hide the graphic when the corresponding object was not detected.  This can happen for
     * intermediate frames temporarily, for example if the object was momentarily blocked from
     * view.
     */
    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {
        mOverlay.remove(mGraphic);
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
    }

    public boolean focusContainsBarcode(Barcode barcode) {
        if (mOverlayFocusImageView != null) {
            float heightScaleFactor = mOverlay.getHeightScaleFactor();
            float widthScaleFactor = mOverlay.getWidthScaleFactor();
            RectF rectF = new RectF(barcode.getBoundingBox());
            rectF.top = heightScaleFactor * rectF.top;
            rectF.bottom = heightScaleFactor * rectF.bottom;
            if (mOverlayFocusImageView.getFacing() == CameraSource.CAMERA_FACING_FRONT) {
                rectF.left = mOverlayFocusImageView.getWidth() - widthScaleFactor * rectF.left;
                rectF.right = mOverlayFocusImageView.getWidth() - widthScaleFactor * rectF.right;
            } else {
                rectF.left = widthScaleFactor * rectF.left;
                rectF.right = widthScaleFactor * rectF.right;
            }
            rectF.left -= (mOverlay.getWidth() - mOverlayFocusImageView.getWidth()) / 2f;
            rectF.right -= (mOverlay.getWidth() - mOverlayFocusImageView.getWidth()) / 2f;
            rectF.top -= (mOverlay.getHeight() - mOverlayFocusImageView.getHeight()) / 2f;
            rectF.bottom -= (mOverlay.getHeight() - mOverlayFocusImageView.getHeight()) / 2f;
            return mOverlayFocusImageView.containsRectF(rectF);
        }
        return false;
    }

    public interface BarcodeGraphicTrackerListener {
        void onUpdated(Barcode barcode);

        void onScanned(Barcode barcode);

        void onScannedMultiple(List<Barcode> barcodes);

        void onBitmapScanned(SparseArray<Barcode> sparseArray);

        void onScanError(String errorMessage);
    }

}
