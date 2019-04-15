package com.kitzapp.telegram_stats.core.appManagers.animation;

import android.animation.ValueAnimator;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

import static com.kitzapp.telegram_stats.common.AppConts.MAX_VALUE_ALPHA;

public class TAlphaAnim extends TTBaseAnimValue implements TAlphaAnimInterface {

    private ValueAnimator _valueAnimator = new ValueAnimator();
    private String _keyMap;

    private Listener _listener;
    private AnimationManagerInterface.ListenerForValues _listenerForManage;

    private int _oldAlpha = MAX_VALUE_ALPHA;

    public TAlphaAnim(String keyMap, Listener listener) {
        _keyMap = keyMap;
        _listener = listener;
    }

    @Override
    public void setNewAlpha(int newAlpha) {
        if (_oldAlpha == newAlpha) {
            return;
        }
        if (_valueAnimator.isRunning()) {
            _oldAlpha = (int) _valueAnimator.getAnimatedValue();
            _valueAnimator.cancel();
        }
        _valueAnimator = AndroidUtilites.getIntAnimator(
                _oldAlpha,
                newAlpha,
                animation -> {
                    if (_listener != null) {
                        _listener.updateAlpha(_keyMap, (int) animation.getAnimatedValue());
                    }
                    if (_listenerForManage != null) {
                        _listenerForManage.animNeedInvalidate(AnimationManager.TYPE_ANIM_ALPHA);
                    }
                });
        _valueAnimator.start();
        _oldAlpha = newAlpha;
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
