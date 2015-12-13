package com.romens.yjkgrab.ui.fragment;

import android.app.Activity;
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
import android.widget.RelativeLayout;

import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.ui.activity.OrderDetailActivity;
import com.romens.yjkgrab.ui.activity.TaskDetailActivity;
import com.romens.yjkgrab.ui.adapter.PickUpAdapter;
import com.romens.yjkgrab.ui.widget.SelectView;
import com.romens.yjkgrab.utils.ToastUtils;
import com.romens.yjkgrab.wokinterface.ResultCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myq on 15-12-9.
 */
public class PickFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View contentView;
    private RelativeLayout tabUnPick, tabPicked;
    private SelectView selectViewUnPick, selectPicked;
    private ListView pickupListview;
    private PickUpAdapter pickedadapter;
    private List<Order> pickedData = new ArrayList<>();
    private List<Order> unPickedData = new ArrayList<>();
    private PickUpAdapter unPickAdapter;
    private int currentPage;
    private int pickedRequestCode = 1, unPickRequestCode = 2;
    private boolean dealing;
    private Order currentOrder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_order, null, true);
        initViews();
        initData();
        return contentView;
    }

    private void initData() {
        notifyChanged();
    }

    private void initViews() {
        tabUnPick = (RelativeLayout) contentView.findViewById(R.id.tab_un_pick);
        tabPicked = (RelativeLayout) contentView.findViewById(R.id.tab_picked);
        tabUnPick.setOnClickListener(this);
        tabPicked.setOnClickListener(this);
        selectViewUnPick = (SelectView) contentView.findViewById(R.id.select_un_pick);
        selectViewUnPick.setSelected(true);
        currentPage = 1;
        selectPicked = (SelectView) contentView.findViewById(R.id.select__picked);
        selectPicked.setSelected(false);
        pickupListview = (ListView) contentView.findViewById(R.id.pickup_listview);
        pickedadapter = new PickUpAdapter(pickedData, mActivity);
        pickupListview.setAdapter(pickedadapter);
        pickupListview.setOnItemClickListener(this);
        unPickAdapter = new PickUpAdapter(unPickedData, mActivity, PickUpAdapter.TYPE_UNPICK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_un_pick:
                selectPicked.setIsSelectPick(true);
                selectViewUnPick.setIsSelectPick(false);
                pickupListview.setAdapter(unPickAdapter);
                currentPage = 2;
                break;
            case R.id.tab_picked:
                currentPage = 1;
                selectViewUnPick.setIsSelectPick(true);
                selectPicked.setIsSelectPick(false);
                pickupListview.setAdapter(pickedadapter);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Order order = currentPage == 1 ? pickedData.get(position) : currentPage == 2 ? unPickedData.get(position) : null;
        currentOrder = order;
        progressDialog.show();
        mOrderDetailInterface.getOrderDetail(order, new ResultCallBack() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                dealOrder(order);
            }

            @Override
            public void onFail() {
                progressDialog.dismiss();
                ToastUtils.toastMsg(mActivity, "获取订单详细信息失败！");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    synchronized void dealOrder(Order order) {
        if (currentOrder == order) {
            currentOrder = null;
            toDetailActivity(order);
        }
    }


    private void toDetailActivity(Order order) {
        switch (currentPage) {
            case 1:
                Intent toTaskIntent = new Intent(mActivity, TaskDetailActivity.class);
                toTaskIntent.putExtra(Constant.OBJECT_ID, order.getObjectId());
                startActivityForResult(toTaskIntent, pickedRequestCode);
                break;
            case 2:
                Intent toOrderIntent = new Intent(mActivity, OrderDetailActivity.class);
                toOrderIntent.putExtra(Constant.OBJECT_ID, order.getObjectId());
                startActivityForResult(toOrderIntent, unPickRequestCode);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            notifyChanged();
        }
    }

    @Override
    public void notifyChanged() {
        if (currentPage == 0)
            return;
        List<Order> data = mActivity.getData();
        pickedData.clear();
        unPickedData.clear();
        for (Order order : data) {
            if (!TextUtils.equals(order.getInstallationId(), mActivity.getGrabApplication().getInstallationId()))
                continue;
            if (TextUtils.equals(Constant.STATUS_WAITING_SEND, order.getStatus())) {
                pickedData.add(order);
            }
            if (TextUtils.equals(Constant.STATUS_WAITING_TAKE, order.getStatus())) {
                unPickedData.add(order);
            }
        }
        if (currentPage == 1)
            pickedadapter.notifyDataSetChanged();
        else if (currentPage == 2)
            unPickAdapter.notifyDataSetChanged();
    }
}
