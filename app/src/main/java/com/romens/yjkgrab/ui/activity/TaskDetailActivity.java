package com.romens.yjkgrab.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.ui.widget.ProgressView;
import com.romens.yjkgrab.ui.widget.cell.TextCell;
import com.romens.yjkgrab.utils.DateFormatHelper;
import com.romens.yjkgrab.utils.DimenHelper;
import com.romens.yjkgrab.utils.StatusHelper;
import com.romens.yjkgrab.utils.ToastUtils;
import com.romens.yjkgrab.wokinterface.FinishOrderInterface;
import com.romens.yjkgrab.wokinterface.ResultCallBack;

import java.util.HashMap;

/**
 * Created by myq on 15-12-9.
 */
public class TaskDetailActivity extends BaseDetailActivity implements FinishOrderInterface {
    private ListView list_contentview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("任务详情");
        setContentView(R.layout.activity_list_layout);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        list_contentview = (ListView) findViewById(R.id.list_contentview);
        list_contentview.setAdapter(new ContentAdapter(this));
    }

    @Override
    public void finishOrder(Order order, ResultCallBack resultCallBack) {
        StatusHelper.update(order, resultCallBack, StatusHelper.TO_FINISH);
    }

    class ContentAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public ContentAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        private Context context;

        @Override
        public int getCount() {
            return 11;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getViewType(position);
            if (viewType == 0) {
                convertView = inflater.inflate(R.layout.text_l_time_r_layout, null, true);
                TextView status = (TextView) convertView.findViewById(R.id.status);
                status.setText(TextUtils.equals(getOrder().getStatus(), "待送") ? "配送中..." : "配送完成");
                TextView date = (TextView) convertView.findViewById(R.id.date);
                date.setText(DateFormatHelper.getCuttentTime(DateFormatHelper.DATE_PARTTEN));
            } else if (viewType == 1) {
                ProgressView progressView = new ProgressView(context);
                progressView.setUnPickColor(Color.GRAY);
                progressView.setPickedColor(0xff00ffff);
                progressView.setCurrentStep(2);
                progressView.setTargetNum(4);
                progressView.setStepTextColor(Color.RED);
                progressView.setDescColor(0xff3F51B5);
                progressView.setShowDesc(true);
                progressView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DimenHelper.dp(80, context)));
                HashMap<Integer, String> map = new HashMap<>();
                map.put(1, "北京");
                map.put(2, "成都机场");
                map.put(3, "成都市区");
                map.put(4, "美年广场");
                progressView.setDescMap(map);
                convertView = progressView;
            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.product_layout, null, true);
                convertView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.frame_shallow));
                TextView product_specification = (TextView) convertView.findViewById(R.id.product_specifications);
                TextView product_name = (TextView) convertView.findViewById(R.id.product_name);
                TextView product_num = (TextView) convertView.findViewById(R.id.product_num);
                ImageView product_icon = (ImageView) convertView.findViewById(R.id.product_icon);

                product_specification.setText(getOrder().getProduct().getSpecifications() + "/" + getOrder().getProduct().getPacking());
                product_name.setText(getOrder().getProduct().getName());
                product_num.setText("x" + getOrder().getProductNum());


            } else if (viewType == 3) {
                convertView = new View(context);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DimenHelper.dp(10, context)));
                convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else if (viewType == 4) {
                TextCell textCell = null;
                convertView = textCell = new TextCell(context);
                textCell.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textCell.setTextColor(getResources().getColor(R.color.gray_black, getTheme()));
                } else {
                    textCell.setTextColor(getResources().getColor(R.color.gray_black));
                }
                textCell.setText("收货人信息");
                textCell.setTextSize(18);
                textCell.setTextViewPadding(20, 10, 0, 10);
                textCell.setBackgroundColor(getResources().getColor(R.color.not_trans));
            } else if (viewType == 5) {
                convertView = inflater.inflate(R.layout.two_text_layout, null, true);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView content = (TextView) convertView.findViewById(R.id.content);
                TextView look_map = (TextView) convertView.findViewById(R.id.look_map);
                switch (position) {
                    case 6:
                        look_map.setVisibility(View.GONE);
                        name.setText("收货人 : ");
                        content.setText(getOrder().getCustomer().getName());
                        break;
                    case 7:
                        look_map.setVisibility(View.GONE);
                        name.setText("联系电话 : ");
                        content.setText(getOrder().getCustomer().getPhone());
                        regitsterPhoneClickListener(content, getOrder().getCustomer().getName());
                        break;
                    case 8:
                        name.setText("配送地址 : ");
                        content.setText(getOrder().getCustomer().getAddress());
                        if (TextUtils.equals(getOrder().getStatus(), "待送")) {
                            look_map.setVisibility(View.VISIBLE);
                            look_map.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(context, "目前不支持哈。。。", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case 9:
                        look_map.setVisibility(View.GONE);
                        name.setText("送达时间 : ");
                        content.setText(DateFormatHelper.formatDate(getOrder().getExpectDate()));
                        break;
                    default:
                        break;
                }
            } else if (viewType == 6) {
                convertView = inflater.inflate(R.layout.one_button_layout, null, true);
                Button income_btn = (Button) convertView.findViewById(R.id.income_btn);

                if (TextUtils.equals(getOrder().getStatus(), Constant.STATUS_WAITING_SEND)) {
                    income_btn.setText("确认送达");
                    income_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finishOrder(getOrder(), new ResultCallBack() {
                                @Override
                                public void onSuccess() {
                                    ToastUtils.toastMsg(TaskDetailActivity.this, "送达成功");
                                    setResult(RESULT_OK);
                                    finish();
                                }

                                @Override
                                public void onFail() {
                                    ToastUtils.toastMsg(TaskDetailActivity.this, "送达失败");
                                }
                            });
                        }
                    });
                } else {
                    income_btn.setText("本次收入");
                    income_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.toastMsg(TaskDetailActivity.this, "哇！好多钱钱！！");
                        }
                    });
                }


            } else if (viewType == 7) {
                convertView = new View(context);
                convertView.setBackgroundColor(getResources().getColor(R.color.not_trans));
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DimenHelper.dp(0.5f, context)));
            }
            return convertView;
        }

        @Override
        public boolean isEnabled(int position) {
            return super.isEnabled(position);
        }

        private int getViewType(int position) {
            if (position == 0) {
                return 0;
            } else if (position == 1) {
                return 1;
            } else if (position == 3) {
                return 2;
            } else if (position == 4) {
                return 3;
            } else if (position == 5) {
                return 4;
            } else if (position >= 6 && position <= 9) {
                return 5;
            } else if (position == 10) {
                return 6;
            } else if (position == 2) {
                return 7;
            }
            return 0;
        }
    }
}
