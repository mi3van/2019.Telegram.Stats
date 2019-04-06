package com.kitzapp.telegram_stats.presentation.ui.activities;

import android.content.Context;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.kitzapp.telegram_stats.Application.AppManagers.MotionMagic;
import com.kitzapp.telegram_stats.Application.AppManagers.ObserverManager;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.domain.executor.ThreadExecutor;
import com.kitzapp.telegram_stats.domain.threading.TMainThread;
import com.kitzapp.telegram_stats.presentation.presenters.impl.ChartPresenter;
import com.kitzapp.telegram_stats.presentation.presenters.impl.TChartPresenter;
import com.kitzapp.telegram_stats.presentation.ui.activities.base.BaseActivity;
import com.kitzapp.telegram_stats.presentation.ui.components.TChartView;

import java.util.Observable;
import java.util.Observer;

public class ChartActivity extends BaseActivity implements ChartPresenter.View, Observer {

    private TChartPresenter _chartPresenter;
    private LinearLayout _containerLayout;
    private ScrollView _mainScrollView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chart;
    }

    @Override
    protected void initVariables() {
        _chartPresenter = new TChartPresenter(
                ThreadExecutor.getInstance(),
                TMainThread.getInstance(),
                this);
    }

    @Override
    protected void initViews() {
        _containerLayout = findViewById(R.id.chartsContainer);
        _mainScrollView = findViewById(R.id.mainScrollView);

        _chartPresenter.runAnalyzeJson();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showMessageToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
    public void clearChartsContainer() {
        _containerLayout.removeAllViews();
    }

    @Override
    public void addChartToContainer(TChartView chartView) {
        _containerLayout.addView(chartView);
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public void update(Observable observable, Object arg) {
        if ((int) arg == ObserverManager.KEY_OBSERVER_ALLOW_TOUCH_SCROLLVIEW_FOR_RECT_SELECT) {
            if (observable instanceof MotionMagic) {
                MotionMagic motionMagic = (MotionMagic) observable;
                boolean isAllowTouchEventForScrollView = motionMagic.getIsAllowTouchEventForScrollView();
                _mainScrollView.requestDisallowInterceptTouchEvent(isAllowTouchEventForScrollView);
            }
        }
    }
}
