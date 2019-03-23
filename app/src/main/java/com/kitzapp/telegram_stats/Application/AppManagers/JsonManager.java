package com.kitzapp.telegram_stats.Application.AppManagers;

import android.content.Context;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.Application.AndroidApp;
import com.kitzapp.telegram_stats.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ivan Kuzmin on 23.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class JsonManager {

    @Nullable
    public static String getJsonStringFromFile(String fileName) {
        String jsonString = null;
        try {
            Context context = AndroidApp.applicationContext;
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
