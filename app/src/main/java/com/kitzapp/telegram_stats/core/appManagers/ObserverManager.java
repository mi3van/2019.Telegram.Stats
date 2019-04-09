package com.kitzapp.telegram_stats.core.appManagers;

import java.util.*;

/**
 * Created by Ivan Kuzmin on 22.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ObserverManager extends Observable {

    private static byte totalInit = 1;
    public final static byte KEY_OBSERVER_THEME_UPDATED = totalInit++;
    public final static byte KEY_OBSERVER_PROHIBITED_SCROLL = totalInit++;

    public void notifyMyObservers(byte keyObserver) {
        this.setChanged();
        this.notifyObservers(keyObserver);
    }
}