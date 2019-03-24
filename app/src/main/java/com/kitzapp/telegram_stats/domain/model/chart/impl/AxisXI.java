package com.kitzapp.telegram_stats.domain.model.chart.impl;

/**
 * Created by Ivan Kuzmin on 23.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface AxisXI {

    long[] getData();

    int getCountDots();

    byte getType();

    String getKey();
}
