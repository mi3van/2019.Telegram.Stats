package com.kitzapp.telegram_stats.core.mainChart.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.base.BaseAdapterForRecyclerView;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartAdapter extends BaseAdapterForRecyclerView<TChartViewHolder> implements TChartListContract.TView {

    private Context _context;
    private TChartListPresenter _presenter;
    private TChartListModel _model;
    private AdapterCallback _callback;

    public TChartAdapter(Context context, ChartsList chartsList, AdapterCallback callback) {
        _context = context;
        _model = new TChartListModel(chartsList);
        _presenter = new TChartListPresenter(_model);
        _presenter.attachView(this);
        _callback = callback;
    }

    @NonNull
    @Override
    public TChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.cell_chart_for_recyclerview, parent, false);
        TChartViewHolder chartViewHolder = new TChartViewHolder(view);
        return chartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TChartViewHolder holder, int position) {
        try {
            _presenter.bindViewHolderOnPosition(holder, position);
        } catch (Exception e) {
            if (_callback != null) {
                _callback.onError(e.getMessage());
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        _presenter.viewIsReady();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        _presenter.deattachView();
    }

    @Override
    public int getItemCount() {
        return _presenter.getItemCount();
    }
}
