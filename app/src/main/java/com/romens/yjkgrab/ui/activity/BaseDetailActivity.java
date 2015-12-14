package com.romens.yjkgrab.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Customer;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.model.Product;
import com.romens.yjkgrab.model.Shop;
import com.romens.yjkgrab.ui.GrabApplication;
import com.romens.yjkgrab.ui.widget.PhoneNumberDialog;

/**
 * Created by myq on 15-12-11.
 */
public abstract class BaseDetailActivity extends BaseActivity {
    private Order order;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
    }


    protected Order getOrder() {
        if (order != null)
            return order;
        String objectId = getIntent().getStringExtra(Constant.OBJECT_ID);
        if (objectId != null) {
            GrabApplication grabApplication = (GrabApplication) getApplication();
            order = grabApplication.queryOrderById(objectId);
        }
        Log.i(">>>>>", order == null ? "noOrder" : "" + order.isPerfect());
        return order == null ? new Order().setCustomer(new Customer()).setProduct(new Product()).setShop(new Shop()) : order;
    }

    protected void regitsterPhoneClickListener(TextView textView, final String name) {
        textView.setTextColor(getResources().getColor(R.color.look_map_color));
        final String phone = textView.getText().toString();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhoneNumberDialog(BaseDetailActivity.this, phone, name).show();
            }
        });
    }
}
