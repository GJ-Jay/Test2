package gj.com.month_buycar.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gj.com.month_buycar.R;
import gj.com.month_buycar.bean.Shop;

/**
 * 左半边的recyclerview
 */
public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.MyHolder> {

    //创建集合（店铺）
    List<Shop> mList = new ArrayList<>();



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(),
                R.layout.recycler_left_item,null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //找数据
        final Shop shop = mList.get(i);
        myHolder.text.setText(shop.getSellerName());//设置店铺名字
        //设置字体颜色和背景
        myHolder.text.setBackgroundResource(shop.getBackground());
        myHolder.text.setTextColor(shop.getTextColor());
        //设置条目点击事件
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置字体的颜色
                for (int j = 0; j <mList.size() ; j++) {
                    mList.get(j).setTextColor(0xffffffff);//设置所有非点击条目的字体及背景
                    mList.get(j).setBackground(R.color.grayblack);
                }

                //点击的当前条目的背景及字体颜色
                shop.setBackground(R.color.white);
                shop.setTextColor(0xff000000);
                //刷新适配器
                notifyDataSetChanged();
                //点击完切换右边的列表
                onItemClickListenter.onItemClick(shop);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<Shop> getList() {
        return mList;
    }

    public void addAll(List<Shop> data) {
        mList.addAll(data);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private final TextView text;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //初始化组件
            text = itemView.findViewById(R.id.left_text);
        }
    }

    private OnItemClickListenter onItemClickListenter;

    public void setOnItemClickListenter(OnItemClickListenter onItemClickListenter) {
        this.onItemClickListenter = onItemClickListenter;
    }

    //自定义点击的接口 设置set方法
    public interface OnItemClickListenter{
        void onItemClick(Shop shop);
    }

}
