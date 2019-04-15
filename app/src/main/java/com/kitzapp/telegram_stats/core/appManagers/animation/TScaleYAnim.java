package com.kitzapp.telegram_stats.core.appManagers.animation;

import android.animation.ValueAnimator;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

public class TScaleYAnim extends TTBaseAnimValue implements TScaleYAnimInterface {

    private ValueAnimator _valueAnimator = new ValueAnimator();
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
        if (_valueAnimator.isRunning()) {
            _oldscaleY = (float) _valueAnimator.getAnimatedValue();
            _valueAnimator.cancel();
        }
        _valueAnimator = AndroidUtilites.getFloatAnimator(
                _oldscaleY,
                newScaleY,
                animation -> {
                    if (_myDelegate != null) {
                        _myDelegate.updateScaleY((float) animation.getAnimatedValue());
                    }
                    if (_listenerForManage != null) {
                        _listenerForManage.animNeedInvalidate(AnimationManager.TYPE_ANIM_SCALE_Y);
                    }
                });
        _valueAnimator.start();
        _oldscaleY = newScaleY;
    }

    @Override
    public void setManagerListener(AnimationManagerInterface.ListenerForValues listenerForManage) {
        _listenerForManage = listenerForManage;
    }

    @Override
    public boolean isAnimatorRun() {
        return _valueAnimator.isRunning();
    }
}
