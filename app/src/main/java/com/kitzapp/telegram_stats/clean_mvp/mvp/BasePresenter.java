package com.kitzapp.telegram_stats.clean_mvp.mvp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BasePresenter<V extends MvpView, M extends MvpModel> implements MvpPresenter<V> {

    @NonNull protected M model;
    @Nullable protected V view = null;

    public BasePresenter(@NonNull M model) {
        this.model = model;
    }

    public void attachView(V mvpView) {
        view = mvpView;
    }

    public void deattachView() {
        view = null;
    }

    public void destroy() {
        view = null;
    }

    public boolean isViewAttached() {
        return view != null;
    }
}