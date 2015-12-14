package com.romens.yjkgrab.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.model.Product;
import com.romens.yjkgrab.model.Shop;
import com.romens.yjkgrab.utils.AnalysisHelper;

import java.util.List;

/**
 * Created by myq on 15-12-10.
 */
public class OrderAdapter extends ModelAdapter<Order> {
    private AdapterView.OnItemClickListener onItemClickListener;

    protected OrderAdapter(List<Order> list, Context context) {
        this(list, context, null);
    }

    public OrderAdapter(List<Order> list, Context context, AdapterView.OnItemClickListener onItemClickListener) {
        super(list, context);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void onBindViewHolder(ModelViewHolder viewHolder, final Order order) {
        final Shop shop = order.getShop();
        ViewHolder holder = (ViewHolder) viewHolder;
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
            holder.shop_adress.setText(shop.getAddress());
            holder.shop_name.setText(shop.getName());
            holder.shop_phone.setText(shop.getPhone());
            holder.distance.setText(order.getDistance() + "km");
        }
    }

    @Override
    protected void onBindViewHolder(final int position, ModelViewHolder viewHolder, final ViewGroup parent) {
        super.onBindViewHolder(position, viewHolder, parent);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.one_key_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick((AdapterView<?>) parent, v, position, getItemId(position));
            }
        });
    }

    @Override
    protected ModelViewHolder createViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_order_list;
    }

    class ViewHolder extends ModelViewHolder {
        public TextView shop_name, shop_adress, shop_phone, distance, one_key_pick;

        public ViewHolder(View convertView) {
            super(convertView);
        }

        @Override
        protected void initView(View convertView) {
            shop_name = (TextView) convertView.findViewById(R.id.shop_name);
            shop_adress = (TextView) convertView.findViewById(R.id.shop_adress);
            shop_phone = (TextView) convertView.findViewById(R.id.shop_phone);
            distance = (TextView) convertView.findViewById(R.id.distance);
            one_key_pick = (TextView) convertView.findViewById(R.id.one_key_pick);
        }
    }

}
