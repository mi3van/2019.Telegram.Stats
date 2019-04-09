package com.kitzapp.telegram_stats.core.appManagers;

import android.content.Context;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.BuildConfig;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ivan Kuzmin on 23.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class JsonManager {

    @Nullable
    public static String getJsonStringFromFile(String fileName, Context context) {
        String jsonString = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            if (BuildConfig.DEBUG) {
                ex.printStackTrace();
            }
        }
        return jsonString;
    }

}
