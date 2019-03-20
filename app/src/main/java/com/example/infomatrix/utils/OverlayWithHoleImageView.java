package com.example.infomatrix.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.TypedValue;
import android.view.View;

import com.example.infomatrix.R;

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
