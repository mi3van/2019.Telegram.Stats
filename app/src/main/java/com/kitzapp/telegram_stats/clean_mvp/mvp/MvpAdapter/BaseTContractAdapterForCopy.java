package com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter;

import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpView;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

interface BaseTContractAdapterForCopy {
    interface TPresenter<V extends MvpView, H extends MvpViewHolder> extends MvpPresenterForAdapter<V, H> {

    }

    interface TModel extends MvpModelForAdapter {

    }

    interface TView extends MvpView {

    }

    interface TViewHolder extends MvpViewHolder {

    }
}
