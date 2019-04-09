package com.kitzapp.telegram_stats.clean_mvp.mvp;

public abstract class BasePresenter<V extends MvpView, M extends MvpModel> implements MvpPresenter<V> {

     protected M model;
     protected V view = null;

    public BasePresenter( M model) {
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