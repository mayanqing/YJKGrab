package com.romens.yjkgrab.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.observer.DataObserver;
import com.romens.yjkgrab.ui.activity.HomeActivity;
import com.romens.yjkgrab.wokinterface.OrderDetailInterface;

/**
 * Created by myq on 15-12-9.
 */
public abstract class BaseFragment extends Fragment implements DataObserver {
    protected HomeActivity mActivity;
    protected ProgressDialog progressDialog;
    public OrderDetailInterface mOrderDetailInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) getActivity();
        mOrderDetailInterface = (OrderDetailInterface) mActivity;
        progressDialog = new ProgressDialog(mActivity);
    }

    @Override
    public void onPause() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPause();
    }

    /**
     * 处理业务逻辑
     *
     * @param order
     */
    abstract void dealOrder(Order order);
}
