package com.kitzapp.telegram_stats.domain.interactors.impl.preference;

import com.kitzapp.telegram_stats.domain.executor.Executor;
import com.kitzapp.telegram_stats.domain.interactors.base.AbstractInteractor;
import com.kitzapp.telegram_stats.domain.repository.chart.ChartRepository;
import com.kitzapp.telegram_stats.domain.threading.MainThread;

/**
 * This is an interactor boilerplate with a reference to a model repository.
 * <p/>
 */
public class TPreferenceInteractor extends AbstractInteractor implements PreferenceInteractor {

    private Callback mCallback;
    private ChartRepository mRepository;

    public TPreferenceInteractor(Executor threadExecutor,
                                 MainThread mainThread,
                                 Callback callback, ChartRepository repository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mRepository = repository;
    }

    @Override
    public void run() {
        // TODO: Implement this with your business logic
    }
}
