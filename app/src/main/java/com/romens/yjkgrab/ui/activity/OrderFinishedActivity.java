package com.romens.yjkgrab.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.ui.GrabApplication;
import com.romens.yjkgrab.ui.adapter.PickUpAdapter;
import com.romens.yjkgrab.utils.StatusHelper;
import com.romens.yjkgrab.utils.ToastUtils;
import com.romens.yjkgrab.wokinterface.OrderDetailInterface;
import com.romens.yjkgrab.wokinterface.ResultCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myq on 15-12-12.
 */
public class OrderFinishedActivity extends BaseActivity implements AdapterView.OnItemClickListener, OrderDetailInterface {
    private ListView list_contentview;
    private PickUpAdapter finishedAdapter;
    private List<Order> data = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("完结订单");
        setContentView(R.layout.activity_list_layout);
        progressDialog = new ProgressDialog(this);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        list_contentview = (ListView) findViewById(R.id.list_contentview);
        finishedAdapter = new PickUpAdapter(data, this, PickUpAdapter.TYPE_FINISHED);
        list_contentview.setAdapter(finishedAdapter);
        list_contentview.setOnItemClickListener(this);
    }

    private void initData() {
        List<Order> list = application.getData();
        if (list == null || list.size() == 0) {
            ToastUtils.toastMsg(this, "无已完结订单" + list.size());
            return;
        }
        data.clear();
        for (Order order : list) {
//            if (!TextUtils.equals(order.getInstallationId(), application.getInstallationId()))
//                continue;
            if (TextUtils.equals(Constant.STATUS_FINISH, order.getStatus())) {
                data.add(order);
            }
        }
        if (data.size() == 0) {
            ToastUtils.toastMsg(this, "无已完结订单");
            return;
        } else {
            finishedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        progressDialog.show();
        getOrderDetail(data.get(position), new ResultCallBack() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Intent toOrderIntent = new Intent(OrderFinishedActivity.this, TaskDetailActivity.class);
                toOrderIntent.putExtra(Constant.OBJECT_ID, data.get(position).getObjectId());
                startActivity(toOrderIntent);
            }

            @Override
            public void onFail() {
                progressDialog.dismiss();
                ToastUtils.toastMsg(OrderFinishedActivity.this, "获取订单详情失败");
            }
        });
    }

    @Override
    public void getOrderDetail(Order order, ResultCallBack resultCallBack) {
        if (order.isPerfect()) {
            resultCallBack.onSuccess();
            return;
        }
        StatusHelper.getDetail(order, resultCallBack);
    }
}

