package com.kitzapp.telegram_stats.core.appManagers.animation;

import android.animation.ValueAnimator;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

public class TScaleYAnim extends TTBaseAnimValue implements TScaleYAnimInterface {

    private Listener _myDelegate;
    private AnimationManagerInterface.ListenerForValues _listenerForManage;

    private float _oldscaleY = 1f;

    public TScaleYAnim(Listener listener) {
        _myDelegate = listener;
    }

    @Override
    public void setNewScaleY(float newScaleY) {
        if (_oldscaleY == newScaleY) {
            return;
        }
        ValueAnimator _valueAnimator = AndroidUtilites.getFloatAnimator(
                _oldscaleY,
                newScaleY,
                animation -> {
                    if (_myDelegate != null) {
                        _oldscaleY = (float) animation.getAnimatedValue();
                        _myDelegate.updateScaleY(_oldscaleY);
                    }
                    if (_listenerForManage != null) {
                        _listenerForManage.animNeedInvalidate();
                    }
                });
        _valueAnimator.start();
    }

    @Override
    public void setManagerListener(AnimationManagerInterface.ListenerForValues listenerForManage) {
        _listenerForManage = listenerForManage;
    }
}
