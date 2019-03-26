package com.kitzapp.telegram_stats.test.common;

import android.annotation.SuppressLint;

import com.kitzapp.telegram_stats.BuildConfig;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

/**
 * Created by Ivan Kuzmin on 2019-03-26.
 * Copyright © 2019 Example. All rights reserved.
 */

class AndroidUtilitesTest extends BaseTest {
    private static int AVAILABLE_RANGE = 20;
    private static int NOT_AVAILABLE_RANGE = 21;

    public static void main(String[] args) {
        runTests();
    }

    static void runTests() {
        try {
            isRangeLineAvailableTest();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
    *  Check 2 point for available in range and not available, 9 reps
    * */
    private static void isRangeLineAvailableTest() throws Exception {
        checkTwoPoints(0, 0, 20, 0);
        checkTwoPoints(0, 0, 10, -10);
        checkTwoPoints(0, 0, 0, -20);
        checkTwoPoints(0, 0, -10, -10);

        checkTwoPoints(0, 0, -20, 0);
        checkTwoPoints(0, 0, -10, 10);
        checkTwoPoints(0, 0, 0,20);
        checkTwoPoints(0, 0, 10, 10);
    }
    
    @SuppressLint("DefaultLocale")
    private static void checkTwoPoints(float x0, float y0, float x1, float y1) throws Exception {
        String availableRangeString = "IS AVAILABLE RANGE (%d) ERROR: x0: \"%.1f\", y0: \"%.1f\", x1: \"%.1f\", y1: \"%.1f\".";
        String notAvailableRangeString = "IS NOT AVAILABLE RANGE (%d) ERROR: x0: \"%.1f\", y0: \"%.1f\", x1: \"%.1f\", y1: \"%.1f\".";

        String messageErrorAvail = String.format(availableRangeString, AVAILABLE_RANGE, x0, y0, x1, y1);
        String messageErrorNotAvail = String.format(notAvailableRangeString, NOT_AVAILABLE_RANGE, x0, y0, x1, y1);
        assertEquals(true, AndroidUtilites.isRangeLineAvailable(x0, y0, x1, y1, AVAILABLE_RANGE), messageErrorAvail);
        assertNotEquals(true, AndroidUtilites.isRangeLineAvailable(x0, y0, x1, y1, NOT_AVAILABLE_RANGE), messageErrorNotAvail);
    }
}
