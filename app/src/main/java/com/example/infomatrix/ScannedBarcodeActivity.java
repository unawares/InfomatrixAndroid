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
import android.support.v4.app.Fragment;
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
import com.example.infomatrix.models.User;
import com.example.infomatrix.network.NetworkService;
import com.example.infomatrix.models.UserCode;
import com.example.infomatrix.design.OverlayQRScannerImageView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannedBarcodeActivity extends AppCompatActivity {

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
                .setRequestedPreviewSize(1920, 1080)
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
                    Rect rect = barcode.getBoundingBox();
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
                            inflateFragmentWithUser(userCode.getUser());
                        } else {
                            recreate();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserCode> call, Throwable t) {
                        recreate();
                    }

                });
    }

    private void inflateFragmentWithUser(User user) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (serviceName) {
            case "FOOD":
                Intent intent = getIntent();
                Food food = intent.getParcelableExtra("food");
                baseServiceFragment = new FoodServiceFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                bundle.putParcelable("food", food);
                baseServiceFragment.setArguments(bundle);
                baseServiceFragment.setOnServiceActionsListener(new BaseServiceFragment.OnServiceActionsListener() {

                    @Override
                    public void onServiceCommit() {
                        Toast.makeText(getApplicationContext(), "Commit", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onServiceCancel() {
                        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        recreate();
                    }

                });
                break;
        }
        if  (baseServiceFragment != null) {
            baseServiceFragment.setUser(user);
            fragmentTransaction.replace(R.id.fragment_frame_layout, baseServiceFragment);
        }
        fragmentTransaction.runOnCommit(new Runnable() {
            @Override
            public void run() {
                baseServiceFragment.serve();
            }
        });
        fragmentTransaction.commit();

    }

    @Override
    public void recreate() {
        super.recreate();
        getSupportFragmentManager().beginTransaction().remove(baseServiceFragment).commit();
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
