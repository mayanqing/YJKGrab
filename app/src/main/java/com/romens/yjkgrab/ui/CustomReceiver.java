package com.romens.yjkgrab.ui;

import com.avos.avoscloud.AVOSCloud;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.ui.activity.MainActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONObject;

public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("-----------------", intent.getAction() + "");
        try {
            if (intent.getAction().equals("com.romens.yjkgrab.action")) {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                final String message = json.getString("alert");
                Intent resultIntent = new Intent(AVOSCloud.applicationContext, MainActivity.class);
                resultIntent.putExtra("message", message);
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(AVOSCloud.applicationContext)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(
                                        AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                                .setContentText(message)
                                .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);

                int mNotificationId = 10086;
                NotificationManager mNotifyMgr =
                        (NotificationManager) AVOSCloud.applicationContext
                                .getSystemService(
                                        Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                Log.i(">>>>>>>>>>>>>>>>", message);
            }
        } catch (Exception e) {

        }
    }
}