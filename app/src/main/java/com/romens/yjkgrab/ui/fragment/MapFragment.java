package com.romens.yjkgrab.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;

/**
 * Created by myq on 15-12-9.
 */
public class MapFragment extends BaseFragment {
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_maps, null, true);
        initViews();
        return contentView;
    }

    private void initViews() {

    }

    @Override
    public void notifyChanged() {

    }

    @Override
    void dealOrder(Order order) {

    }
}
