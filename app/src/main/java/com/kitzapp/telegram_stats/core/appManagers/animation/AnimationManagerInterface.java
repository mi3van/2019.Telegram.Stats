package com.kitzapp.telegram_stats.core.appManagers.animation;

interface AnimationManagerInterface {

    void setNewAlpha(String keyAlphaMap, int newAlpha);

    void setNewScaleY(float oldScaleY, float newScaleY);

    interface ListenerForValues {
        void animNeedInvalidate();
    }

    interface ListenerForView {
        void animNeedInvalidateView();
    }
}