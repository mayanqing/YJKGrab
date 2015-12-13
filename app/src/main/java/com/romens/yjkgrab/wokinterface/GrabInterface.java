package com.romens.yjkgrab.wokinterface;

import com.romens.yjkgrab.model.Order;

/**
 * Created by myq on 15-12-11.
 */
public interface GrabInterface {
    void grabing(Order order,ResultCallBack resultCallBack);
}
