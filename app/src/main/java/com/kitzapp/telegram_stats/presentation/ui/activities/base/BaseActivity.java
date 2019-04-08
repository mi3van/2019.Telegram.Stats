package com.kitzapp.telegram_stats.presentation.ui.activities.base;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.Application.AppManagers.ThemeManager;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.simple.CustomActionBarTypeface;

import java.lang.reflect.Field;

/**
 * Created by Ivan Kuzmin on 2019-03-21.
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class BaseActivity extends Activity implements BaseView {

    private int _oldToolbarColor;
    private ActionBar _actionBar = null;
    private Window _window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initVariables();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());

        _oldToolbarColor = getCurrentColor();
        _actionBar = getActionBar();
        _window = getWindow();

        if (getApplicationContext() instanceof AndroidApp) {
            ((AndroidApp) getApplicationContext()).setCurrentActivity(this);
        }

        this.initViews();
        this.initToolbar();
        this.initBackgrStatusBar();
    }

    protected abstract void initVariables();

    protected abstract int getLayoutID();

    protected abstract void initViews();

    private void initToolbar() {
        if (_actionBar != null) {
            _actionBar.setDisplayShowTitleEnabled(true);

            View v = _window.getDecorView();
            int actionBarId = getResources().getIdentifier("action_bar", "id", "android");
            ViewGroup actionBarView = v.findViewById(actionBarId);
            try {
                Field f = actionBarView.getClass().getSuperclass().getDeclaredField("mContentHeight");
                f.setAccessible(true);
                f.set(actionBarView, ThemeManager.CELL_HEIGHT_56DP_IN_PX);
            } catch (NoSuchFieldException e) {

            } catch (IllegalAccessException e) {

            }

            this.changeToolbarText(_actionBar);
            this.setToolbarColor(_oldToolbarColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                _actionBar.setElevation(8);
            }
        }
    }

    private void initBackgrStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setStatusBarColor(_oldToolbarColor);
            int blackColor = 0xff111111;
            _window.setNavigationBarColor(blackColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_button, menu);
        return true;
    }

    protected void updateActionBarBackgr() {
        int newColor = getCurrentColor();
        if (_oldToolbarColor != newColor) {
            ValueAnimator valueAnimator = AndroidUtilites.getArgbAnimator(
                    _oldToolbarColor,
                    newColor,
                    animation -> {
                        int animatedColor = (int) animation.getAnimatedValue();
                        this.setToolbarColor(animatedColor);
                        this.setStatusBarColor(animatedColor);
                    });
            valueAnimator.start();
            _oldToolbarColor = newColor;
        }
    }

    private void changeToolbarText(ActionBar actionBar) {
        TextPaint toolbarTextPaint = ThemeManager.toolbarTextPaint;

        String toolbarTitle = getResources().getString(R.string.toolbar_title);

        SpannableString actionBarText = new SpannableString(toolbarTitle);
        actionBarText.setSpan(new CustomActionBarTypeface(toolbarTextPaint.getTypeface()), 0, actionBarText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBarText.setSpan(new ForegroundColorSpan(toolbarTextPaint.getColor()), 0, actionBarText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBarText.setSpan(new RelativeSizeSpan(1f), 0,actionBarText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size

        actionBar.setTitle(actionBarText);
    }

    private void setToolbarColor(int color) {
        if (_actionBar != null) {
            _actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int darkerColor;
            if (color > 0xff181818) {
                darkerColor = color - 0x00181818;
            } else {
                darkerColor = 0xff000000;
            }
            _window.setStatusBarColor(darkerColor);
        }
    }

    private int getCurrentColor() {
        return ThemeManager.getColor(ThemeManager.key_toolbarBackColor);
    }

    protected void onDestroy() {
        this.clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        if (getApplicationContext() instanceof AndroidApp) {
            ((AndroidApp) getApplicationContext()).setCurrentActivity(null);
        }
    }
}
