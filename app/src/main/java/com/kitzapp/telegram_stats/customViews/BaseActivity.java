package com.kitzapp.telegram_stats.customViews;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.*;
import android.widget.Toast;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;
import com.kitzapp.telegram_stats.customViews.simple.CustomActionBarTypeface;

import java.lang.reflect.Field;

/**
 * Created by Ivan Kuzmin on 2019-03-21.
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class BaseActivity extends Activity {

    private int _oldToolbarBackgrColor;
    private int _oldToolbarTitleColor;
    private int _oldToolbarIconColor;

    private ActionBar _actionBar = null;
    private Menu _menu;
    private Window _window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.initVariables();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());

        _oldToolbarBackgrColor = this.getCurrentToolbarBackgrColor();
        _oldToolbarTitleColor = this.getCurrentToolbarTitleColor();
        _oldToolbarIconColor = this.getCurrentToolbarIconColor();

        _actionBar = getActionBar();
        _window = getWindow();

        if (getApplicationContext() instanceof AndroidApp) {
            ((AndroidApp) getApplicationContext()).setCurrentActivity(this);
        }

        this.initViews();
        this.initToolbar();
        this.initStatusAndNavigationBarColor();
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.initToolbarText(_actionBar);

            this.setToolbarBackgrColor(_oldToolbarBackgrColor);
            this.setToolbarTitleColor(_oldToolbarTitleColor);
            this.setToolbarIconColor(_oldToolbarIconColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                _actionBar.setElevation(8);
            }
        }
    }

    private void initStatusAndNavigationBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setStatusBarColor(_oldToolbarBackgrColor);
            int blackColor = 0xff111111;
            _window.setNavigationBarColor(blackColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_button, menu);
        _menu = menu;
        this.setToolbarIconColor(_oldToolbarIconColor);
        return true;
    }

    private void initToolbarText(ActionBar actionBar) {
        String toolbarTitle = getResources().getString(R.string.toolbar_title);

        SpannableString spanString = new SpannableString(toolbarTitle);
        spanString.setSpan(new CustomActionBarTypeface(ThemeManager.toolbarTextPaint.getTypeface()), 0, spanString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new RelativeSizeSpan(1f), 0,spanString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size

        actionBar.setTitle(spanString);
    }

    private void setToolbarBackgrColor(int color) {
        if (_actionBar != null) {
            _actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    private void setToolbarTitleColor(int titleColor) {
        CharSequence title = _actionBar.getTitle();
        SpannableString spanString = new SpannableString(title);
        spanString.setSpan(new ForegroundColorSpan(titleColor), 0, spanString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        _actionBar.setTitle(spanString);
    }

    private void setToolbarIconColor(int iconColor) {
        if (_menu != null && _menu.size() > 0) {
            MenuItem menuItem = _menu.getItem(0);
            Drawable icon = menuItem.getIcon();
            AndroidUtilites.setDrawFilterATOP(icon, iconColor);
        }
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int newColor = color;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            if (color > 0xff505050) {
                newColor = color - 0x00505050;
            } else {
                newColor = 0xff000000;
            }
            _window.setStatusBarColor(newColor);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = _window.getDecorView();
            if (color > 0xff7a7a7a) {
                if (decorView.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                if (decorView.getSystemUiVisibility() != 0) {
                    decorView.setSystemUiVisibility(0);
                }
            }
        }
    }

    protected void updateActionBarBackgr() {
        int newBackgrColor = getCurrentToolbarBackgrColor();
        if (_oldToolbarBackgrColor != newBackgrColor) {
//            BACKGROUND ANIM COLOR
            ValueAnimator backgrAnimator = AndroidUtilites.getArgbAnimator(
                    _oldToolbarBackgrColor,
                    newBackgrColor,
                    animation -> {
                        int animatedColor = (int) animation.getAnimatedValue();
                        this.setToolbarBackgrColor(animatedColor);
                        this.setStatusBarColor(animatedColor);
                    });
            backgrAnimator.start();
            _oldToolbarBackgrColor = newBackgrColor;

//            TITLE ANIM COLOR
            int newTitleColor = this.getCurrentToolbarTitleColor();
            ValueAnimator titleAnimator = AndroidUtilites.getArgbAnimator(
                    _oldToolbarTitleColor,
                    newTitleColor,
                    animation -> {
                        int animatedColor = (int) animation.getAnimatedValue();
                        this.setToolbarTitleColor(animatedColor);
                    });
            titleAnimator.start();
            _oldToolbarTitleColor = newTitleColor;

//            ICON ANIM COLOR
            int newIconColor = getCurrentToolbarIconColor();
            ValueAnimator iconAnimator = AndroidUtilites.getArgbAnimator(
                    _oldToolbarIconColor,
                    newIconColor,
                    animation -> {
                        int animatedColor = (int) animation.getAnimatedValue();
                        this.setToolbarIconColor(animatedColor);
                    });
            iconAnimator.start();
            _oldToolbarIconColor = newIconColor;
        }
    }

    private int getCurrentToolbarBackgrColor() {
        return ThemeManager.getColor(ThemeManager.key_toolbarBackColor);
    }

    private int getCurrentToolbarTitleColor() {
        return ThemeManager.toolbarTextPaint.getColor();
    }

    private int getCurrentToolbarIconColor() {
        return ThemeManager.getColor(ThemeManager.key_toolbarIconColor);
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

    public void showProgress() {

    }

    public void hideProgress() {

    }

    public void showMessageToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

}
