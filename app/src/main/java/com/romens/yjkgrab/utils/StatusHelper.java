package com.romens.yjkgrab.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.table.OrderTable;
import com.romens.yjkgrab.wokinterface.ResultCallBack;

import java.util.Date;

/**
 * Created by myq on 15-12-12.
 */
public class StatusHelper {
    public static final int TO_SEND = 1;
    public static final int TO_FINISH = 2;
    public static final int TO_CANCEL = 3;

    public static void update(final Order order, final ResultCallBack resultCallBack, final int to) {
        AVQuery<AVObject> query = AVQuery.getQuery(OrderTable.Order);
        query.getInBackground(order.getObjectId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e != null) {
                    resultCallBack.onFail();
                    return;
                }
                updateInfo(avObject, order, resultCallBack, to);
            }
        });
    }

    /**
     * 更改订单信息为完成
     *
     * @param avObject
     * @param order
     * @param resultCallBack
     */
    private static void updateInfo(final AVObject avObject, final Order order, final ResultCallBack resultCallBack, final int to) {
        final Date date = new Date();
        switch (to) {
            case TO_CANCEL:
                avObject.put(OrderTable.status, Constant.STATUS_INVALID);
                break;
            case TO_SEND:
                avObject.put(OrderTable.status, Constant.STATUS_WAITING_SEND);
                avObject.put(OrderTable.pickupDate, date);
                break;
            case TO_FINISH:
                avObject.put(OrderTable.status, Constant.STATUS_FINISH);
                avObject.put(OrderTable.serviceDate, date);
                break;
            default:
                break;
        }
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    resultCallBack.onFail();
                    return;
                }
                switch (to) {
                    case TO_CANCEL:
                        order.setStatus(Constant.STATUS_INVALID);
                        break;
                    case TO_SEND:
                        order.setStatus(Constant.STATUS_WAITING_SEND);
                        order.setPickupDate(date);
                        break;
                    case TO_FINISH:
                        order.setStatus(Constant.STATUS_FINISH);
                        order.setServiceDate(date);
                        break;
                    default:
                        break;
                }
                resultCallBack.onSuccess();
            }
        });
    }

    public static void getDetail(final Order order, final ResultCallBack resultCallBack) {
        AVQuery<AVObject> query = AVQuery.getQuery(OrderTable.Order);
        query.getInBackground(order.getObjectId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e != null || avObject == null) {
                    resultCallBack.onFail();
                    return;
                }
                avObject.getAVObject(OrderTable.shop).fetchIfNeededInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (avObject != null) {
                            order.setShop(AnalysisHelper.ShopHelper.analysis(avObject));
                            success(order, resultCallBack);
                        } else {
                            resultCallBack.onFail();
                        }
                    }
                });

                avObject.getAVObject(OrderTable.customer).fetchIfNeededInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (avObject != null) {
                            order.setCustomer(AnalysisHelper.CustomHelper.analysis(avObject));
                            success(order, resultCallBack);
                        } else {
                            resultCallBack.onFail();
                        }
                    }
                });
                avObject.getAVObject(OrderTable.product).fetchIfNeededInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (avObject != null) {
                            order.setProduct(AnalysisHelper.ProductHelper.analysis(avObject));
                            success(order, resultCallBack);
                        } else {
                            resultCallBack.onFail();
                        }
                    }
                });
            }
        });
    }

    private static void success(Order order, ResultCallBack resultCallBack) {
        synchronized (StatusHelper.class) {
            if (order.isPerfect()) {
                resultCallBack.onSuccess();
            }
        }
    }
}
