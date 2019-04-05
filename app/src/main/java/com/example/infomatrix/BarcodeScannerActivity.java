package com.example.infomatrix;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.List;

public class BarcodeScannerActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {

    private static final int BARCODE_READER_ACTIVITY_REQUEST = 201;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        Intent launchIntent = com.notbytes.barcode_reader.BarcodeReaderActivity.getLaunchIntent(this, true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }

    @Override
    public void onTouchBarcode(Barcode barcode) {

    }

    @Override
    public void onUpdated(Barcode barcode) {
        System.out.println(barcode.displayValue);
    }

    @Override
    public void onScanned(Barcode barcode) {
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }
}
