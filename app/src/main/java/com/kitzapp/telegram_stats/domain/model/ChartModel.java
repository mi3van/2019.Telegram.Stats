package com.kitzapp.telegram_stats.domain.model;

import java.util.ArrayList;

/**
 * A sample model. Replace this with your own.
 */
public class ChartModel {
    public static final byte TYPE_LINE = 1;
    public static final byte TYPE_X_AXIS = 2;

    private ArrayList<String> titleAndData;
    private byte type;
    private short color;
    private String name;

    public ChartModel(ArrayList<String> titleAndData, byte type, short color, String name) {
        this.titleAndData = titleAndData;
        this.type = type;
        this.color = color;
        this.name = name;
    }

    public ArrayList<String> getTitleAndData() {
        return titleAndData;
    }

    public byte getType() {
        return type;
    }

    public short getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
