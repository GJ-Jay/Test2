package gj.com.month_buycar.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.List;

import gj.com.month_buycar.R;
import gj.com.month_buycar.adapter.CartAdapter;
import gj.com.month_buycar.bean.Goods;
import gj.com.month_buycar.bean.Result;
import gj.com.month_buycar.bean.Shop;
import gj.com.month_buycar.core.DataCall;
import gj.com.month_buycar.presenter.CartPresenter;

/**
 * 仿淘宝购物车
 */
public class Frag02 extends Fragment implements DataCall<List<Shop>>,
        CartAdapter.TotalPriceListener {


    private ExpandableListView mGoodsList;
    private CheckBox mCheckAll;
    private TextView mSumPrice;
    private CartAdapter mCartAdapter;
    private CartPresenter cartPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag02,container,false);
        //初始化组件
        mGoodsList = view.findViewById(R.id.list_cart);
        mCheckAll = view.findViewById(R.id.check_all);
        mSumPrice = view.findViewById(R.id.goods_sum_price);

        //点击了全选或反选后 其他所有的商品都选中或都不选
        mCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCartAdapter.checkAll(isChecked);
            }
        });

        //设置适配器
        mCartAdapter = new CartAdapter();
        mGoodsList.setAdapter(mCartAdapter);//添加适配器
        mCartAdapter.setTotalPriceListener(this);//设置总价回调器
        mGoodsList.setGroupIndicator(null);//将默认小箭头去掉
        //让其group不能被点击
        mGoodsList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        cartPresenter = new CartPresenter(this);
        cartPresenter.requestData();
        return view;
    }

    @Override
    public void success(List<Shop> data) {
        mCartAdapter.addAll(data);
        //遍历所有group,将所有项设置成默认展开
        int groupCount = data.size();
        for (int i = 0; i <groupCount ; i++) {
            mGoodsList.expandGroup(i);
        }

        //刷新适配器
        mCartAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(Result result) {
        Toast.makeText(getActivity(), result.getCode() + "   " + result.getMsg(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void totalPrice(double totalPrice) {
        mSumPrice.setText(String.valueOf(totalPrice));//设置总价
    }
}
