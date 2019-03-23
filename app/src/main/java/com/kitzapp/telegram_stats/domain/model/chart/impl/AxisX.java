package com.kitzapp.telegram_stats.domain.model.chart.impl;

import java.util.ArrayList;

/**
 * Created by Ivan Kuzmin on 23.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class AxisX implements AxisXI {

    private String key;
    private ArrayList<Long> dates;
    private byte type;

    public AxisX(String key, ArrayList<Long> dates, byte type) {
        this.key = key;
        this.dates = dates;
        this.type = type;
    }

    @Override
    public long getDateInfo(int position) {
        return dates.get(position);
    }

    @Override
    public int getCountDots() {
        return dates.size();
    }

    @Override
    public byte getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
