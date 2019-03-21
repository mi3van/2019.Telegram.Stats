package com.kitzapp.telegram_stats.domain.repository.chart;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ivan Kuzmin on 2019-03-20.
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartRepository implements ChartRepository {

    private Context _context;

    public TChartRepository(Context context) {
        this._context = context;
    }

    @Override
    public String getJsonForChart() {
        String json;
        try {
            InputStream is = _context.getAssets().open("chart_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
