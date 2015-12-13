package com.romens.yjkgrab.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.model.Shop;
import com.romens.yjkgrab.ui.widget.StatusView;
import com.romens.yjkgrab.utils.AnalysisHelper;

import java.util.List;

/**
 * Created by myq on 15-12-10.
 */
public class PickUpAdapter extends ModelAdapter<Order> {
    public static final int TYPE_PICKED = 0;
    public static final int TYPE_UNPICK = 1;
    public static final int TYPE_FINISHED = 2;
    private int adapterType;

    public PickUpAdapter(List<Order> list, Context context) {
        this(list, context, TYPE_PICKED);
    }

    public PickUpAdapter(List<Order> list, Context context, int adapterType) {
        super(list, context);
        this.adapterType = adapterType;
    }

    @Override
    protected void onBindViewHolder(ModelViewHolder viewHolder, final Order order) {
        final Shop shop = order.getShop();
        if (shop == null) {
            AVQuery<AVObject> query = AVQuery.getQuery("Shop");
            query.getInBackground(order.getShopId(), new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject != null) {
                        order.setShop(AnalysisHelper.ShopHelper.analysis(avObject));
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.shop_name.setText(shop.getName());
            holder.shop_phone.setText(shop.getPhone());
            holder.order_id.setText(order.getOrderId());
        }
    }

    @Override
    protected ModelViewHolder createViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_pick_list;
    }

    class ViewHolder extends ModelViewHolder {
        public TextView shop_name, shop_phone, order_id;

        public ViewHolder(View convertView) {
            super(convertView);
        }

        @Override
        protected void initView(View convertView) {
            if (adapterType == TYPE_UNPICK) {
                StatusView statusView = (StatusView) convertView.findViewById(R.id.status);
                statusView.setText("未取件");
            } else if (adapterType == TYPE_FINISHED) {
                StatusView statusView = (StatusView) convertView.findViewById(R.id.status);
                statusView.setText("已完结");
            }
            shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            shop_phone = (TextView) convertView.findViewById(R.id.shop_phone);
            order_id = (TextView) convertView.findViewById(R.id.order_id);
        }
    }

}
