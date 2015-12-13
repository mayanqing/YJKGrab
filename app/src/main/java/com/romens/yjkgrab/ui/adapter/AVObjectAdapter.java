package com.romens.yjkgrab.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.romens.yjkgrab.R;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by myq on 15-12-8.
 */
public class AVObjectAdapter extends ModelAdapter<AVObject> {
    public AVObjectAdapter(List<AVObject> list, Context context) {
        super(list, context);
    }

    @Override
    protected void onBindViewHolder(ModelViewHolder viewHolder, AVObject avObject) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.title.setText(avObject.getObjectId());
//        JSONObject jsonObject = avObject.getJSONObject("serverData");
        holder.content.setText(avObject.get("words").toString() + "------" + avObject.getString("adress"));
    }

    @Override
    protected ModelViewHolder createViewHolder(View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_grab_layout;
    }

    class ViewHolder extends ModelViewHolder {
        public ImageView icon;
        public TextView title, content;

        public ViewHolder(View convertView) {
            super(convertView);
        }

        @Override
        protected void initView(View convertView) {
            icon = (ImageView) convertView.findViewById(R.id.grab);
            title = (TextView) convertView.findViewById(R.id.title);
            content = (TextView) convertView.findViewById(R.id.content);
        }
    }
}
