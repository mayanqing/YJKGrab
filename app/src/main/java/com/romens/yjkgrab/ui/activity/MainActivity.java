package com.romens.yjkgrab.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.utils.AVOSCloudHelper;
import com.romens.yjkgrab.utils.AvosNetworkHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity", getIntent().getStringExtra("message")+"");
//        AVOSCloudHelper.test();
    }
}
