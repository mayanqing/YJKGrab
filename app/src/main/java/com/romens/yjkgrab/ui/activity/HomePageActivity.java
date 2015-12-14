package com.romens.yjkgrab.ui.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.romens.yjkgrab.Constant;
import com.romens.yjkgrab.R;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.observer.DataObserver;
import com.romens.yjkgrab.table.OrderTable;
import com.romens.yjkgrab.table.RuntimeGrabingTable;
import com.romens.yjkgrab.ui.adapter.HomePagerAdapter;
import com.romens.yjkgrab.ui.fragment.HomeFragment;
import com.romens.yjkgrab.ui.fragment.MapFragment;
import com.romens.yjkgrab.ui.fragment.PickFragment;
import com.romens.yjkgrab.utils.AnalysisHelper;
import com.romens.yjkgrab.utils.DateCompartor;
import com.romens.yjkgrab.utils.StatusHelper;
import com.romens.yjkgrab.utils.ToastUtils;
import com.romens.yjkgrab.wokinterface.ResultCallBack;
import com.romens.yjkgrab.wokinterface.WorkInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomePageActivity extends BaseActivity implements View.OnClickListener, WorkInterface, AdapterView.OnItemClickListener {
    public static boolean isGrabing() {
        return isGrabing;
    }

    public static void setIsGrabing(boolean isGrabing) {
        HomePageActivity.isGrabing = isGrabing;
    }

    private static boolean isGrabing = false;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ImageView title_menu, play_pause;
    private TextView title_name;
    private String[] titleName = new String[]{"实时订单", "附近订单", "我的订单"};


    private ProgressDialog progressDialog;

    public static boolean isForeground() {
        return isForeground;
    }

    public static void setIsForeground(boolean isForeground) {
        HomePageActivity.isForeground = isForeground;
    }

    private static boolean isForeground = false;

    public ArrayList<Order> getData() {
        return application.getData();
    }

    private ArrayList<DataObserver> observers = new ArrayList<>();
    private static boolean isWorking = false;
    private ViewPager mainVp;
    private Fragment[] fragments = new Fragment[3];
    private TextView[] LIn = new TextView[3];
    private LinearLayout[] bottomLinearLayouts = new LinearLayout[3];
    private ImageView[] bottomIcons = new ImageView[3];
    private ImageView[] bottomTexts = new ImageView[3];
    private int[] bottomSelectIconsIds = {R.mipmap.home_icon_selected, R.mipmap.near_icon_selected, R.mipmap.order_icon_selected};
    private int[] bottomUnSelectIconsIds = {R.mipmap.home_icon_unselect, R.mipmap.near_icon_unselect, R.mipmap.order_icon_unselect};
    private int[] bottomSelectTextIds = {R.mipmap.home_text_selected, R.mipmap.near_text_selected, R.mipmap.order_text_selected};
    private int[] bottomUnSelectTextIds = {R.mipmap.home_text_unselect, R.mipmap.near_text_unselect, R.mipmap.order_text_unselect};
    private LinearLayout more_ll;


    //一键抢单
    private RelativeLayout oneKeyPick;
    private ImageView onKeyIcon;
    private NewOrderReceiver newOrderReceiver = new NewOrderReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_page);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        init();
        //初始化上下班状态
        setIsWorking(isWorking());
        registerNewOrderReceiver();
        setIsForeground(true);
        //通知栏进入
        queryDataFromServer();
        //清空通知栏
        NotificationManager mNotifyMgr =
                (NotificationManager) AVOSCloud.applicationContext
                        .getSystemService(
                                Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancelAll();
    }

    private void registerNewOrderReceiver() {
        IntentFilter intentFilter = new IntentFilter(NEW_ORDER_ACTION);
        registerReceiver(newOrderReceiver, intentFilter);
    }

    public static boolean isWorking() {
        return isWorking;
    }

    public void setIsWorking(boolean isWorking) {
        HomePageActivity.isWorking = isWorking;
        if (play_pause != null) {
            play_pause.setImageResource(isWorking ? R.mipmap.ic_pause_circle_filled_white : R.mipmap.ic_play_circle_filled_white);
        }
        if (onKeyIcon != null) {
            onKeyIcon.clearAnimation();
            RotateAnimation animation = new RotateAnimation(isWorking ? 135f : 0f, isWorking ? 0f : 135f, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(250);
            //加上这句，动画就会停留在执行完毕的状态
            //如果不加这句，会回到一开始的状态，所以会认为没有效果。如果把duration设置的长一点如5000ms，会看到明显的动画效果
            animation.setFillAfter(true);
            onKeyIcon.setAnimation(animation);
        }
    }

    private void queryDataFromServer() {
        progressDialog.show();
        final ArrayList<Order> data = application.getData();
        data.clear();
        AVQuery<AVObject> query = AVQuery.getQuery("Order");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject avObject : list) {
                        data.add(AnalysisHelper.OrderHelper.analysis(avObject));
                        notifyAllObservers();
                    }
                } else {
                    ToastUtils.toastMsg(HomePageActivity.this, "查找失败");
                }
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    public void notifyAllObservers() {
        for (DataObserver observer : observers) {
            if (!isGrabing && fragments[0] == observer) {
                continue;
            }
            observer.notifyChanged();
        }
    }

    private void init() {
        initTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        mDrawerList.setAdapter(new DrawerAdapter(this));
        mDrawerList.setOnItemClickListener(this);
        initBottonMenus();
        initViewPaget();
        setTitleName(titleName[0]);
    }

    /**
     * 初始化下方菜单栏
     */
    private void initBottonMenus() {
        bottomLinearLayouts[0] = (LinearLayout) findViewById(R.id.home_ll);
        bottomIcons[0] = (ImageView) findViewById(R.id.home_icon);
        bottomTexts[0] = (ImageView) findViewById(R.id.home_text);
        bottomLinearLayouts[1] = (LinearLayout) findViewById(R.id.near_ll);
        bottomIcons[1] = (ImageView) findViewById(R.id.near_icon);
        bottomTexts[1] = (ImageView) findViewById(R.id.near_text);
        bottomLinearLayouts[2] = (LinearLayout) findViewById(R.id.order_ll);
        bottomIcons[2] = (ImageView) findViewById(R.id.order_icon);
        bottomTexts[2] = (ImageView) findViewById(R.id.order_text);
        for (LinearLayout textView : bottomLinearLayouts) {
            textView.setOnClickListener(this);
        }
        more_ll = (LinearLayout) findViewById(R.id.more_ll);
        more_ll.setOnClickListener(this);
        oneKeyPick = (RelativeLayout) findViewById(R.id.one_key_pick);
        onKeyIcon = (ImageView) findViewById(R.id.one_key_icon);
        oneKeyPick.setOnClickListener(this);
    }

    private void initViewPaget() {
        mainVp = (ViewPager) findViewById(R.id.main_vp);
        fragments[0] = new HomeFragment();
        fragments[1] = new MapFragment();
        fragments[2] = new PickFragment();
        registerDataObserver((DataObserver) fragments[0]);
        registerDataObserver((DataObserver) fragments[2]);
        mainVp.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), fragments));
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitleName(titleName[position]);
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        bottomIcons[i].setImageResource(bottomSelectIconsIds[i]);
                        bottomTexts[i].setImageResource(bottomSelectTextIds[i]);
                    } else {
                        bottomIcons[i].setImageResource(bottomUnSelectIconsIds[i]);
                        bottomTexts[i].setImageResource(bottomUnSelectTextIds[i]);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTitle() {
        title_menu = (ImageView) findViewById(R.id.title_menu);
        title_name = (TextView) findViewById(R.id.title_name);
        play_pause = (ImageView) findViewById(R.id.play_pause);
        title_menu.setOnClickListener(this);
        play_pause.setOnClickListener(this);
    }

    private void setTitleName(String titleName) {
        title_name.setText(titleName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_menu:
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                break;

            case R.id.play_pause:
                setIsWorking(!isWorking());
                break;
            case R.id.home_ll:
                mainVp.setCurrentItem(0, false);
                break;
            case R.id.near_ll:
                mainVp.setCurrentItem(1, false);
                break;
            case R.id.order_ll:
                mainVp.setCurrentItem(2, false);
                break;
            case R.id.more_ll:
//                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.one_key_pick:
                if (isWorking) {
                    if (mainVp.getCurrentItem() == 0) {
                        HomeFragment homeFragment = (HomeFragment) fragments[0];
                        homeFragment.grabbingAll();
                    } else {
                        mainVp.setCurrentItem(0, false);
                    }
                } else {
                    ToastUtils.toastMsg(HomePageActivity.this, "先上班！！！");
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        setIsForeground(false);
        unregisterReceiver(newOrderReceiver);
        unRegisterDataObserver((DataObserver) fragments[0]);
        unRegisterDataObserver((DataObserver) fragments[2]);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void registerDataObserver(DataObserver observer) {
        observers.add(observer);
    }

    public void unRegisterDataObserver(DataObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void grabing(final Order order, final ResultCallBack resultCallBack) {
        AVObject avObject = new AVObject(RuntimeGrabingTable.RuntimeGrabing);
        avObject.put(RuntimeGrabingTable.installationId, application.getInstallationId());
        avObject.put(RuntimeGrabingTable.order, AVObject.createWithoutData(OrderTable.Order, order.getObjectId()));
        avObject.put(RuntimeGrabingTable.grabdate, new Date());
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    resultCallBack.onFail();
                    return;
                }
                queryWhoGrabSuccess(order, resultCallBack);
            }
        });
    }


    /**
     * 更新数据
     *
     * @param order
     * @param resultCallBack
     */
    private void updateOrderInfo(final Order order, final ResultCallBack resultCallBack, final boolean isSuccess) {
        AVQuery<AVObject> query = AVQuery.getQuery(OrderTable.Order);
        query.getInBackground(order.getObjectId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e != null || avObject == null) {
                    resultCallBack.onFail();
                    return;
                }
                if (!isSuccess) {
                    //抢单失败，标注失效
                    order.setStatus(Constant.STATUS_INVALID);
                    //改变界面显示
                    notifyAllObservers();
                    resultCallBack.onFail();
                    return;
                }
                grab(avObject, order, resultCallBack);
            }
        });
    }

    /**
     * 看是不是最先抢到此单的
     *
     * @param order
     * @param resultCallBack
     */
    private void queryWhoGrabSuccess(final Order order, final ResultCallBack resultCallBack) {
        AVQuery<AVObject> query = AVQuery.getQuery(RuntimeGrabingTable.RuntimeGrabing);
        query.whereEqualTo(RuntimeGrabingTable.order, AVObject.createWithoutData(OrderTable.Order, order.getObjectId()));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                Collections.sort(list, new DateCompartor());
                if (TextUtils.equals(list.get(0).getString(RuntimeGrabingTable.installationId), application.getInstallationId())) {
                    //成功
                    updateOrderInfo(order, resultCallBack, true);
                } else {
                    updateOrderInfo(order, resultCallBack, false);
                }
            }
        });
    }

    @Override
    public void getOrderDetail(final Order order, final ResultCallBack resultCallBack) {
        if (order.isPerfect()) {
            resultCallBack.onSuccess();
            return;
        }
        StatusHelper.getDetail(order, resultCallBack);
    }


    private void grab(final AVObject avObject, final Order order, final ResultCallBack resultCallBack) {
        avObject.put(OrderTable.status, Constant.STATUS_WAITING_TAKE);
        avObject.put(OrderTable.installationId, application.getInstallationId());
        final Date date = new Date();
        avObject.put(OrderTable.grabDate, date);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    order.setInstallationId(application.getInstallationId());
                    order.setStatus(Constant.STATUS_WAITING_TAKE);
                    order.setPickupDate(date);
                    notifyAllObservers();
                    resultCallBack.onSuccess();
                } else {
                    e.printStackTrace();
                    resultCallBack.onFail();
                }
            }
        });
    }

    public static final String NEW_ORDER_ACTION = "com.yunuo.order.newOrder";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, OrderFinishedActivity.class));
    }

    public class NewOrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), NEW_ORDER_ACTION)) {
                ToastUtils.toastMsg(HomePageActivity.this, "有新订单！！");
                queryDataFromServer();
            }
        }
    }

    class DrawerAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public DrawerAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean isEnabled(int position) {

            return position == 0 ? false : super.isEnabled(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getDrawerViewType(position);
            if (viewType == 0) {
                convertView = inflater.inflate(R.layout.user_info_layout, null, true);
            } else if (viewType == 1) {
                convertView = inflater.inflate(R.layout.item_choice_layout, null, true);
            }
            return convertView;
        }

        private int getDrawerViewType(int position) {
            if (position == 0) {
                return 0;
            } else if (position > 0 && position < 6) {
                return 1;
            }
            return 0;
        }
    }
}
