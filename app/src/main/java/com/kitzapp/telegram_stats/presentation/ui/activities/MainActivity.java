package com.kitzapp.telegram_stats.presentation.ui.activities;

import android.os.Bundle;

import com.kitzapp.telegram_stats.R;
import com.kitzapp.telegram_stats.presentation.presenters.impl.MainPresenter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

    }
}
