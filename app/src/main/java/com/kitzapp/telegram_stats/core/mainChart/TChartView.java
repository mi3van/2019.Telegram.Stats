package com.kitzapp.telegram_stats.core.mainChart;

import android.content.Context;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.core.appManagers.ObserverManager;
import com.kitzapp.telegram_stats.core.appManagers.motions.BaseMotionManager;
import com.kitzapp.telegram_stats.customViews.BaseActivity;
import com.kitzapp.telegram_stats.customViews.TFullChartView;

import java.util.Observable;
import java.util.Observer;

import static com.kitzapp.telegram_stats.core.mainChart.TChartContract.TView;

public class TChartView extends BaseActivity implements TView, Observer {

    private TChartPresenter _chartPresenter;

    private LinearLayout _containerLayout;
    private ScrollView _mainScrollView;

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

        _containerLayout = findViewById(R.id.chartsContainer);
        _mainScrollView = findViewById(R.id.mainScrollView);

        _chartPresenter.runAnalyzeJson();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.themeBtn) {
            _mainScrollView.fling(0);
            _chartPresenter.changeCurrentTheme();
            this.updateActionBarBackgr();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clearChartsContainer() {
        _containerLayout.removeAllViews();
    }

    @Override
    public void addChartToContainer(TFullChartView chartView) {
        _containerLayout.addView(chartView);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if ((byte) arg == ObserverManager.KEY_OBSERVER_PROHIBITED_SCROLL) {
            if (observable instanceof BaseMotionManager) {
                BaseMotionManager motionMagic = (BaseMotionManager) observable;

                boolean isProhibitedScroll = motionMagic.getIsProhibitedScroll();
                _mainScrollView.requestDisallowInterceptTouchEvent(isProhibitedScroll);
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
