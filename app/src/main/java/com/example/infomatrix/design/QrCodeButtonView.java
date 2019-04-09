package com.example.infomatrix.design;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.infomatrix.R;

public class QrCodeButtonView extends RelativeLayout {

    public QrCodeButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.qr_code_button, this);
    }

}
