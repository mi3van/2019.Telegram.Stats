package com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpView;

public abstract class BasePresenterAdapter<V extends MvpView, H extends MvpViewHolder, M extends MvpModelForAdapter>
        implements MvpPresenterForAdapter<V, H> {
    @Nullable
    protected V view = null;
    @NonNull
    protected M model;
    @Nullable
    protected H viewHolder = null;

    public BasePresenterAdapter(@NonNull M model) {
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