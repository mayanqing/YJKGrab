package com.romens.yjkgrab.ui;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.ui.activity.YJKActivity;
import com.romens.yjkgrab.utils.AVOSCloudHelper;

import java.util.ArrayList;

/**
 * Created by myq on 15-12-7.
 */
public class GrabApplication extends Application {

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    private String installationId;

    public ArrayList<Order> getData() {
        return data;
    }

    public void setData(ArrayList<Order> data) {
        this.data = data;
    }

    private ArrayList<Order> data = new ArrayList<>();

    public Order queryOrderById(String objectId) {
        for (Order order : data) {
            if (TextUtils.equals(objectId, order.getObjectId())) {
                return order;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloudHelper.init(this);
        //// 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(this, "public", YJKActivity.class);
        //第一个参数是当前的 context，第二个参数是频道名称，第三个参数是回调对象的类，回调对象是指用户点击通知栏的通知进入的 Activity 页面。
        //PushService.subscribe(this, "private", Callback1.class);
        // PushService.subscribe(this, "protected", Callback2.class);
        //退订频道也很简单：
        //PushService.unsubscribe(context, "protected");
        //退订之后需要重新保存 Installation
        //AVInstallation.getCurrentInstallation().saveInBackground();
        //你可以通过以下代码保存你的 Installation id。如果你的系统之前还没有 Installation id, 系统会为你自动生成一个。如果你的应用卸载后，Installation id也将会被删除
        // 设置默认打开的 Activity
        PushService.setDefaultPushCallback(this, YJKActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    setInstallationId(AVInstallation.getCurrentInstallation().getInstallationId());
                    // 关联  installationId 到用户表等操作……
                    Log.i(">>>>>>>>>>>>", "installationId=" + installationId);
                } else {
                    // 保存失败，输出错误信息
                    e.printStackTrace();
                }
            }
        });

    }
}
