package com.notbytes.barcode_reader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

public class BarcodeReaderActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {

    private static BarcodeReaderActivity instance;
    private static FragmentController fragmentController;

    public static BarcodeReaderActivity getInstance() {
        if (instance == null)
            throw new RuntimeException("Reference to SharedDataObject was null");
        return instance;
    }

    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_ERROR = -1;

    private static final String KEY_AUTO_FOCUS = "key_auto_focus";
    private static final String KEY_USE_FLASH = "key_use_flash";

    public static String KEY_CAPTURED_BARCODE = "key_captured_barcode";
    public static String KEY_CAPTURED_RAW_BARCODE = "key_captured_raw_barcode";
    private boolean autoFocus = false;
    private boolean useFlash = false;
    private BarcodeReaderFragment mBarcodeReaderFragment;
    private BaseFragment mServiceFragment;

    private ImageView cancelImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_reader);
        instance = this;
        final Intent intent = getIntent();
        if (intent != null) {
            autoFocus = intent.getBooleanExtra(KEY_AUTO_FOCUS, false);
            useFlash = intent.getBooleanExtra(KEY_USE_FLASH, false);
        }
        mBarcodeReaderFragment = attachBarcodeReaderFragment();
        mServiceFragment = attachServiceFragment();
        cancelImageView = findViewById(R.id.cancel_image_view);

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentController = null;
    }

    private BarcodeReaderFragment attachBarcodeReaderFragment() {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        BarcodeReaderFragment fragment = BarcodeReaderFragment.newInstance(autoFocus, useFlash);
        fragment.setListener(this);
        fragmentTransaction.replace(R.id.fm_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }

    private BaseFragment attachServiceFragment() {
        if (fragmentController != null) {
            BaseFragment serviceFragment = fragmentController.loadFragment();
            final FragmentManager supportFragmentManager = getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.service_container, serviceFragment);
            fragmentTransaction.commitAllowingStateLoss();
            return serviceFragment;
        }
        return null;
    }

    public static Intent getLaunchIntent(Context context, boolean autoFocus, boolean useFlash) {
        Intent intent = new Intent(context, BarcodeReaderActivity.class);
        intent.putExtra(KEY_AUTO_FOCUS, autoFocus);
        intent.putExtra(KEY_USE_FLASH, useFlash);
        return intent;
    }

    public static void setFragmentController(FragmentController<? extends BaseFragment> controller) {
        fragmentController = controller;
    }

    @Override
    public void onScanned(Barcode barcode) {
        if (mServiceFragment != null) {
            mServiceFragment.onScanned(barcode);
        }
    }

    @Override
    public void onTouchBarcode(Barcode barcode) {

    }

    @Override
    public void onUpdated(Barcode barcode) {

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        setResult(RESULT_ERROR);
        finish();
    }

    @Override
    public void onCameraPermissionDenied() {
        setResult(RESULT_ERROR);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static abstract class BaseFragment extends Fragment {

        abstract protected void onScanned(Barcode barcode);

        protected void resume() {
            BarcodeReaderActivity.getInstance().mBarcodeReaderFragment.resumeScanning();
        }

        protected void pause() {
            BarcodeReaderActivity.getInstance().mBarcodeReaderFragment.pauseScanning();
        }

        protected void playSuccessBeep() {
            BarcodeReaderActivity.getInstance().mBarcodeReaderFragment.playSuccessBeep();
        }

        protected void playErrorBeep() {
            BarcodeReaderActivity.getInstance().mBarcodeReaderFragment.playErrorBeep();
        }

        protected void finish() {
            BarcodeReaderActivity.getInstance().finish();
        }

    }

    public static interface FragmentController<T extends BaseFragment> {

        T loadFragment();

    }

}
