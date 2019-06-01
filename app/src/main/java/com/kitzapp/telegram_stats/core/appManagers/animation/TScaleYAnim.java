package com.kitzapp.telegram_stats.core.appManagers.animation;

import android.animation.ValueAnimator;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

public class TScaleYAnim extends TTBaseAnimValue implements TScaleYAnimInterface {

    private Listener _myDelegate;
    private AnimationManagerInterface.ListenerForValues _listenerForManage;
    private ValueAnimator _valueAnimator = null;

    private float _newScaleY = 1f;

    public TScaleYAnim(Listener listener) {
        _myDelegate = listener;
    }

    @Override
    public void setNewScaleY(float oldScaleY, float newScaleY) {
        if (oldScaleY == newScaleY || _newScaleY == newScaleY) {
            return;
        }
        _newScaleY = newScaleY;
        if (_valueAnimator != null ) {
            oldScaleY = (float) _valueAnimator.getAnimatedValue();
            _valueAnimator.cancel();
        }
        _valueAnimator = AndroidUtilites.getFloatAnimator(
                oldScaleY,
                newScaleY,
                animation -> {
                    if (_myDelegate != null) {
                        _myDelegate.updateScaleY((float) animation.getAnimatedValue());
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
