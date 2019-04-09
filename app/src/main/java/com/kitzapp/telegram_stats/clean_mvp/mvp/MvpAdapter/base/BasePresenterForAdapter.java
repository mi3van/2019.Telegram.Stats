package com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.MvpModelForAdapter;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.MvpPresenterForAdapter;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter.MvpViewHolder;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpView;

public abstract class BasePresenterForAdapter<V extends MvpView, H extends MvpViewHolder, M extends MvpModelForAdapter>
        implements MvpPresenterForAdapter<V, H> {
    @Nullable
    protected V view = null;
    @NonNull
    protected M model;
    @Nullable
    protected H viewHolder = null;

    public BasePresenterForAdapter(@NonNull M model) {
        this.model = model;
    }

    public void attachView(@Nullable V mvpView) {
        view = mvpView;
    }

    @Override
    public void attachViewHolder(H viewHolder) {
        this.viewHolder = viewHolder;
    }

    public void deattachView() {
        view = null;
        viewHolder = null;
    }

    public void destroy() {
        view = null;
        viewHolder = null;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public boolean isViewHolderAttached() {
        return viewHolder != null;
    }

    @Override
    public int getItemCount() {
        return model.getItemCount();
    }
}