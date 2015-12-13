package com.romens.yjkgrab.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.Set;

import javax.security.auth.callback.Callback;

/**
 * Created by myq on 15-12-7.
 */
public class AvosNetworkHelper {
    /**
     * 要保存 Post 数据到云端，添加属性的方法与 Java 中的 Map 类似
     *
     * @param parmas
     */
    public static void doPost(HashMap<String, Object> parmas, SaveCallback saveCallback) {
        AVObject post = new AVObject("Post");
        Set<Map.Entry<String, Object>> set = parmas.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        for (; iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            post.put(entry.getKey(), entry.getValue());
        }
//        post.put("content", "每个Java程序员必备的8个开发工具");
//        post.put("pubUser", "LeanCloud官方客服");
//        post.put("pubTimestamp", 1435541999);
        try {
            post.saveInBackground(saveCallback);
            post.save();//同步
            //post.saveInBackground();//异歩版本
        } catch (AVException e) {
            // e.getMessage() 捕获的异常信息
        }
    }


    public static String serAVObject(AVObject avObject) {
        return avObject.toString();
    }

    public static AVObject parseAvObject(String avobjStr) throws Exception {
        return AVObject.parseAVObject(avobjStr);
    }
}
