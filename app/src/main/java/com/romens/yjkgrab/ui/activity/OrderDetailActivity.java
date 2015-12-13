package com.romens.yjkgrab.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.table.OrderTable;
import com.romens.yjkgrab.ui.fragment.PickFragment;
import com.romens.yjkgrab.ui.widget.ProgressView;
import com.romens.yjkgrab.ui.widget.cell.TextCell;
import com.romens.yjkgrab.utils.DateFormatHelper;
import com.romens.yjkgrab.utils.StatusHelper;
import com.romens.yjkgrab.utils.ToastUtils;
import com.romens.yjkgrab.wokinterface.CancelOrderInterface;
import com.romens.yjkgrab.wokinterface.FinishOrderInterface;
import com.romens.yjkgrab.wokinterface.PickUpInterface;
import com.romens.yjkgrab.wokinterface.ResultCallBack;

public class OrderDetailActivity extends BaseDetailActivity implements CancelOrderInterface, PickUpInterface {
    private ListView list_contentview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("订单详情");
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
    public void cancelOrder(Order order, ResultCallBack resultCallBack) {
        StatusHelper.update(order, resultCallBack, StatusHelper.TO_CANCEL);
    }

    @Override
    public void pickUp(Order order, ResultCallBack resultCallBack) {
        StatusHelper.update(order, resultCallBack, StatusHelper.TO_SEND);
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
            return 8;
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
                TextCell textCell = null;
                convertView = textCell = new TextCell(context);
                textCell.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textCell.setTextColor(getResources().getColor(R.color.gray_black, getTheme()));
                } else {
                    textCell.setTextColor(getResources().getColor(R.color.gray_black));
                }
                if (position == 0) {
                    textCell.setText("商品详情");
                } else {
                    textCell.setText("收货人信息");
                }
                textCell.setTextSize(18);
                textCell.setTextViewPadding(20, 10, 0, 10);
                textCell.setBackgroundColor(getResources().getColor(R.color.back_color));
            } else if (viewType == 1) {
                convertView = inflater.inflate(R.layout.product_detail, null, true);
                //product
                TextView product_specification = (TextView) convertView.findViewById(R.id.product_specifications);
                TextView product_name = (TextView) convertView.findViewById(R.id.product_name);
                TextView product_num = (TextView) convertView.findViewById(R.id.product_num);
                ImageView product_icon = (ImageView) convertView.findViewById(R.id.product_icon);

                product_specification.setText(getOrder().getProduct().getSpecifications() + "/" + getOrder().getProduct().getPacking());
                product_name.setText(getOrder().getProduct().getName());
                product_num.setText("x" + getOrder().getProductNum());
                //shop
                TextView order_pick_up_date = (TextView) convertView.findViewById(R.id.order_pick_up_date);
                TextView shop_adress = (TextView) convertView.findViewById(R.id.shop_adress);
                TextView shop_phone = (TextView) convertView.findViewById(R.id.shop_phone);
                shop_phone.setText(getOrder().getShop().getPhone());
                regitsterPhoneClickListener(shop_phone, getOrder().getShop().getName());
                shop_adress.setText(getOrder().getShop().getAddress());
                order_pick_up_date.setText(DateFormatHelper.formatDate(getOrder().getPickupDate()));
                //order
                TextView order_id = (TextView) convertView.findViewById(R.id.order_id);
                order_id.setText(getOrder().getOrderId());

            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.two_text_layout, null, true);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView content = (TextView) convertView.findViewById(R.id.content);
                switch (position) {
                    case 3:
                        name.setText("收货人 : ");
                        content.setText(getOrder().getCustomer().getName());
                        break;
                    case 4:
                        name.setText("联系电话 : ");
                        content.setText(getOrder().getCustomer().getPhone());
                        regitsterPhoneClickListener(content, getOrder().getCustomer().getName());
                        break;
                    case 5:
                        name.setText("配送地址 : ");
                        content.setText(getOrder().getCustomer().getAddress());
                        break;
                    case 6:
                        name.setText("送达时间 : ");
                        content.setText(DateFormatHelper.formatDate(getOrder().getExpectDate()));
                        break;
                    default:
                        break;
                }
            } else if (viewType == 3) {
                convertView = inflater.inflate(R.layout.two_button_layout, null, true);
                convertView.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(getOrder(), new ResultCallBack() {
                            @Override
                            public void onSuccess() {
                                ToastUtils.toastMsg(OrderDetailActivity.this, "取消成功");
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFail() {
                                ToastUtils.toastMsg(OrderDetailActivity.this, "取消失败");
                            }
                        });
                    }
                });
                convertView.findViewById(R.id.confrim_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickUp(getOrder(), new ResultCallBack() {
                            @Override
                            public void onSuccess() {
                                ToastUtils.toastMsg(OrderDetailActivity.this, "取件成功");
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFail() {
                                ToastUtils.toastMsg(OrderDetailActivity.this, "取件成功");
                            }
                        });
                    }
                });
            }
            return convertView;
        }

        @Override
        public boolean isEnabled(int position) {
            if (position < 4) {
                return false;
            }
            return super.isEnabled(position);
        }

        private int getViewType(int position) {
            if (position == 0 || position == 2) {
                return 0;
            } else if (position == 1) {
                return 1;
            } else if (position >= 3 && position <= 6) {
                return 2;
            } else if (position == 7) {
                return 3;
            }
            return 3;
        }
    }
}
