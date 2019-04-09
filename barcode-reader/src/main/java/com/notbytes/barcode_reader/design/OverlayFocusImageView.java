package com.notbytes.barcode_reader.design;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.R;
import com.notbytes.barcode_reader.camera.CameraSource;
import com.notbytes.barcode_reader.camera.GraphicOverlay;

public class OverlayFocusImageView extends OverlayQRScannerImageView {

    private final Object mLock = new Object();
    private int mFacing = CameraSource.CAMERA_FACING_BACK;
    private int mPreviewWidth;
    private float mWidthScaleFactor = 1.0f;
    private int mPreviewHeight;
    private float mHeightScaleFactor = 1.0f;
    private Paint paint;
    private float weight = 20;

    public OverlayFocusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        super.init();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (mLock) {
            canvas.restore();
            float centerX = getWidth() / 2f;
            float centerY = getHeight() / 2f;
            paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
            canvas.drawRoundRect(
                    getBoundingBox(),
                    radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawRoundRect(
                    centerX - width / 2 + weight,
                    centerY - height / 2 + weight,
                    centerX + width / 2 - weight,
                    centerY + height / 2 - weight,
                    radius - weight / 2f, radius - weight / 2f, paint);
            canvas.rotate(45, centerX, centerY);
            canvas.drawRoundRect(
                    centerX - 1.2f * width / 2,
                    centerY - 1.2f * height / 2,
                    centerX + 1.2f * width / 2,
                    centerY + 1.2f * height / 2,
                    radius, radius, paint);
            if ((mPreviewWidth != 0) && (mPreviewHeight != 0)) {
                mWidthScaleFactor = (float) getWidth() / (float) mPreviewWidth;
                mHeightScaleFactor = (float) getHeight() / (float) mPreviewHeight;
            }
        }
    }

    public void setCameraInfo(int previewWidth, int previewHeight, int facing) {
        synchronized (mLock) {
            mPreviewWidth = previewWidth;
            mPreviewHeight = previewHeight;
            mFacing = facing;
        }
    }

    public boolean containsRectF(RectF rectF) {
        return getBoundingBox().contains(rectF);
    }

    public int getFacing() {
        return mFacing;
    }

    public void setFacing(int mFacing) {
        this.mFacing = mFacing;
    }

    public float getWidthScaleFactor() {
        return mWidthScaleFactor;
    }

    public void setWidthScaleFactor(float mWidthScaleFactor) {
        this.mWidthScaleFactor = mWidthScaleFactor;
    }

    public float getHeightScaleFactor() {
        return mHeightScaleFactor;
    }

    public void setHeightScaleFactor(float mHeightScaleFactor) {
        this.mHeightScaleFactor = mHeightScaleFactor;
    }
}
