package com.kitzapp.telegram_stats.core.appManagers.animation;

interface AnimationManagerInterface {

    void setNewAlpha(String keyAlphaMap, int newAlpha);

    void setNewScaleY(float newScaleY);

    void setNewAlphaAndScaleY(String keyAlphaMap, int newAlpha, float newScaleY);

    interface ListenerForValues {
        void animNeedInvalidate(byte animType);
    }

    interface ListenerForView {
        void animNeedInvalidateView();
    }
}