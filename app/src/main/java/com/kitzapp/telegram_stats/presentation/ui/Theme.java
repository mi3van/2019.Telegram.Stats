package com.kitzapp.telegram_stats.presentation.ui;

import com.kitzapp.telegram_stats.AndroidApplication;
import com.kitzapp.telegram_stats.presentation.ui.components.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.RFontTextPaint;

import java.util.HashMap;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class Theme {
    private static int totalInit = 1;
    public final static int LIGHT = totalInit++;
    public final static int DARK = totalInit++;

    private static int currentTheme;
    private static HashMap<String, Integer> currentColors;
    private static HashMap<String, Integer> darkThemeColors;
    private static HashMap<String, Integer> lightThemeColors;

    public static final String key_totalBackColor = "totalBackColor";
    public static final String key_actionBarBackColor = "actionBarBackColor";
    public static final String key_actionBarTextColor = "actionBarTextColor";
    private static final String key_cellTitleTextColor = "cellTitleTextColor";
    public static final String key_cellBackColor = "cellBackColor";
    public static final String key_cellSubBackColor = "cellSubBackColor";
    public static final String key_chartDescrTextColor = "chartDescrTextColor";
    private static final String key_simpleTextColor = "simpleTextColor";

    private static final int colorLightBackup;
    private static final int colorDarkBackup;

    public static RFontTextPaint actionBarTextPaint;
    public static RFontTextPaint simpleTextPaint;
    public static RFontTextPaint cellTitleTextPaint;
    public static RFontTextPaint chartDescrTextPaint;

    static {
        currentColors = new HashMap<>();
        darkThemeColors = new HashMap<>();
        lightThemeColors = new HashMap<>();

        actionBarTextPaint = new RFontTextPaint();
        simpleTextPaint = new RFontTextPaint();
        cellTitleTextPaint = new RFontTextPaint();
        chartDescrTextPaint = new RFontTextPaint();

        actionBarTextPaint.setTextSize(AndroidUtilites.dp(16));
        simpleTextPaint.setTextSize(AndroidUtilites.dp(12));
        cellTitleTextPaint.setTextSize(AndroidUtilites.dp(12));
        chartDescrTextPaint.setTextSize(AndroidUtilites.dp(8));

        colorLightBackup = 0x00000000;
        colorDarkBackup = 0xffffffff;

        initThemesHashes();

        applyTheme(AndroidApplication.mainRepository.getCurrentTheme());

    }

    public static void changeThemeAndSave() {
        int newTheme = currentTheme == LIGHT ? DARK : LIGHT;
        applyTheme(newTheme, true);
    }

    public static void applyTheme(int theme) {
        applyTheme(theme, false);
    }

    public static void applyTheme(int theme, boolean save) {
        if (currentTheme != theme) {
            currentTheme = theme;

            if (save) {
                AndroidApplication.mainRepository.saveNewTheme(theme);
            }

            if (currentTheme == DARK) {
                currentColors = darkThemeColors;
            } else {
                currentColors = lightThemeColors;
            }

            updateFontsColors();
        }
    }

    private static void updateFontsColors() {
        simpleTextPaint.setColor(getColor(key_simpleTextColor));
        cellTitleTextPaint.setColor(getColor(key_cellTitleTextColor));
        chartDescrTextPaint.setColor(getColor(key_chartDescrTextColor));
    }

    public static int getColor(String key) {
        Integer color = currentColors.get(key);
        if (color == null) {
            color = currentTheme == LIGHT ? colorLightBackup : colorDarkBackup;
        }
        return color;
    }

    public static void initThemesHashes() {
        darkThemeColors.put(key_totalBackColor, 0xff151e27);
        darkThemeColors.put(key_actionBarBackColor, 0xff212d3b);
        darkThemeColors.put(key_actionBarTextColor, 0xffffffff);
        darkThemeColors.put(key_cellTitleTextColor, 0xff77bdf2);
        darkThemeColors.put(key_cellBackColor , 0xff1d2733);
        darkThemeColors.put(key_cellSubBackColor , 0xff19232e);
        darkThemeColors.put(key_chartDescrTextColor, 0xff4d606f);
        darkThemeColors.put(key_simpleTextColor , 0xffffffff);

        lightThemeColors.put(key_totalBackColor, 0xfff0f0f0);
        lightThemeColors.put(key_actionBarBackColor, 0xff517da2);
        lightThemeColors.put(key_actionBarTextColor, 0xffffffff);
        lightThemeColors.put(key_cellTitleTextColor, 0xff77bdf2);
        lightThemeColors.put(key_cellBackColor , 0xffffffff);
        lightThemeColors.put(key_cellSubBackColor , 0xfff5f8f9);
        lightThemeColors.put(key_chartDescrTextColor, 0xff88939a);
        lightThemeColors.put(key_simpleTextColor , 0xff000000);
    }
}
