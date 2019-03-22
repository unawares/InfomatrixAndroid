package com.example.infomatrix.design;

import android.content.Context;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

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
        return new RectF(
                getWidth() / 2f - width / 2f,
                getHeight() / 2f - height / 2f,
                getWidth() / 2f + width / 2f,
                getHeight() / 2f + height / 2f
        );
    }

}
