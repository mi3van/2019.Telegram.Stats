package com.kitzapp.telegram_stats.core.mainChart.list;

import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpModel;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpPresenter;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpView;
import com.kitzapp.telegram_stats.pojo.chart.Chart;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

interface TChartListContract {
    interface TPresenter<V extends MvpView> extends MvpPresenter<V> {
        int getItemCount();

        Chart getChartOnPosition(int position) throws Exception;
    }

    interface TModel extends MvpModel {

        Chart getChartOnPosition(int position) throws Exception;

        int getItemCount();
    }

    interface TView extends MvpView {
        interface AdapterCallback {
            void onError(String errorMessage);
        }
    }
}
