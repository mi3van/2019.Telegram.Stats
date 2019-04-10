package com.kitzapp.telegram_stats;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.PreferenceManager;
import com.kitzapp.telegram_stats.core.appManagers.ThemeManager;

public class AndroidApp extends Application {
    public static volatile Resources resources;
    public static volatile PreferenceManager mainRepository;
    public static volatile ObserverManager observerManager;
    public static volatile PopupWindow popupWindow;

    private volatile Activity _currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        Context context = getBaseContext();

        observerManager = new ObserverManager();
        mainRepository = new PreferenceManager(context);
        ThemeManager.initTextFonts(context);

        this.configurePopupWindow(context);
    }

    public Activity getCurrentActivity() {
        return _currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this._currentActivity = currentActivity;
    }

    private void configurePopupWindow(Context context) {
//        SETUP POPUP VIEW
        popupWindow = new PopupWindow(context);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View popupView = layoutInflater.inflate(R.layout.popup_window, null);
        popupWindow.setContentView(popupView);
        popupWindow.setAnimationStyle(R.style.popup_window_animation);
    }
}
