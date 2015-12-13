package com.romens.yjkgrab.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.romens.yjkgrab.ui.GrabApplication;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    protected GrabApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (GrabApplication) getApplication();
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackMenuCLick();
        }
        return super.onOptionsItemSelected(item);
    }

    public GrabApplication getGrabApplication() {
        return application;
    }

    protected void onBackMenuCLick() {
        finish();
    }
}
