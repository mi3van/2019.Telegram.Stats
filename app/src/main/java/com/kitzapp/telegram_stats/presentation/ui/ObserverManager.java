package com.kitzapp.telegram_stats.presentation.ui;

import android.util.SparseArray;
import com.kitzapp.telegram_stats.BuildConfig;

import java.util.*;

/**
 * Created by Ivan Kuzmin on 22.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ObserverManager extends Observable {

    private static int totalInit = 1;
    public final static int KEY_OBSERVER_THEME_UPDATED = totalInit++;

    public void notifyMyObservers(int keyObserver) {
        this.setChanged();
        this.notifyObservers(keyObserver);
    }
}
