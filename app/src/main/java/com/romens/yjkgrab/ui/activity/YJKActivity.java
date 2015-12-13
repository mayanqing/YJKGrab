package com.romens.yjkgrab.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.ui.adapter.AVObjectAdapter;
import com.romens.yjkgrab.ui.resideMenu.MenuActivity;
import com.romens.yjkgrab.ui.widget.reddotface.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class YJKActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView listview;
    private AVObjectAdapter adapter;
    private List<AVObject> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yjk_main);
        Log.i("MainActivity", getIntent().getStringExtra("message") + "");
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {

        listview = (ListView) findViewById(R.id.listview);
        adapter = new AVObjectAdapter(data, this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        AVQuery<AVObject> query = AVQuery.getQuery("TestObject");
//        query.whereEqualTo("state", false);
        query.whereWithinKilometers("location", new AVGeoPoint(31.53, 104.56), 200);
//        query.whereWithinGeoBox("location", new AVGeoPoint(30.03, 103.06), new AVGeoPoint(31.53, 104.56));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e) {
                    Log.i(">>>", "查询成功");
                } else {
                    Log.i(">>>", "查询失败");
                }

                if (list != null) {
                    data.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, MenuActivity.class));
                break;
            default:
                break;
        }

    }
}
