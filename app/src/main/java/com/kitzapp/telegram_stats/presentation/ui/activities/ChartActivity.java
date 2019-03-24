package com.kitzapp.telegram_stats.presentation.ui.activities;

import android.graphics.Color;
import android.os.Build;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.common.AndroidUtilites;
import com.kitzapp.telegram_stats.domain.executor.ThreadExecutor;
import com.kitzapp.telegram_stats.domain.threading.TMainThread;
import com.kitzapp.telegram_stats.presentation.presenters.impl.ChartPresenter;
import com.kitzapp.telegram_stats.presentation.presenters.impl.TChartPresenter;
import com.kitzapp.telegram_stats.presentation.ui.activities.base.BaseActivity;
import com.kitzapp.telegram_stats.presentation.ui.components.ChartView.TChartView;

import java.util.Objects;

public class ChartActivity extends BaseActivity implements ChartPresenter.View {

    private FloatingActionButton _loading;

    private ViewGroup _baseLayout;
    private TChartPresenter _chartPresenter;
    private ProgressBar _progressBar;
    private Toolbar _toolbar;
    private LinearLayout _containerLayout;

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
        _baseLayout = findViewById(R.id.baseLayout);
        _loading = findViewById(R.id.loading);
        _progressBar = findViewById(R.id.progressBar);
        _toolbar = findViewById(R.id.toolbar);
        _containerLayout = findViewById(R.id.chartsContainer);

        setSupportActionBar(_toolbar);
        String toolbarTitle = getResources().getString(R.string.toolbar_title);
        Objects.requireNonNull(getSupportActionBar()).setTitle(toolbarTitle);

        _loading.setOnClickListener(l -> _chartPresenter.runAnalyzeJson());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int blackColor = AndroidUtilites.getColorSDK(R.color.cBlack);
            Window window = getWindow();
            window.setNavigationBarColor(blackColor);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void showProgress() {
        _progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        _progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(_baseLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.themeBtn) {
            _chartPresenter.changeCurrentTheme();
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
}
