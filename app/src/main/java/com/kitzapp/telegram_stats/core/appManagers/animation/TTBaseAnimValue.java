package com.kitzapp.telegram_stats.core.appManagers.animation;

/**
 * Created by Ivan Kuzmin on 15.04.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
interface TTBaseAnimValueInterface {

    void setManagerListener(AnimationManagerInterface.ListenerForValues listenerForManage);

}

abstract class TTBaseAnimValue implements TTBaseAnimValueInterface {
}
