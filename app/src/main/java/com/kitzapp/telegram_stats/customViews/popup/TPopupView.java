package com.kitzapp.telegram_stats.customViews.popup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.customViews.simple.TColorfulLinLayout;

/**
 * Created by Ivan Kuzmin on 2019-04-11;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class TPopupView extends FrameLayout {
    public TPopupView(Context context) {
        super(context);
        this.init();
    }

    public TPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public TPopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void init() {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            return;
        }

        View popupView = inflater.inflate(R.layout.popup_window, null);

    }
}
