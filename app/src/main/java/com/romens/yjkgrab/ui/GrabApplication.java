package com.romens.yjkgrab.ui;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.ui.activity.MainActivity;
import com.romens.yjkgrab.utils.AVOSCloudHelper;

/**
 * Created by myq on 15-12-7.
 */
public class GrabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloudHelper.init(this);


        //// 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(this, "public", MainActivity.class);
        //第一个参数是当前的 context，第二个参数是频道名称，第三个参数是回调对象的类，回调对象是指用户点击通知栏的通知进入的 Activity 页面。
        //PushService.subscribe(this, "private", Callback1.class);
        // PushService.subscribe(this, "protected", Callback2.class);


        //退订频道也很简单：

        //PushService.unsubscribe(context, "protected");
        //退订之后需要重新保存 Installation
        //AVInstallation.getCurrentInstallation().saveInBackground();
        //你可以通过以下代码保存你的 Installation id。如果你的系统之前还没有 Installation id, 系统会为你自动生成一个。如果你的应用卸载后，Installation id也将会被删除


        // 设置默认打开的 Activity
        PushService.setDefaultPushCallback(this, MainActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
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
