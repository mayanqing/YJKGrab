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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.romens.yjkgrab.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by myq on 15-12-13.
 */
public class PhoneNumberDialog extends AlertDialog implements View.OnClickListener {
    private String phoneNumber, name;
    private RelativeLayout rl;
    private TextView call, send_msg, save, cancel, title;

    protected PhoneNumberDialog(Context context) {
        this(context, R.style.full_dialog);
    }

    public PhoneNumberDialog(Context context, String phoneNumber, String name) {
        this(context);
        this.phoneNumber = replaceBlank(phoneNumber);
        this.name = name;
    }

    protected PhoneNumberDialog(Context context, int themeResId) {
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
        setContentView(R.layout.full_screen_dialog_layout);
        initView();
    }

    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.rl);
        call = (TextView) findViewById(R.id.call);
        send_msg = (TextView) findViewById(R.id.send_msg);
        save = (TextView) findViewById(R.id.save);
        cancel = (TextView) findViewById(R.id.cancel);
        title = (TextView) findViewById(R.id.title);
        title.setText(phoneNumber + "\n可能是一个电话号码,你可以");
        rl.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        send_msg.setOnClickListener(this);
        call.setOnClickListener(this);
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
            case R.id.call:
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                getContext().startActivity(intentCall);
                break;
            case R.id.send_msg:
                Intent intentSms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                getContext().startActivity(intentSms);
                break;
            case R.id.save:
                Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                intent.setType(Contacts.People.CONTENT_ITEM_TYPE);
                intent.putExtra(Contacts.Intents.Insert.NAME, name);
                intent.putExtra(Contacts.Intents.Insert.PHONE, phoneNumber);
                intent.putExtra(Contacts.Intents.Insert.PHONE_TYPE, Contacts.PhonesColumns.TYPE_MOBILE);
                getContext().startActivity(intent);
                break;
            case R.id.title:
                break;
            case R.id.cancel:
                break;
            default:
                break;
        }
        dismiss();
    }
}
