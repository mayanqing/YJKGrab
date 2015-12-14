package com.romens.yjkgrab.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.utils.DateFormatHelper;

/**
 * Created by myq on 15-12-10.
 */
public class GrabSuccessDialog extends AlertDialog implements View.OnClickListener {

    public GrabSuccessDialog(Context context, Order order) {
        this(context, 0);
        setOrder(order);
    }

    public GrabSuccessDialog(Context context, CancelClickListener cancelClick, PickupClickListener pickupClick) {
        this(context, 0);
        this.cancelClick = cancelClick;
        this.pickupClick = pickupClick;
    }

    private CancelClickListener cancelClick;
    private PickupClickListener pickupClick;
    private TextView order_id, shop_name_detail, shop_adress_detail, customer_name, customer_adrress_detail, customer_phone_num, remarks, pick_up_date, cancel_order;
//    pcik_up_product;
    private Order order;

    public GrabSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected GrabSuccessDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grab_success);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        init();
        initOrderInfo(order);
    }

    private void init() {
        order_id = (TextView) findViewById(R.id.order_id);
        shop_name_detail = (TextView) findViewById(R.id.shop_name_detail);
        shop_adress_detail = (TextView) findViewById(R.id.shop_adress_detail);
        customer_name = (TextView) findViewById(R.id.customer_name);
        customer_adrress_detail = (TextView) findViewById(R.id.customer_adrress_detail);
        customer_phone_num = (TextView) findViewById(R.id.customer_phone_num);
        pick_up_date = (TextView) findViewById(R.id.pick_up_date);
        cancel_order = (TextView) findViewById(R.id.cancel_order);
//        pcik_up_product = (TextView) findViewById(R.id.pcik_up_product);
        cancel_order.setOnClickListener(this);
//        pcik_up_product.setOnClickListener(this);
    }

    private void initOrderInfo(Order order) {
        order_id.setText(order.getOrderId());
        shop_name_detail.setText(order.getShop().getName());
        shop_adress_detail.setText(order.getShop().getAddress());
        customer_name.setText(order.getCustomer().getName());
        customer_adrress_detail.setText(order.getCustomer().getAddress());
        customer_phone_num.setText(order.getCustomer().getPhone());
        pick_up_date.setText(DateFormatHelper.formatDate(DateFormatHelper.DATE_PARTTEN, order.getPickupDate()));
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.cancel_order:
//                if (cancelClick != null)
//                    cancelClick.onCancelClick();
//                break;
//            case R.id.pcik_up_product:
//                if (pickupClick != null)
//                    pickupClick.onPickupClick();
//                break;
//            default:
//                break;
//        }
        dismiss();
    }

    public interface CancelClickListener {
        void onCancelClick();
    }

    public interface PickupClickListener {
        void onPickupClick();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCancelClick(CancelClickListener cancelClick) {
        this.cancelClick = cancelClick;
    }

    public void setPickupClick(PickupClickListener pickupClick) {
        this.pickupClick = pickupClick;
    }
}
