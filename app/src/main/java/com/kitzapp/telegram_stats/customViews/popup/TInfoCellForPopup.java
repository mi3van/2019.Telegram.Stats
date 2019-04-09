package com.kitzapp.telegram_stats.customViews.popup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TTextView;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TInfoCellForPopup extends LinearLayout {
    private TTextView _title;
    private TTextView _description;
    private String _titleText;
    private String _descriptionText;
    private int _textColor;

    public TInfoCellForPopup(Context context) {
        super(context);
    }

    public TInfoCellForPopup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TInfoCellForPopup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TInfoCellForPopup(Context context, String title, String description, int textColor) {
        super(context);
        this.init(title, description, textColor);
    }

    private void init(String titleText, String descriptionText, int textColor) {
        this.setOrientation(VERTICAL);

        _titleText = titleText;
        _descriptionText = descriptionText;
        _textColor = textColor;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.setMargins(0, 0, ThemeManager.CHART_CELL_RIGHTLEFT_MARGIN_PX >> 1, 0);

        _title = new TTextView(getContext());
        _title.setTypeface(ThemeManager.chartTitleTextPaint.getTypeface());
        _title.setText(_titleText);
        _title.setTextSizeDP(ThemeManager.chartTitleTextPaint.getTextSize());
        _title.setTextColor(_textColor);
        addView(_title);

        _description = new TTextView(getContext());
        _description.setTypeface(ThemeManager.chartDescrTextPaint.getTypeface());
        _description.setText(_descriptionText);
        _description.setTextColor(_textColor);
        addView(_description);

        LinearLayout.LayoutParams latyoutParamsTitle = (LayoutParams) _title.getLayoutParams();
        latyoutParamsTitle.setMargins(0, 0, 0, -4);
    }
}
