package com.kitzapp.telegram_stats.core.appManagers.animation;

import android.animation.ValueAnimator;
import com.kitzapp.telegram_stats.common.AndroidUtilites;

import static com.kitzapp.telegram_stats.common.AppConts.MAX_VALUE_ALPHA;

public class TAlphaAnim extends TTBaseAnimValue implements TAlphaAnimInterface {

    private String _keyMap;

    private Listener _listener;
    private AnimationManagerInterface.ListenerForValues _listenerForManage;
    private ValueAnimator _valueAnimator = null;

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
        if (_valueAnimator != null ) {
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
                        _listenerForManage.animNeedInvalidate();
                    }
                });
        _valueAnimator.start();
        _oldAlpha = newAlpha;
    }

    @Override
    public void setManagerListener(AnimationManagerInterface.ListenerForValues listenerForManage) {
        _listenerForManage = listenerForManage;
    }
}
