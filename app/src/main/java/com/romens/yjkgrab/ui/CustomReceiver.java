package com.romens.yjkgrab.ui;

import com.avos.avoscloud.AVOSCloud;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.ui.activity.HomeActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

public class CustomReceiver extends BroadcastReceiver {
    public static final String PUSH_ACTION = "com.romens.yjkgrab.action";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("action",intent.getAction());
            if (intent.getAction().equals(PUSH_ACTION)) {
                if (HomeActivity.isForeground()) {
                    Log.i("action","sendBroadCast");
                    context.sendBroadcast(new Intent(HomeActivity.NEW_ORDER_ACTION));
                    return;
                }
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                final String message = json.getString("alert");
                String title = json.getString("title");
                Intent resultIntent = new Intent(AVOSCloud.applicationContext, HomeActivity.class);
                resultIntent.putExtra("message", message);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(AVOSCloud.applicationContext)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(
                                        title)
                                .setContentText(message)
                                .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);
                mBuilder.setVibrate(new long[]{0, 300, 500, 700});
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                int mNotificationId = 10086;

                NotificationManager mNotifyMgr =
                        (NotificationManager) AVOSCloud.applicationContext
                                .getSystemService(
                                        Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                Log.i("action", "sendNotify");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
