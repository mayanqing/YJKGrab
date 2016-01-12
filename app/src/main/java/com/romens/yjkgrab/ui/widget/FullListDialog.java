package com.romens.yjkgrab.ui.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.romens.yjkgrab.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by myq on 15-12-13.
 */
public class FullListDialog extends AlertDialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private RelativeLayout rl;
    private TextView cancel, titleView;
    private ListView listView;
    private ListAdapter adapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private String title;

    protected FullListDialog(Context context) {
        this(context, R.style.full_dialog);
    }

    public FullListDialog(Context context, String title, ListAdapter adapter, AdapterView.OnItemClickListener onItemClickListener) {
        this(context, R.style.full_dialog);
        this.title = title;
        this.adapter = adapter;
        this.onItemClickListener = onItemClickListener;
    }

    protected FullListDialog(Context context, int themeResId) {
        super(context, themeResId);
        setOwnerActivity((Activity) context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager windowManager = getOwnerActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        this.getWindow().setAttributes(lp);
        setContentView(R.layout.full_screen_list_dialog_layout);
        initView();
    }

    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.rl);
        cancel = (TextView) findViewById(R.id.cancel);
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        listView = (ListView) findViewById(R.id.dialog_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        rl.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                break;
            case R.id.cancel:
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(parent, view, position, id);
        }
        dismiss();
    }
}
