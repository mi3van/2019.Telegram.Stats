package com.kitzapp.telegram_stats.test.common;

/**
 * Created by Ivan Kuzmin on 2019-03-26.
 * Copyright © 2019 Example. All rights reserved.
 */

class BaseTest {
    static void assertEquals(boolean expected, boolean actual, String message) throws Exception {
        if (expected != actual) {
            throw new Exception(message);
        }
    }
    static void assertNotEquals(boolean expected, boolean actual, String message) throws Exception {
        if (expected == actual) {
            throw new Exception(message);
        }
    }
}
