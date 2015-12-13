package com.romens.yjkgrab.wokinterface;

import com.romens.yjkgrab.model.Order;

/**
 * Created by myq on 15-12-11.
 */
public interface CancelOrderInterface {
    void cancelOrder(Order order, ResultCallBack resultCallBack);
}
