package com.kitzapp.telegram_stats.common;

/**
 * Created by Ivan Kuzmin on 29.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class MyLongPair {
    private long max;
    private long min;

    public MyLongPair(long max, long min) {
        this.max = max;
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }
}
