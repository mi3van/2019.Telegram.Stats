package com.kitzapp.telegram_stats.customViews.popup;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.TViewObserver;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.TTextView;

import java.util.Observable;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TCellDescriptionTexts extends LinearLayout implements TViewObserver {
    private TTextView _titleTV;
    private TTextView _descriptionTV;

    private int _oldTitleColor;

    public TCellDescriptionTexts(Context context) {
        super(context);
    }

    public TCellDescriptionTexts(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TCellDescriptionTexts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TCellDescriptionTexts(Context context, String title, String description,
                                 int textColor,
                                 LayoutParams layoutParamsText,
                                 LayoutParams layoutParamsDescription) {
        super(context);
        this.init();
        this.loadData(title, description, textColor, layoutParamsText, layoutParamsDescription);
    }

    public void init() {
        this.setOrientation(HORIZONTAL);

        _titleTV = new TTextView(getContext());
        _titleTV.setTypeface(ThemeManager.rRegularTypeface);
        _titleTV.setTextSizeDP(ThemeManager.TEXT_SMALL_SIZE_DP);
        _titleTV.setSingleLine();
        _titleTV.setLines(1);
        _titleTV.setEllipsize(TextUtils.TruncateAt.END);

        _oldTitleColor = getStandartTitleColor();
        _titleTV.setTextColor(_oldTitleColor);

        addView(_titleTV);

        _descriptionTV = new TTextView(getContext());
        _descriptionTV.setTypeface(ThemeManager.rBoldTypeface);
        _descriptionTV.setTextSizeDP(ThemeManager.TEXT_SMALL_SIZE_DP);

        addView(_descriptionTV);
    }

    private void loadData(String titleText, String descriptionText, int textColor,
                          LayoutParams layoutParamsText,
                          LayoutParams layoutParamsDescription) {

        _titleTV.setLayoutParams(layoutParamsText);
        _titleTV.setText(titleText);

        _descriptionTV.setLayoutParams(layoutParamsDescription);
        _descriptionTV.setText(descriptionText);
        _descriptionTV.setTextColor(textColor);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.setMargins(0, ThemeManager.MARGIN_4DP_IN_PX, 0, 0);

        this.addObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.deleteObserver();
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((byte) arg == ObserverManager.KEY_OBSERVER_THEME_UPDATED) {
            int newTitleColor = this.getStandartTitleColor();

            // TITLE CHANGE COLOR
            if (newTitleColor != _oldTitleColor) {
                ValueAnimator textRGBAnim = AndroidUtilites.getArgbAnimator(
                        _oldTitleColor,
                        newTitleColor,
                        animation -> _titleTV.setTextColor((int) animation.getAnimatedValue()));
                textRGBAnim.start();
                _oldTitleColor = newTitleColor;
            }
        }
    }

    private int getStandartTitleColor() {
        int color = ThemeManager.getColor(ThemeManager.key_blackWhiteTextColor);
        return color;
    }

    @Override
    public void addObserver() {
        AndroidApp.observerManager.addObserver(this);
    }

    @Override
    public void deleteObserver() {
        AndroidApp.observerManager.deleteObserver(this);
    }
}
