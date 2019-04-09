package com.kitzapp.telegram_stats.core.mainChart;

import android.content.Context;
import android.view.MenuItem;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.motions.BaseMotionManager;
import com.kitzapp.telegram_stats.core.mainChart.list.TChartAdapter;
import com.kitzapp.telegram_stats.customViews.BaseActivity;
import com.kitzapp.telegram_stats.pojo.chart.ChartsList;

import java.util.Observable;
import java.util.Observer;

import static com.kitzapp.telegram_stats.core.mainChart.TChartContract.TView;

public class TChartView extends BaseActivity implements TView, Observer {

    private TChartPresenter _chartPresenter;

    private RecyclerView _chartsRecyclerView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chart;
    }

    @Override
    protected void initVariables() {
        TChartModel chartModel = new TChartModel();
        _chartPresenter = new TChartPresenter(chartModel);
    }

    @Override
    protected void initViews() {
        _chartPresenter.attachView(this);

        _chartsRecyclerView = findViewById(R.id.chartRecyclerView);
        this.initRecycleView(_chartsRecyclerView);

        _chartPresenter.runAnalyzeJson();
    }

    private void initRecycleView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager viewManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(viewManager);

        recyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.themeBtn) {
            _chartPresenter.changeCurrentTheme();
            this.updateActionBarBackgr();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateChartsData(ChartsList chartsList) {
        TChartAdapter chartAdapter = new TChartAdapter(getBaseContext(), chartsList, this::showMessageToast);
        _chartsRecyclerView.setAdapter(chartAdapter);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if ((byte) arg == ObserverManager.KEY_OBSERVER_PROHIBITED_SCROLL) {
            if (observable instanceof BaseMotionManager) {
                BaseMotionManager motionMagic = (BaseMotionManager) observable;

                boolean isProhibitedScroll = motionMagic.getIsProhibitedScroll();
                _chartsRecyclerView.requestDisallowInterceptTouchEvent(isProhibitedScroll);
            }
        }
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    protected void onDestroy() {
        _chartPresenter.destroy();
        super.onDestroy();
    }
}
