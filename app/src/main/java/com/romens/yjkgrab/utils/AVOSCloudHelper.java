package com.romens.yjkgrab.utils;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.R;

/**
 * Created by myq on 15-12-7.
 */
public class AVOSCloudHelper {

    public static void init(Context appContext){
        AVOSCloud.initialize(appContext, appContext.getString(R.string.app_id), appContext.getString(R.string.app_key));
    }

    public static void test(){
        // 测试 SDK 是否正常工作的代码
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words", "Hello,World!");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.d("saved", "success!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
