package com.kitzapp.telegram_stats.Application.AppManagers;

import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.presentation.ui.components.impl.TTextPaint;

import java.util.HashMap;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class ThemeManager {
//    TEXT SIZES DP
    public static final int TOOLBAR_TEXT_SIZE_DP = 22;
    public static final int SIMPLE_TEXT_SIZE_DP = 18;
    public static final int CHART_TITLE_TEXT_SIZE_DP = 16;
    public static final int CHART_SUB_TEXT_SIZE_DP = 8;

//    CHART CELLS DP
    private static final int CHART_CELL_HEIGHT_DP = 56;
    private static final int CHART_CELL_BOTTOM_MARGIN_DP = 28;
    private static final int CHART_CELL_RIGHT_LEFT_MARGIN_DP = 16;
//    CHART CELLS PX
    public static final int CHART_CELL_HEIGHT_PX;
    public static final int CHART_CELL_BOTTOM_MARGIN_PX;
    public static final int CHART_CELL_RIGHTLEFT_MARGIN_PX;

//    CHART VIEWS DP
    private static final int CHART_LINE_IN_PART_WIDTH_DP = 2;
    private static final int CHART_LINE_FULL_WIDTH_DP = 1;
    private static final int CHART_FULL_TOP_BOTTOM_MARGIN_DP = 4;
    private static final int CHART_PART_HEIGHT_DP = 280;
    private static final int CHART_DELIMITER_WIDTH_DP = 1;
    private static final int CHART_RECT_SELECT_WIDTH_DP = 4;

//    CHART VIEWS PX
    public static final int CHART_LINE_IN_PART_WIDTH_PX;
    public static final int CHART_LINE_FULL_WIDTH_PX;
    public static final int CHART_FULL_TOP_BOTTOM_MARGIN_PX;
    public static final int CHART_PART_HEIGHT_PX;

    public static final int CHART_DELIMITER_WIDTH_PX;
    public static final int CHART_RECT_SELECT_WIDTH_PX;

    private static int totalInit = 1;
    public final static int LIGHT = totalInit++;
    public final static int DARK = totalInit++;

    private static int currentTheme;
    private static HashMap<String, Integer> currentColors;
    private static HashMap<String, Integer> darkThemeColors;
    private static HashMap<String, Integer> lightThemeColors;

//    COLORS BASIC VIEWS
    public static final String key_totalBackColor = "totalBackColor";
    public static final String key_toolbarBackColor = "actionBarBackColor";
    private static final String key_toolbarTextColor = "actionBarTextColor";

    //    COLORS CELLS
    private static final String key_cellTitleTextColor = "cellTitleTextColor";
    public static final String key_cellBackColor = "cellBackColor";
    public static final String key_cellSubBackColor = "cellSubBackColor";
    private static final String key_chartDescrTextColor = "chartDescrTextColor";
    public static final String key_rectSelectColor = "rectSelectColor";

    //    COLORS OTHER VIEWS
    private static final String key_simpleTextColor = "simpleTextColor";
    public static final String key_delimiterColor = "delimiterColor";

    private static final int colorLightBackup;
    private static final int colorDarkBackup;

    public static TTextPaint toolbarTextPaint;
    public static TTextPaint simpleTextPaint;
    public static TTextPaint chartTitleTextPaint;
    public static TTextPaint chartDescrTextPaint;

    static {
        currentColors = new HashMap<>();
        darkThemeColors = new HashMap<>();
        lightThemeColors = new HashMap<>();

        toolbarTextPaint = new TTextPaint();
        simpleTextPaint = new TTextPaint();
        chartTitleTextPaint = new TTextPaint();
        chartDescrTextPaint = new TTextPaint();

        colorLightBackup = 0x00000000;
        colorDarkBackup = 0xffffffff;

        initThemesHashes();

        applyTheme(AndroidApp.mainRepository.getCurrentTheme());

        CHART_CELL_HEIGHT_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_CELL_HEIGHT_DP);
        CHART_CELL_BOTTOM_MARGIN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_CELL_BOTTOM_MARGIN_DP);
        CHART_CELL_RIGHTLEFT_MARGIN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_CELL_RIGHT_LEFT_MARGIN_DP);

        CHART_LINE_FULL_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_LINE_FULL_WIDTH_DP);
        CHART_LINE_IN_PART_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_LINE_IN_PART_WIDTH_DP);
        CHART_FULL_TOP_BOTTOM_MARGIN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_FULL_TOP_BOTTOM_MARGIN_DP);
        CHART_PART_HEIGHT_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_PART_HEIGHT_DP);

        CHART_DELIMITER_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_DELIMITER_WIDTH_DP);
        CHART_RECT_SELECT_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_RECT_SELECT_WIDTH_DP);
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

            if (currentTheme == DARK) {
                currentColors = darkThemeColors;
            } else {
                currentColors = lightThemeColors;
            }

            updateFontsColors();

            if (save) {
                AndroidApp.mainRepository.saveNewTheme(theme);
                AndroidApp.observerManager.notifyMyObservers(ObserverManager.KEY_OBSERVER_THEME_UPDATED);
            }
        }
    }

    private static void updateFontsColors() {
        simpleTextPaint.setColor(getColor(key_simpleTextColor));
        toolbarTextPaint.setColor(getColor(key_toolbarTextColor));
        chartTitleTextPaint.setColor(getColor(key_cellTitleTextColor));
        chartDescrTextPaint.setColor(getColor(key_chartDescrTextColor));

        simpleTextPaint.setTypeface(AndroidUtilites.getTypeface("fonts/rregular.ttf"));
        toolbarTextPaint.setTypeface(AndroidUtilites.getTypeface("fonts/rmedium.ttf"));
        chartTitleTextPaint.setTypeface(AndroidUtilites.getTypeface("fonts/rmedium.ttf"));
        chartDescrTextPaint.setTypeface(AndroidUtilites.getTypeface("fonts/rlight.ttf"));

        simpleTextPaint.setTextSize(SIMPLE_TEXT_SIZE_DP);
        toolbarTextPaint.setTextSize(TOOLBAR_TEXT_SIZE_DP);
        chartTitleTextPaint.setTextSize(CHART_TITLE_TEXT_SIZE_DP);
        chartDescrTextPaint.setTextSize(CHART_SUB_TEXT_SIZE_DP);
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
        darkThemeColors.put(key_toolbarBackColor, 0xff212d3b);
        darkThemeColors.put(key_toolbarTextColor, 0xffffffff);
        darkThemeColors.put(key_cellTitleTextColor, 0xff7bc4fb);
        darkThemeColors.put(key_cellBackColor , 0xff1d2733);
        darkThemeColors.put(key_cellSubBackColor , 0xff19232e);
        darkThemeColors.put(key_chartDescrTextColor, 0xff4d606f);
        darkThemeColors.put(key_simpleTextColor , 0xffffffff);
        darkThemeColors.put(key_delimiterColor, 0xff0f1823);
        darkThemeColors.put(key_rectSelectColor, 0x5Fa5c3d9);

        lightThemeColors.put(key_totalBackColor, 0xfff0f0f0);
        lightThemeColors.put(key_toolbarBackColor, 0xff517da2);
        lightThemeColors.put(key_toolbarTextColor, 0xffffffff);
        lightThemeColors.put(key_cellTitleTextColor, 0xff3896d4);
        lightThemeColors.put(key_cellBackColor , 0xffffffff);
        lightThemeColors.put(key_cellSubBackColor , 0xfff5f8f9);
        lightThemeColors.put(key_chartDescrTextColor, 0xff88939a);
        lightThemeColors.put(key_simpleTextColor , 0xff000000);
        lightThemeColors.put(key_delimiterColor, 0xffe7e7e7);
        lightThemeColors.put(key_rectSelectColor, 0x5Fa5c3d9);
    }
}
