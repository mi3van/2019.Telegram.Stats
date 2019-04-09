package com.kitzapp.telegram_stats.pojo.chart.impl;

/**
 * Created by Ivan Kuzmin on 23.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class AxisX implements AxisXI {

    private String key;
    private long[] dates;
    private byte type;

    public AxisX(String key, long[] dates, byte type) {
        this.key = key;
        this.dates = dates;
        this.type = type;
    }

    @Override
    public long[] getData() {
        return dates;
    }

    @Override
    public int getCountDots() {
        return dates.length;
    }

    @Override
    public byte getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
