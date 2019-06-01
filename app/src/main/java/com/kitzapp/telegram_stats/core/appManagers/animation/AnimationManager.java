package com.kitzapp.telegram_stats.core.appManagers.animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan Kuzmin on 12.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class AnimationManager implements AnimationManagerInterface, AnimationManagerInterface.ListenerForValues {

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
    public void setNewScaleY(float oldScaleY, float newScaleY) {
        _scaleYAnim.setNewScaleY(oldScaleY, newScaleY);
    }

    @Override
    public void animNeedInvalidate() {
        if (_listener == null) {
            return;
        }

        _listener.animNeedInvalidateView();
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
