package com.romens.yjkgrab.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.table.OrderTable;
import com.romens.yjkgrab.ui.adapter.OrderAdapter;
import com.romens.yjkgrab.ui.widget.GrabSuccessDialog;
import com.romens.yjkgrab.utils.StatusHelper;
import com.romens.yjkgrab.utils.ToastUtils;
import com.romens.yjkgrab.wokinterface.CancelOrderInterface;
import com.romens.yjkgrab.wokinterface.GrabInterface;
import com.romens.yjkgrab.wokinterface.PickUpInterface;
import com.romens.yjkgrab.wokinterface.ResultCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myq on 15-12-9.
 */
public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener, CancelOrderInterface, PickUpInterface {
    private ListView orderListView;
    private View contentView;
    private OrderAdapter adapter;
    private List<Order> data = new ArrayList<>();
    //防止重复抢单
    private boolean grabbing = false;
    protected GrabInterface grabInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        grabInterface = (GrabInterface) mActivity;
    }

    @Override
    void dealOrder(final Order order) {
        if (grabInterface != null) {
            if (grabbing) {
                grabInterface.grabing(order, new ResultCallBack() {
                    @Override
                    public void onSuccess() {
                        GrabSuccessDialog grabSuccessDialog = new GrabSuccessDialog(mActivity, order);
                        grabSuccessDialog.setOrder(order);
                        grabSuccessDialog.setCancelClick(new GrabSuccessDialog.CancelClickListener() {
                            @Override
                            public void onCancelClick() {
                                cancelOrder(order, new ResultCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        mActivity.notifyAllObservers();
                                        ToastUtils.toastMsg(mActivity, "取消成功");
                                    }

                                    @Override
                                    public void onFail() {
                                        ToastUtils.toastMsg(mActivity, "取消失败");
                                    }
                                });
                            }
                        });
                        grabSuccessDialog.setPickupClick(new GrabSuccessDialog.PickupClickListener() {
                            @Override
                            public void onPickupClick() {
                                pickUp(order, new ResultCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        mActivity.notifyAllObservers();
                                        ToastUtils.toastMsg(mActivity, "取件成功");
                                    }

                                    @Override
                                    public void onFail() {
                                        ToastUtils.toastMsg(mActivity, "取件失败");
                                    }
                                });
                            }
                        });
                        grabSuccessDialog.show();
                    }

                    @Override
                    public void onFail() {
                        ToastUtils.toastMsg(mActivity, "抢单失败");
                    }
                });
                grabbing = false;
            }
        }
    }

    @Override
    public void onDetach() {
        grabInterface = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_home, null, true);
        initViews();
        initData();
        return contentView;
    }

    private void initData() {
        notifyChanged();
    }

    private void initViews() {
        orderListView = (ListView) contentView.findViewById(R.id.order_listview);
        adapter = new OrderAdapter(data, mActivity);
        orderListView.setAdapter(adapter);
        orderListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mActivity.isWorking()) {
            final Order order = data.get(position);
            progressDialog.show();
            mOrderDetailInterface.getOrderDetail(order, new ResultCallBack() {
                @Override
                public void onSuccess() {
                    dealOrder(order);
                    progressDialog.dismiss();
                }

                @Override
                public void onFail() {

                    progressDialog.dismiss();
                    ToastUtils.toastMsg(mActivity, "抢单失败");
                }
            });
            grabbing = true;
        } else {
            ToastUtils.toastMsg(mActivity, "先上班！！！");
        }
    }

    @Override
    public void notifyChanged() {
        if (!mActivity.isGrabing())
            return;
        if (adapter == null)
            return;
        List<Order> list = mActivity.getData();
        data.clear();
        for (Order order : list) {
            if (TextUtils.equals(Constant.STATUS_WAITING_GRAB, order.getStatus())) {
                data.add(order);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void cancelOrder(Order order, ResultCallBack resultCallBack) {
        StatusHelper.update(order, resultCallBack, StatusHelper.TO_CANCEL);
    }

    @Override
    public void pickUp(Order order, ResultCallBack resultCallBack) {
        StatusHelper.update(order, resultCallBack, StatusHelper.TO_SEND);
    }

}
