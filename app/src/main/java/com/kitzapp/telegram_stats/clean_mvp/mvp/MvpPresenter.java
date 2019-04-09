package com.kitzapp.telegram_stats.clean_mvp.mvp;

import androidx.annotation.Nullable;

public interface MvpPresenter<V extends MvpView> {

    void attachView(@Nullable V view);
    void deattachView();
    void viewIsReady();
    void destroy();

    /**
     * Method that should signal the appropriate view to show the appropriate error with the provided message.
     */
    void onError(String message);
}