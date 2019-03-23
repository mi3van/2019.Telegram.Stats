package com.kitzapp.telegram_stats.domain.model.chart.impl;

import java.util.ArrayList;

public class Line implements LineI {
    public static final byte TYPE_LINE = 1;
    public static final byte TYPE_X_AXIS = 2;

    private String key;
    private ArrayList<Integer> data;
    private byte type;
    private String name;
    private int color;
    private boolean isActive = true;

    public Line(String key, ArrayList<Integer> data, byte type, String name, int color) {
        this.key = key;
        this.data = data;
        this.type = type;
        this.name = name;
        this.color = color;
    }

    @Override
    public int getDotInfo(int position) {
        return data.get(position);
    }

    @Override
    public int getCountDots() {
        return data.size();
    }

    @Override
    public byte getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public String getKey() {
        return key;
    }
}
