package com.kitzapp.telegram_stats.pojo.chart.impl;

public class Line implements LineI {
    public static final byte TYPE_LINE = 1;
    public static final byte TYPE_X_AXIS = 2;

    private String key;
    private long[] data;
    private byte type;
    private String name;
    private int color;
    private boolean isActive = true;

    public Line(String key, long[] data, byte type, String name, int color) {
        this.key = key;
        this.data = data;
        this.type = type;
        this.name = name;
        this.color = color;
    }

    public long[] getData() {
        return data;
    }

    @Override
    public int getCountDots() {
        return data.length;
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
    public void setIsActive(boolean active) {
        isActive = active;
    }

    @Override
    public String getKey() {
        return key;
    }
}
