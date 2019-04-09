package com.kitzapp.telegram_stats.clean_mvp.mvp.MvpAdapter;

import androidx.annotation.NonNull;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpPresenter;
import com.kitzapp.telegram_stats.clean_mvp.mvp.MvpView;

/**
 * Created by Ivan Kuzmin on 2019-04-02;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public interface MvpPresenterForAdapter<V extends MvpView, H extends MvpViewHolder> extends MvpPresenter<V> {

    void attachViewHolder(H viewHolder);

    int getItemCount();

    void bindViewHolderOnPosition(@NonNull V view, int position);
}