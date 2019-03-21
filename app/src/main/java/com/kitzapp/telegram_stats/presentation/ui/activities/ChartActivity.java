package com.kitzapp.telegram_stats.presentation.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.domain.executor.ThreadExecutor;
import com.kitzapp.telegram_stats.domain.threading.TMainThread;
import com.kitzapp.telegram_stats.presentation.presenters.impl.ChartPresenter;
import com.kitzapp.telegram_stats.presentation.presenters.impl.TChartPresenter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChartActivity extends AppCompatActivity implements ChartPresenter.View {

    private FloatingActionButton _loading;

    private TChartPresenter _chartPresenter;
    private ProgressBar _progressBar;
    private TextView _textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initVariables();
    }

    private void initVariables() {
        _loading = findViewById(R.id.loading);
        _progressBar = findViewById(R.id.progressBar);
        _textView = findViewById(R.id.test);

        _loading.setOnClickListener(l -> _chartPresenter.runAnalyzeJson());

        _chartPresenter = new TChartPresenter(
                getApplicationContext(),
                ThreadExecutor.getInstance(),
                TMainThread.getInstance(),
                this);
    }

    @Override
    public void showProgress() {
        _progressBar.setVisibility(View.VISIBLE);
        _textView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        _progressBar.setVisibility(View.GONE);
        _textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        _textView.setText(message);
    }

    @Override
    public void displayJson(String json) {
        _textView.setText(json);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _chartPresenter.resume();
    }
}
