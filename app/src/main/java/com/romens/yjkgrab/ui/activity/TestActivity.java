package com.romens.yjkgrab.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.romens.yjkgrab.R;
import com.romens.yjkgrab.ui.widget.FullListDialog;
import com.romens.yjkgrab.ui.widget.ProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TestActivity extends BaseActivity implements View.OnClickListener {
    private Random r = new Random();
    private ProgressView progressView;
    private HashMap<Integer, String> timeMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        getSupportActionBar().setTitle("进度条测试");
        init();
    }

    private void init() {
        progressView = (ProgressView) findViewById(R.id.progressView);
        for (int i = 0; i < 10; i++) {
            timeMap.put(i + 1, "1" + i + ":00");
        }
        progressView.setDescMap(timeMap);
        Button step = (Button) findViewById(R.id.step);
        step.setOnClickListener(this);
        Button pick_color = (Button) findViewById(R.id.pick_color);
        pick_color.setOnClickListener(this);
        Button show_time = (Button) findViewById(R.id.show_time);
        show_time.setOnClickListener(this);
        Button current_step = (Button) findViewById(R.id.current_step);
        current_step.setOnClickListener(this);
        Button time_color = (Button) findViewById(R.id.time_color);
        time_color.setOnClickListener(this);
        Button text_color = (Button) findViewById(R.id.text_color);
        text_color.setOnClickListener(this);
        Button un_pick_color = (Button) findViewById(R.id.un_pick_color);
        un_pick_color.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step:
                progressView.setTargetNum(Math.abs(r.nextInt(6) + 1));
                break;
            case R.id.pick_color:
                progressView.setPickedColor(Color.rgb(Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255))));
                break;
            case R.id.show_time:
                progressView.setShowDesc(!progressView.isShowDesc());
                break;
            case R.id.current_step:
                progressView.setCurrentStep(Math.abs(r.nextInt(progressView.getTargetNum())));
                break;
            case R.id.time_color:
                progressView.setDescColor(Color.rgb(Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255))));
                break;
            case R.id.text_color:
                progressView.setStepTextColor(Color.rgb(Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255))));
                break;
            case R.id.un_pick_color:
                showListDialog();
//                progressView.setUnPickColor(Color.rgb(Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255)), Math.abs(r.nextInt(255))));
                break;
            default:
                break;
        }
    }

    private void showListDialog() {
        final ArrayList<String> arrs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            arrs.add("item" + i);
        }
        String title="11111222";
        FullListDialog fullListDialog = new FullListDialog(this,title, new ArrayAdapter<String>(this, R.layout.dialog_textview, arrs), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TestActivity.this, arrs.get(position), Toast.LENGTH_SHORT).show();
            }
        });
//        fullListDialog.setTitle("没没没没沒");
        fullListDialog.show();
    }
}
