package com.kitzapp.telegram_stats.domain.repository.Charts;

import android.content.Context;
import com.kitzapp.telegram_stats.domain.model.ChartModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ivan Kuzmin on 2019-03-20.
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartRepository implements ChartRepository {

    @Override
    public String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("chart_data.json");
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
