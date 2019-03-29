package com.kitzapp.telegram_stats.Application.AppManagers;

import java.util.*;

/**
 * Created by Ivan Kuzmin on 22.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ObserverManager extends Observable {

    private static int totalInit = 1;
    public final static int KEY_OBSERVER_THEME_UPDATED = totalInit++;
    public final static int KEY_OBSERVER_ALLOW_TOUCH_SCROLLVIEW_FOR_RECT_SELECT = totalInit++;
//    public final static int KEY_OBSERVER_ALLOW_TOUCH_SCROLLVIEW_FOR_PART = totalInit++;

    public void notifyMyObservers(int keyObserver) {
        this.setChanged();
        this.notifyObservers(keyObserver);
    }
}
