package com.kitzapp.telegram_stats.core.appManagers;

import android.content.Context;
import android.graphics.Typeface;
import com.kitzapp.telegram_stats.AndroidApp;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

import java.util.HashMap;

/**
 * Created by Ivan Kuzmin on 2019-03-22.
 * Copyright © 2019 Example. All rights reserved.
 */

public class ThemeManager {
//    TEXT SIZES DP
    public static final int TEXT_BIG_SIZE_DP = 18;
    public static final int TEXT_MEDIUM_SIZE_DP = 16;
    public static final int TEXT_SMALL_SIZE_DP = 14;

//    CHART CELLS DP
    private static final int CELL_HEIGHT_48DP = 48;
    private static final int CELL_HEIGHT_56DP = 56;
    private static final int CHART_CELL_BOTTOM_MARGIN_DP = 28;

//    CHART CELLS PX
    public static final int CELL_HEIGHT_48DP_IN_PX;
    public static final int CELL_HEIGHT_56DP_IN_PX;
    public static final int CHART_CELL_BOTTOM_MARGIN_PX;
    public static final int MARGIN_32DP_IN_PX;
    public static final int MARGIN_16DP_IN_PX;
    public static final int MARGIN_8DP_IN_PX;
    public static final int MARGIN_4DP_IN_PX;

//    CHART VIEWS DP
    private static final int CHART_FULL_TOP_BOTTOM_MARGIN_DP = 4;
    private static final int CHART_PART_HEIGHT_DP = 250;
    private static final int CHART_DELIMITER_FATNESS_DP = 1;

    private static final int CHART_RECT_SELECT_WIDTH_DP = 4;
    private static final int CHART_MINIATURE_VERTICAL_PADDING_SUM_DP = 4;

    private static final int CHART_CIRCLE_SIZE_DP = 10;

    //    CHART VIEWS PX
    public static final int CHART_LINE_IN_BIG_WIDTH_PX;
    public static final int CHART_LINE_IN_MINIATURE_WIDTH_PX;
    public static final int CHART_FULL_TOP_BOTTOM_MARGIN_PX;
    public static final int CHART_PART_HEIGHT_PX;
    public static final int CHART_DELIMITER_FATNESS_PX;

    public static final int CHART_RECT_SELECT_WIDTH_PX;
    public static final int CHART_BIG_VERTICAL_PADDING_SUM_PX;
    public static final int CHART_BIG_VERTICAL_PADDING_HALF_PX;
    public static final int CHART_MINIATURE_VERTICAL_PADDING_SUM_PX;
    public static final int CHART_MINIATURE_VERTICAL_PADDING_HALF_PX;

    public static final int CHART_CIRCLE_LINE_WIDTH_PX;
    public static final int CHART_CIRCLE_SIZE_PX;
    public static final int CHART_CIRCLE_HALF_SIZE_PX;
    public static final int CHART_CIRCLE_RADIUS_PX;
    public static final int CHART_CIRCLE_RADIUS_INSIDE_PX;

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
    public static final String key_toolbarIconColor = "actionBarIconColor";

    //    COLORS CELLS
    public static final String key_cellBackColor = "cellBackColor";
    public static final String key_cellChartFullBackColor = "cellChartFullBackColor";

    public static final String key_rectSelectColor = "rectSelectColor";
    public static final String key_rectBackColor = "rectBackColor";

    //    COLORS OTHER VIEWS
    public static final String key_blackWhiteTextColor = "simpleTextColor";
    public static final String key_grayTextColor = "chartDescrTextColor";
    public static final String key_delimiterColor = "delimiterColor";

    private static final int colorLightBackup;
    private static final int colorDarkBackup;

    public static Typeface rBoldTypeface;
    public static Typeface rMediumTypeface;
    public static Typeface rRegularTypeface;

    static {
        currentColors = new HashMap<>();
        darkThemeColors = new HashMap<>();
        lightThemeColors = new HashMap<>();

        colorLightBackup = 0x00000000;
        colorDarkBackup = 0xffffffff;

        initThemesHashes();

        applyTheme(AndroidApp.mainRepository.getCurrentTheme());

        CELL_HEIGHT_48DP_IN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CELL_HEIGHT_48DP);
        CELL_HEIGHT_56DP_IN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CELL_HEIGHT_56DP);
        CHART_CELL_BOTTOM_MARGIN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_CELL_BOTTOM_MARGIN_DP);
        MARGIN_16DP_IN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, 16);
        MARGIN_32DP_IN_PX = MARGIN_16DP_IN_PX << 1;
        MARGIN_8DP_IN_PX = MARGIN_16DP_IN_PX >> 1;
        MARGIN_4DP_IN_PX = MARGIN_8DP_IN_PX >> 1;

        CHART_LINE_IN_MINIATURE_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, 1);
        CHART_LINE_IN_BIG_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, 2);
        CHART_FULL_TOP_BOTTOM_MARGIN_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_FULL_TOP_BOTTOM_MARGIN_DP);
        CHART_PART_HEIGHT_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_PART_HEIGHT_DP);

        CHART_DELIMITER_FATNESS_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_DELIMITER_FATNESS_DP);
        CHART_RECT_SELECT_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_RECT_SELECT_WIDTH_DP);
        CHART_BIG_VERTICAL_PADDING_SUM_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_CIRCLE_SIZE_DP);
        CHART_BIG_VERTICAL_PADDING_HALF_PX = CHART_BIG_VERTICAL_PADDING_SUM_PX >> 1;
        CHART_MINIATURE_VERTICAL_PADDING_SUM_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_MINIATURE_VERTICAL_PADDING_SUM_DP);
        CHART_MINIATURE_VERTICAL_PADDING_HALF_PX = CHART_MINIATURE_VERTICAL_PADDING_SUM_PX >> 1;

        CHART_CIRCLE_LINE_WIDTH_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, 2);
        CHART_CIRCLE_SIZE_PX = AndroidUtilites.convertDpToPx(AndroidApp.resources, CHART_CIRCLE_SIZE_DP);
        CHART_CIRCLE_HALF_SIZE_PX = CHART_CIRCLE_SIZE_PX >> 1;
        CHART_CIRCLE_RADIUS_PX = CHART_CIRCLE_HALF_SIZE_PX - CHART_LINE_IN_MINIATURE_WIDTH_PX - 1;
        CHART_CIRCLE_RADIUS_INSIDE_PX = CHART_CIRCLE_RADIUS_PX - (CHART_CIRCLE_LINE_WIDTH_PX >> 1);
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

            if (save) {
                AndroidApp.mainRepository.saveNewTheme(theme);
                AndroidApp.observerManager.notifyMyObservers(ObserverManager.KEY_OBSERVER_THEME_UPDATED);
            }
        }
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
        darkThemeColors.put(key_toolbarIconColor, 0xffffffff);
        darkThemeColors.put(key_cellBackColor , 0xff1d2733);
        darkThemeColors.put(key_cellChartFullBackColor , 0xff19232e);
        darkThemeColors.put(key_blackWhiteTextColor, 0xffffffff);
        darkThemeColors.put(key_rectBackColor, 0xaf19232e);

        lightThemeColors.put(key_totalBackColor, 0xfff0f0f0);
        lightThemeColors.put(key_toolbarBackColor, 0xffffffff);
        lightThemeColors.put(key_toolbarIconColor, 0xff8e8e93);
        lightThemeColors.put(key_cellBackColor , 0xffffffff);
        lightThemeColors.put(key_cellChartFullBackColor, 0xffffffff);
        lightThemeColors.put(key_blackWhiteTextColor, 0xff000000);
        lightThemeColors.put(key_rectBackColor, 0xafeeeeee);

        //  No change colors
        int color = 0x6f777777;
        lightThemeColors.put(key_grayTextColor, color);    darkThemeColors.put(key_grayTextColor, color);
        color = 0x5Fa5c3d9;
        lightThemeColors.put(key_rectSelectColor, color);  darkThemeColors.put(key_rectSelectColor, color);
        color = 0x2a666666;
        lightThemeColors.put(key_delimiterColor, color);   darkThemeColors.put(key_delimiterColor, color);
    }

    public static void initTextFonts(Context context) {
        rBoldTypeface = AndroidUtilites.getTypeface(context, "fonts/rbold.ttf");
        rMediumTypeface = AndroidUtilites.getTypeface(context, "fonts/rmedium.ttf");
        rRegularTypeface = AndroidUtilites.getTypeface(context, "fonts/rregular.ttf");
    }
}
