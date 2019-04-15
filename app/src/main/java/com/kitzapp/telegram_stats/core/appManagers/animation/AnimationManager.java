package com.kitzapp.telegram_stats.core.appManagers.animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan Kuzmin on 12.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class AnimationManager implements AnimationManagerInterface, AnimationManagerInterface.ListenerForValues {
    public static final byte TYPE_ANIM_ALPHA = 0;
    public static final byte TYPE_ANIM_SCALE_Y = 2;

    private final HashMap<String, TAlphaAnim> _alphaAnimsMap;
    private final TScaleYAnim _scaleYAnim;

    private final ListenerForView _listener;

    public AnimationManager(HashMap<String, TAlphaAnim> alphaAnimsMap,
                            TScaleYAnim scaleYAnim,
                            ListenerForView listener) {
        _alphaAnimsMap = alphaAnimsMap;
        _scaleYAnim = scaleYAnim;
        _listener = listener;

        this.initAnimsManagerListener();
    }

    @Override
    public void setNewAlpha(String keyAlphaMap, int newAlpha) {
        TAlphaAnim alphaAnim = _alphaAnimsMap.get(keyAlphaMap);
        if (alphaAnim != null) {
            alphaAnim.setNewAlpha(newAlpha);
        }
    }

    @Override
    public void setNewScaleY(int newScaleY) {
        _scaleYAnim.setNewScaleY(newScaleY);
    }

    @Override
    public void setNewAlphaAndScaleY(String keyAlphaMap, int newAlpha, int newScaleY) {
        TAlphaAnim alphaAnim = _alphaAnimsMap.get(keyAlphaMap);
        if (alphaAnim != null) {
            alphaAnim.setNewAlpha(newAlpha);
        }
        _scaleYAnim.setNewScaleY(newScaleY);
    }

    @Override
    public void animNeedInvalidate(byte animType) {
        if (_listener == null) {
            return;
        }

        if (animType == TYPE_ANIM_ALPHA) {
            _listener.animNeedInvalidateView();
        } else if (animType == TYPE_ANIM_SCALE_Y) {
            if (!getIsAlphaRun()) {
                _listener.animNeedInvalidateView();
            }
        }
    }

    private boolean getIsAlphaRun() {
        boolean isAlphaRun = false;
        if (_alphaAnimsMap.isEmpty()) {
            return isAlphaRun;
        }

        for (Map.Entry<String, TAlphaAnim> entry: _alphaAnimsMap.entrySet()) {
            if  (entry.getValue().isAnimatorRun()) {
                isAlphaRun = true;
            }
        }

        return isAlphaRun;
    }

    private void initAnimsManagerListener() {
        _scaleYAnim.setManagerListener(this);

        if (_alphaAnimsMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, TAlphaAnim> entry: _alphaAnimsMap.entrySet()) {
            entry.getValue().setManagerListener(this);
        }
    }
}
