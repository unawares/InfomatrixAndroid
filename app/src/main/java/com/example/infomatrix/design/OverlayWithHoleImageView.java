package com.example.infomatrix.design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

public class OverlayWithHoleImageView extends OverlayQRScannerImageView {

    private Paint paint;
    private int alpha;

    public OverlayWithHoleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        super.init();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        alpha = 128;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(alpha);
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(
                getBoundingBox(),
                radius, radius, paint);
    }

}
