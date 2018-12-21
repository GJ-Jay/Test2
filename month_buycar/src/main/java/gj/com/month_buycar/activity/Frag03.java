package gj.com.month_buycar.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import gj.com.month_buycar.R;
import gj.com.month_buycar.adapter.LeftAdapter;
import gj.com.month_buycar.adapter.RightAdapter;
import gj.com.month_buycar.bean.Goods;
import gj.com.month_buycar.bean.Result;
import gj.com.month_buycar.bean.Shop;
import gj.com.month_buycar.core.DataCall;
import gj.com.month_buycar.presenter.CartPresenter;

/**
 * 仿饿了么点餐
 */
public class Frag03 extends Fragment implements DataCall<List<Shop>> {

    private RecyclerView mLeftRecycler;
    private RecyclerView mRightRecycler;
    private TextView mCount;
    private TextView mSumPrice;
    private LeftAdapter mLeftAdapter;
    private RightAdapter mRightAdapter;
    private CartPresenter cartPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag03,container,false);
        //初始化组件
        mLeftRecycler = view.findViewById(R.id.left_recycler);
        mRightRecycler = view.findViewById(R.id.right_recycler);
        mCount = view.findViewById(R.id.goods_number);
        mSumPrice = view.findViewById(R.id.goods_sum_price);

        //设置recyclerview的布局管理器
        mLeftRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mLeftAdapter = new LeftAdapter();
        mRightAdapter = new RightAdapter();
        //设置条目点击时间
        mLeftAdapter.setOnItemClickListenter(new LeftAdapter.OnItemClickListenter() {
            @Override
            public void onItemClick(Shop shop) {
                //首先 先清空原来的右侧数据
                mRightAdapter.clearList();//清空右侧数据
                //在根据点击的店铺条目重新添加数据到集合中
                mRightAdapter.addAll(shop.getList());//将店铺的商品集合传到右侧适配器
                //刷新适配器
                mRightAdapter.notifyDataSetChanged();
            }
        });
        mLeftRecycler.setAdapter(mLeftAdapter);//设置适配器
        mRightAdapter.setOnNumListener(new RightAdapter.OnNumListener() {
            @Override
            public void onNum() {
                calculatePrice(mLeftAdapter.getList());//设置总价和总数量
            }
        });

        //设置右半边适配器
        mRightRecycler.setAdapter(mRightAdapter);
        //调用P层请求数据
        cartPresenter = new CartPresenter(this);
        cartPresenter.requestData();
        return view;
    }

    /**
     * @author dingtao
     * @date 2018/12/18 7:01 PM
     * 计算总价格
     */
    private void calculatePrice(List<Shop> shopList){
        double totalPrice=0;
        int totalNum = 0;
        for (int i = 0; i < shopList.size(); i++) {//循环的商家
            Shop shop = shopList.get(i);
            for (int j = 0; j < shop.getList().size(); j++) {
                Goods goods = shop.getList().get(j);
                //计算价格
                totalPrice = totalPrice + goods.getNum() * goods.getPrice();
                totalNum+=goods.getNum();//计数
            }
        }
        mSumPrice.setText("价格："+totalPrice);
        mCount.setText(""+totalNum);
    }

    @Override
    public void success(List<Shop> data) {
        calculatePrice(data);//计算价格和数量
        mLeftAdapter.addAll(data);//左边的添加类型

        //得到默认选中的shop，设置上颜色和背景
        Shop shop = data.get(1);
        shop.setTextColor(0xff000000);
        shop.setBackground(R.color.white);
        //根据左侧商铺给右侧添加集合
        mRightAdapter.addAll(shop.getList());

        //刷新适配器
        mLeftAdapter.notifyDataSetChanged();
        mRightAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(Result result) {
        Toast.makeText(getContext(), result.getCode() + "   " +
                result.getMsg(), Toast.LENGTH_LONG).show();
    }
}
