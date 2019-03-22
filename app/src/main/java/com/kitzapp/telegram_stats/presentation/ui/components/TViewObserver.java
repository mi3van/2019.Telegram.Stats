package com.kitzapp.telegram_stats.presentation.ui.components;

import java.util.Observer;

/**
 * Created by Ivan Kuzmin on 22.03.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface TViewObserver extends Observer {

    void init();

    void addObserver();

    void deleteObserver();

}
