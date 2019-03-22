package com.example.infomatrix.design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

import com.example.infomatrix.R;

public class OverlayFocusImageView extends OverlayQRScannerImageView {

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
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        paint.setColor(getResources().getColor(R.color.colorPrimary));
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
    }

}
