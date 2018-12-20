package com.gj.buycarej;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gj.buycarej.adapter.StarExpanderAdapter;
import com.gj.buycarej.bean.GsonBean;
import com.gj.buycarej.bean.MessageEvent;
import com.gj.buycarej.bean.PriceAndCountEvent;
import com.gj.buycarej.core.IView;
import com.gj.buycarej.presenter.PresentCar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IView {

    private ExpandableListView elv;
    ArrayList<GsonBean.DataBean> dataBeen = new ArrayList<>();
    private CheckBox checkquan;
    private List<GsonBean.DataBean.ListBean> lists;
    ArrayList<List<GsonBean.DataBean.ListBean>> listBean = new ArrayList<>();
    private StarExpanderAdapter adapter;
    private TextView tv_price;
    private TextView tv_num;
    private PresentCar presentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册一下
        EventBus.getDefault().register(this);
        //获取控件
        elv = (ExpandableListView) findViewById(R.id.elv);
        checkquan = (CheckBox) findViewById(R.id.checkbox2);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_num = (TextView) findViewById(R.id.tv_num);
        //获取数据
        presentCar = new PresentCar();
        presentCar.attachView(this);
        presentCar.getNews();
        checkquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置全选
                adapter.changeAllListCbState(checkquan.isChecked());
            }
        });

    }
    //销毁方法
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //eventBus销毁
        EventBus.getDefault().unregister(this);
        if (presentCar!=null){
            presentCar.detachView();
        }

    }
    @Override
    public void Success(String tag, List<GsonBean.DataBean> list) {
        dataBeen.addAll(list);
        for (int i = 0; i < dataBeen.size(); i++) {
            GsonBean.DataBean bean = dataBeen.get(i);
            lists = bean.getList();
            listBean.add(lists);
        }
        adapter = new StarExpanderAdapter(MainActivity.this,dataBeen,listBean);
        elv.setAdapter(adapter);
        //隐藏二级列表前小三角
        elv.setGroupIndicator(null);
        //使二级列表一直展示
        for (int i = 0; i <dataBeen.size() ; i++) {
            elv.expandGroup(i);
        }
    }
    @Override
    public void Failed(String tag, Exception e) {
    }
    //必写注解
    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        checkquan.setChecked(event.isChecked());
    }
    @Subscribe
    public void onMessageEvent(PriceAndCountEvent event) {
        tv_num.setText("结算(" + event.getCount() + ")");
        tv_price.setText(event.getPrice() + "");
    }
}

