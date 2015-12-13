package com.romens.yjkgrab.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by myq on 15-12-1.
 */
public abstract class ModelAdapter<T> extends BaseAdapter {
    private List<T> list;

    public Context getContext() {
        return context;
    }

    private Context context;
    private LayoutInflater inflater;

    public ModelAdapter(List<T> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModelViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutResId(), null, true);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ModelViewHolder) convertView.getTag();
        }
        onBindViewHolder(position, viewHolder);
        return convertView;
    }

    protected void onBindViewHolder(int position, ModelViewHolder viewHolder) {
        T t = list.get(position);
        onBindViewHolder(viewHolder, t);
    }

    protected abstract void onBindViewHolder(ModelViewHolder viewHolder, T t);

    protected abstract ModelViewHolder createViewHolder(View convertView);

    protected abstract int getLayoutResId();

    public abstract class ModelViewHolder {
        public ModelViewHolder(View convertView) {
            initView(convertView);
        }

        protected abstract void initView(View convertView);
    }
}
