package com.notbytes.barcode_reader.design;

import android.content.Context;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.notbytes.barcode_reader.camera.GraphicOverlay;

public abstract class OverlayQRScannerImageView extends AppCompatImageView {

    protected float radius;
    protected float height;
    protected float width;

    public OverlayQRScannerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.radius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16,
                getResources().getDisplayMetrics()
        );
        this.height = this.width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 256,
                getResources().getDisplayMetrics()
        );
        postInvalidate();
    }

    public RectF getBoundingBox() {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        return new RectF(
                centerX - width / 2,
                centerY - height / 2,
                centerX + width / 2,
                centerY + height / 2
        );
    }

}
