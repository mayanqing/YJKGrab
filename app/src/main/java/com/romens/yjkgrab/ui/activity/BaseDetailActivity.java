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

/**
 * Created by myq on 15-12-11.
 */
public abstract class BaseDetailActivity extends BaseActivity {
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                new AlertDialog.Builder(BaseDetailActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                        .setAdapter(new ArrayAdapter<String>(BaseDetailActivity.this, android.R.layout.simple_list_item_1, new String[]{"发送短信", "拨打电话", "添加到通讯录"})
                                , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intentSms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                                        startActivity(intentSms);
                                        break;
                                    case 1:
                                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                        startActivity(intentCall);
                                        break;
                                    case 2:
                                        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                                        intent.setType(Contacts.People.CONTENT_ITEM_TYPE);
                                        intent.putExtra(Contacts.Intents.Insert.NAME, name);
                                        intent.putExtra(Contacts.Intents.Insert.PHONE, phone);
                                        intent.putExtra(Contacts.Intents.Insert.PHONE_TYPE, Contacts.PhonesColumns.TYPE_MOBILE);
                                        startActivity(intent);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
            }
        });
    }
}
