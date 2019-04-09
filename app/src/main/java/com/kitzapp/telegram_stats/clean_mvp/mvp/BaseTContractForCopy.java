package com.kitzapp.telegram_stats.clean_mvp.mvp;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

interface BaseTContractForCopy {
    interface TPresenter<V extends MvpView> extends MvpPresenter<V> {

    }

    interface TModel extends MvpModel {

    }

    interface TView extends MvpView {

    }
}
