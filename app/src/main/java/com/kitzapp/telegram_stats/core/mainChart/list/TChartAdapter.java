package com.kitzapp.telegram_stats.core.mainChart.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.customViews.TFullCellView;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

/**
 * Created by Ivan Kuzmin on 09.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class TChartAdapter extends BaseAdapter implements TChartListContract.TView {

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

    @Override
    public int getCount() {
        return _presenter.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        try {
            return _presenter.getChartOnPosition(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(_context).inflate(R.layout.cell_chart_for_recyclerview, parent, false);

            TFullCellView tFullCellView = view.findViewById(R.id.fullChartCell);

            try {
                tFullCellView.loadData(_presenter.getChartOnPosition(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}
