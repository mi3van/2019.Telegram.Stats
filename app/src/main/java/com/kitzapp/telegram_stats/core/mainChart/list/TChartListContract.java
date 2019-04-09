package com.kitzapp.telegram_stats.core.mainChart.list;

import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.MvpModelForAdapter;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.MvpPresenterForAdapter;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.MvpViewHolder;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpView;
import com.kitzapp.telegram_stats.pojo.chart.Chart;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

interface TChartListContract {
    interface TPresenter<V extends MvpView, VH extends MvpViewHolder> extends MvpPresenterForAdapter<V, VH> {

    }

    interface TModel extends MvpModelForAdapter {

        Chart getChartOnPosition(int position) throws Exception;

    }

    interface TView extends MvpView {
        interface AdapterCallback {
            void onError(String errorMessage);
        }
    }

    interface TViewHolder extends MvpViewHolder {
    }
}
