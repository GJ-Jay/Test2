package gj.com.month_buycar.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import gj.com.month_buycar.R;
import gj.com.month_buycar.bean.Goods;
import gj.com.month_buycar.bean.Shop;
import gj.com.month_buycar.core.GJApplication;
import gj.com.month_buycar.view.AddSubLayout;

public class CartAdapter extends BaseExpandableListAdapter {

    //创建集合
    ArrayList<Shop> mList = new ArrayList<>();

    public CartAdapter(){

    }
    TotalPriceListener totalPriceListener;

    public void setTotalPriceListener(TotalPriceListener totalPriceListener) {
        this.totalPriceListener = totalPriceListener;
    }

    public void addAll(List<Shop> data) {
        if(data!=null){
            mList.addAll(data);
        }
    }

    //设置接口用于计算总价
    public interface TotalPriceListener{
        void totalPrice(double totalPrice);
    }

    //父级列表的数量
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    //子级列表的数量
    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getList().size();
    }

    //获取父级
    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    //获取子级
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getList().get(childPosition);
    }

    //父级id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //子级id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //父级视图
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupHolder holder = null;

        if(convertView == null){
            convertView = View.inflate(parent.getContext(),
                    R.layout.frag02_cart_group_item,null);
            holder = new GroupHolder();
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }else {
            holder = (GroupHolder) convertView.getTag();
        }
        //获取当前的店铺
        final Shop shop = mList.get(groupPosition);

        //赋值
        holder.checkBox.setText(shop.getSellerName());//店铺名
        holder.checkBox.setChecked(shop.isCheck());//店铺前边复选框的选中状态
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shop.setCheck(isChecked);//数据更新 设置选中状态
                List<Goods> goodsList = mList.get(groupPosition).getList();//得到商品信息
                //商品信息循环赋值
                for (int i = 0; i <goodsList.size() ; i++) {
                    //商铺选中则商品必须选中
                    goodsList.get(i).setSelected(isChecked?1:0);
                }
                notifyDataSetChanged();//刷新适配器
                //选中的话 计算价格
                calculatePrice();
            }
        });

        return convertView;
    }

    /**
     * 计算总价格
     */
    private void calculatePrice() {
        double totalPrice = 0;
        for (int i = 0; i <mList.size() ; i++) {//循环的是商家
            Shop shop = mList.get(i);
            for (int j = 0; j <shop.getList().size() ; j++) {//遍历商品
                Goods goods = shop.getList().get(j);
                if(goods.getSelected()==1){//如果是选中状态
                    totalPrice = totalPrice+goods.getNum()*goods.getPrice();//选中的计算总价
                }
            }
        }
        if(totalPriceListener!=null){
            totalPriceListener.totalPrice(totalPrice);//设置总价
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MyHolder holder = null;

        if(convertView == null){
            convertView = View.inflate(parent.getContext(),
                    R.layout.frag02_cart_child_item,null);
            holder = new MyHolder();
            holder.check = convertView.findViewById(R.id.cart_goods_check);
            holder.image = convertView.findViewById(R.id.image);
            holder.text = convertView.findViewById(R.id.text);
            holder.price = convertView.findViewById(R.id.text_price);
            holder.addSub = convertView.findViewById(R.id.add_sub_layout);
            convertView.setTag(holder);
        }else {
            holder = (MyHolder) convertView.getTag();
        }
        //获取商品
        final Goods goods = mList.get(groupPosition).getList().get(childPosition);
        //赋值
        holder.text.setText(goods.getTitle());
        holder.price.setText("单价:"+goods.getPrice());//单价
        //点击选中，计算价格
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //设置选中状态
                goods.setSelected(isChecked?1:0);
                calculatePrice();//选中计算价格
            }
        });

        if(goods.getSelected()==0){
            holder.check.setChecked(false);
        }else {
            holder.check.setChecked(true);
        }

        //设置图片
        String imageurl = "https" + goods.getImages().split("https")[1];
        Log.i("dt", "imageUrl: " + imageurl);
        imageurl = imageurl.substring(0, imageurl.lastIndexOf(".jpg") +
                ".jpg".length());
        Glide.with(GJApplication.getInstance()).load(imageurl).into(holder.image);//加载图片

        holder.addSub.setCount(goods.getNum());//设置商品数量
        holder.addSub.setAddSubLinstener(new AddSubLayout.AddSubLinstener() {
            @Override
            public void addSub(int count) {
                //商品设置数量
                goods.setNum(count);
                calculatePrice();//计算价格
            }
        });
        return convertView;
    }

    /**
     * 全部选中或者取消
     * @param
     * @param
     * @return
     */
    public void checkAll(boolean isCheck){
        for (int i = 0; i <mList.size() ; i++) {//循环的商家
            Shop shop = mList.get(i);//设置商家选中状态
            shop.setCheck(isCheck);
            for (int j = 0; j <shop.getList().size() ; j++) {
                Goods goods = shop.getList().get(j);//得到商品
                goods.setSelected(isCheck?1:0);//设置商品选中状态
            }
        }
        //刷新
        notifyDataSetChanged();
        calculatePrice();//调用总价
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupHolder{
        CheckBox checkBox;
    }

    class MyHolder{
        CheckBox check;
        TextView text;
        TextView price;
        ImageView image;
        AddSubLayout addSub;
    }
}
