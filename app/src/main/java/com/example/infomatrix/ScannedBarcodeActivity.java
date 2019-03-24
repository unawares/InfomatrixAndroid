package com.example.infomatrix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.infomatrix.models.Food;
import com.example.infomatrix.models.Service;
import com.example.infomatrix.models.User;
import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.models.UserCode;
import com.example.infomatrix.design.OverlayQRScannerImageView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannedBarcodeActivity extends AppCompatActivity {

    private final int CAMERA_WIDTH = 1080;
    private final int CAMERA_HEIGHT = 1920;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private SurfaceView cameraSurfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private OverlayQRScannerImageView overlayQRScannerImageView;
    private String serviceName;
    private ImageView cancelButton;
    private BaseServiceFragment baseServiceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);
        Intent intent = getIntent();
        serviceName = intent.getStringExtra("service");
        init();
    }

    private void init() {
        cameraSurfaceView = findViewById(R.id.camera_surface_view);
        overlayQRScannerImageView = findViewById(R.id.overlay_focus_image_view);
        cancelButton = findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    private void startDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(CAMERA_HEIGHT, CAMERA_WIDTH)
                .setAutoFocusEnabled(true)
                .build();

        cameraSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(holder);
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this,
                                new String[]{ Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopDetectorsAndSources();
            }

        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final RectF focus = overlayQRScannerImageView.getBoundingBox();
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                for (int i = 0; i < barcodes.size(); i++) {
                    Barcode barcode = barcodes.valueAt(i);
                    Rect surfaceRect = cameraSurfaceView.getHolder().getSurfaceFrame();
                    Rect rect = barcode.getBoundingBox();
                    rect.top = (int) (surfaceRect.top + (surfaceRect.bottom - surfaceRect.top) / (float) CAMERA_HEIGHT * rect.top);
                    rect.bottom = (int) (surfaceRect.top + (surfaceRect.bottom - surfaceRect.top) / (float) CAMERA_HEIGHT * rect.bottom);
                    rect.left = (int) (surfaceRect.left + (surfaceRect.right - surfaceRect.left) / (float) CAMERA_WIDTH * rect.left);
                    rect.right = (int) (surfaceRect.left + (surfaceRect.right - surfaceRect.left) / (float) CAMERA_WIDTH * rect.right);
                    if (rect.top > focus.top
                            && rect.bottom < focus.bottom
                            && rect.left > focus.left
                            && rect.right < focus.right ) {
                        float c =
                                ((rect.bottom - rect.top) / (focus.bottom - focus.top)
                                        + (rect.right - rect.left) / (focus.right - focus.left)) / 2f;
                        if (c > 0.1) {
                            stopDetectorsAndSources();
                            found(barcode.displayValue);
                            break;
                        }
                    }
                }
            }

        });
    }

    private void stopDetectorsAndSources() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (cameraSource != null) {
                    cameraSource.release();
                }
                cameraSource = null;
            }
        });
    }

    private void found(final String code) {
        NetworkService.getInstance()
                .getTokenApi()
                .getUserCode(code)
                .enqueue(new Callback<UserCode>() {

                    @Override
                    public void onResponse(Call<UserCode> call, Response<UserCode> response) {
                        if (response.isSuccessful()) {
                            UserCode userCode = response.body();
                            if (userCode.isActive()) {
                                inflateFragmentWithUserCode(userCode);
                            } else {
                                Toast.makeText(getApplicationContext(), "User code is not active", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid QR code", Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserCode> call, Throwable t) {
                        t.printStackTrace();
                        recreate();
                    }

                });
    }

    private void inflateFragmentWithUserCode(UserCode userCode) {
        switch (serviceName) {
            case "FOOD":
                foodService(userCode);
                break;
            case "TO_CAMP":
                transportationService(userCode, "to");
                break;
            case "FROM_CAMP":
                transportationService(userCode, "from");
                break;
        }
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if  (baseServiceFragment != null) {
            baseServiceFragment.setOnServiceActionsListener(new BaseServiceFragment.OnServiceActionsListener() {

                @Override
                public void onServiceCommit() {
                    finish();
                }

                @Override
                public void onServiceCancel() {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                            .remove(baseServiceFragment)
                            .runOnCommit(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            recreate();
                                        }
                                    }, 200);
                                }
                            })
                            .commit();
                }

            });
            fragmentTransaction.replace(R.id.fragment_frame_layout, baseServiceFragment);
        }
        fragmentTransaction.commit();

    }

    private void foodService(UserCode userCode) {
        Intent intent = getIntent();
        Food food = intent.getParcelableExtra("food");
        baseServiceFragment = new FoodServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user_code", userCode);
        bundle.putParcelable("food", food);
        baseServiceFragment.setArguments(bundle);
    }

    private void transportationService(UserCode userCode, String transportation) {
        baseServiceFragment = new TransportationServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user_code", userCode);
        bundle.putString("transportation", transportation);
        baseServiceFragment.setArguments(bundle);
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDetectorsAndSources();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
