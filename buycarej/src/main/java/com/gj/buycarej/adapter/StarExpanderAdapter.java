package com.gj.buycarej.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gj.buycarej.R;
import com.gj.buycarej.bean.GsonBean;
import com.gj.buycarej.bean.MessageEvent;
import com.gj.buycarej.bean.PriceAndCountEvent;
import com.gj.buycarej.core.MyView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class StarExpanderAdapter extends BaseExpandableListAdapter {
    Context context;
    List<GsonBean.DataBean> dataBeen;
    ArrayList<List<GsonBean.DataBean.ListBean>> listBean;
 
 
 
    public StarExpanderAdapter(Context context, List<GsonBean.DataBean> dataBeen, ArrayList<List<GsonBean.DataBean.ListBean>> listBean) {
        this.context = context;
        this.dataBeen = dataBeen;
        this.listBean = listBean;
 
    }
 
    @Override
    public int getGroupCount() {
        return dataBeen.size();
    }
 
    @Override
    public int getChildrenCount(int i) {
        return listBean.get(i).size();
    }
 
    @Override
    public Object getGroup(int i) {
            return dataBeen.get(i);
    }
 
    @Override
    public Object getChild(int i, int i1) {
        return listBean.get(i).get(i1);
    }
 
    @Override
    public long getGroupId(int i) {
        return i;
    }
 
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
    //一级列表布局
    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        final GsonBean.DataBean groupBean = dataBeen.get(i);
        final GroupViewHolder holder;
        if(view==null){
            holder = new GroupViewHolder();
            view = View.inflate(context, R.layout.item_yj, null);
            holder.tv_sbt = view.findViewById(R.id.tv_sign);
            holder.cbgroup = view.findViewById(R.id.cb_parent);
            view.setTag(holder);
        }else
        {
            holder = (GroupViewHolder) view.getTag();
        }
        holder.cbgroup.setChecked(groupBean.isGropuCb());
        holder.tv_sbt.setText(groupBean.getSellerName());
        //一级列表checkBox的点击事件
        holder.cbgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断一级列表复选框的状态  设置为true或false
                groupBean.setGropuCb(holder.cbgroup.isChecked());
                //改变二级checkbod的状态
                changeChildeCbState(i,holder.cbgroup.isChecked());
                //算钱
                EventBus.getDefault().post(compute());
                //改变全选状态   isAllGroupCbSelect判断一级是否全部选中
                changeAllCbState(isAllGroupCbSelected());
                //必刷新
                notifyDataSetChanged();
            }
        });
        return view;
    }
    //二级列表布局
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final GsonBean.DataBean.ListBean clildBean = this.listBean.get(i).get(i1);
        final ChildViewHolder childViewHolder;
        if(view==null){
           view = View.inflate(context, R.layout.item_ej, null);
           childViewHolder = new ChildViewHolder();
            childViewHolder.tv_sbt = (TextView) view.findViewById(R.id.tv_sbt);
            childViewHolder.img = (ImageView) view.findViewById(R.id.img);
            childViewHolder.tv_pri = (TextView) view.findViewById(R.id.tv_pri);
            childViewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            childViewHolder.cbChild =view.findViewById(R.id.cb_child);
            childViewHolder.mv = view.findViewById(R.id.mv);
            childViewHolder.tv_del = view.findViewById(R.id.tv_del);
            childViewHolder.et_et = (EditText) childViewHolder.mv.findViewById(R.id.et_et);
            view.setTag(childViewHolder);
       }else{
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        childViewHolder.tv_sbt.setText(clildBean.getTitle());
        String images = clildBean.getImages();
        String[] split = images.split("\\|");
        Glide.with(context).load(split[0]).into(childViewHolder.img);
        childViewHolder.tv_pri.setText(clildBean.getPrice()+"");
        childViewHolder.tv_content.setText(clildBean.getSubhead());
        childViewHolder.cbChild.setChecked(clildBean.isChildCb());
        childViewHolder.et_et.setText(clildBean.getNum()+"");
        //加号
        childViewHolder.mv.setOnAddDelClickLstener(new MyView.OnAddDelClickLstener() {
            @Override
            public void onAddClick(View v) {
                int num = clildBean.getNum();
                //num为int类型所以要加空字符串
                childViewHolder.et_et.setText(++num+"");
                clildBean.setNum(num);
                //如果二级列表的checkbox为选中,计算价钱
                if (childViewHolder.cbChild.isChecked()){
                    PriceAndCountEvent priceAndCountEvent = (PriceAndCountEvent) compute();
                    EventBus.getDefault().post(priceAndCountEvent);
                }
            }
        //减号
            @Override
            public void onDelClick(View v) {
                int num = clildBean.getNum();
                if(num==1){
                    return;
                }
                childViewHolder.et_et.setText(--num+"");
                clildBean.setNum(num);
                if(childViewHolder.cbChild.isChecked()){
                    PriceAndCountEvent priceAndCountEvent = (PriceAndCountEvent) compute();
                    EventBus.getDefault().post(priceAndCountEvent);
                }
            }
        });
        //删除
        childViewHolder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<GsonBean.DataBean.ListBean> listBeen = listBean.get(i);
                GsonBean.DataBean.ListBean remove = listBeen.remove(i1);
                if(listBeen.size()==0){
                    //先移除二级列表的集合,再移除一级列表的集合
                    listBean.remove(i);
                    dataBeen.remove(i);
                }
                //算钱
                EventBus.getDefault().post(compute());
                notifyDataSetChanged();
            }
        });
 
        //二级列表的点击事件
        childViewHolder.cbChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置该条目对象里的checked属性值
                clildBean.setChildCb(childViewHolder.cbChild.isChecked());
                //计算价钱
                PriceAndCountEvent priceAndCountEvent = (PriceAndCountEvent) compute();
                EventBus.getDefault().post(priceAndCountEvent);
                //判断当前checkbox是选中的状态
                if(childViewHolder.cbChild.isChecked()){
                    //如果全部选中(isAllChildCbSelected)
                    if(isAllChildCbSelected(i)){
                        //改变一级列表的状态
                        changGroupCbState(i,true);
                        //改变全选的状态
                        changeAllCbState(isAllGroupCbSelected());
                    }
                }else {
                    //如果没有全部选中,一级列表的checkbox为false不为选中
                    changGroupCbState(i,false);
                    changeAllCbState(isAllGroupCbSelected());
                }
                notifyDataSetChanged();
            }
        });
 
        return view;
    }
 
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    class GroupViewHolder {
        CheckBox cbgroup;
        TextView tv_sbt;
    }
    class ChildViewHolder {
        TextView tv_sbt;
        ImageView img;
        TextView tv_pri;
        TextView tv_content;
        CheckBox cbChild;
        MyView mv;
        TextView tv_del;
 
        EditText et_et;
    }
 
    /**
     * 改变二级列表checkbox状态
     * 如果一级选中,控制二级也选中
     * @param i
     * @param flag
     */
     private void changeChildeCbState(int i,boolean flag){
         List<GsonBean.DataBean.ListBean> listBeen = listBean.get(i);
         for (int j = 0; j <listBeen.size(); j++) {
             GsonBean.DataBean.ListBean listBean = listBeen.get(j);
             listBean.setChildCb(flag);
         }
     }
    /**
     * 判断一级列表是否全选中
     * @return
     */
     private boolean isAllGroupCbSelected(){
         for (int i = 0; i <dataBeen.size() ; i++) {
             GsonBean.DataBean dataBean = dataBeen.get(i);
             if(!dataBean.isGropuCb()){
                 return false;
             }
         }
         return true;
     }
    /**
     * 改变全选的状态
     *
     * @param flag
     */
     private void changeAllCbState(boolean flag){
         MessageEvent messageEvent = new MessageEvent();
         messageEvent.setChecked(flag);
         EventBus.getDefault().post(messageEvent);
     }
    /**
     * 判断二级列表是否全部选中
     * @param i
     * @return
     */
    private boolean isAllChildCbSelected (int i){
         List<GsonBean.DataBean.ListBean> listBeen = listBean.get(i);
         for (int j = 0; j <listBeen.size() ; j++) {
             GsonBean.DataBean.ListBean listBean = listBeen.get(j);
             if(!listBean.isChildCb()){
                 return false;
             }
         }
         return true;
     }
    /**
     * 改变一级列表checkbox状态
     *
     * @param i
     */
     private void changGroupCbState(int i,boolean flag){
         GsonBean.DataBean dataBean = dataBeen.get(i);
         dataBean.setGropuCb(flag);
     }
    /**
     * 改变二级列表checkbox状态
     * @param i
     * @param flag
     */
     private void changeChildCbState(int i,boolean flag){
         List<GsonBean.DataBean.ListBean> listBeen = listBean.get(i);
         for (int j = 0; j <listBeen.size() ; j++) {
             GsonBean.DataBean.ListBean listBean = listBeen.get(j);
             listBean.setChildCb(flag);
         }
     }
    /**
     * 计算列表中，选中的钱和数量
     */
     private Object compute(){
        int count = 0;
         int price = 0;
         for (int i = 0; i <listBean.size() ; i++) {
             List<GsonBean.DataBean.ListBean> listBeen = listBean.get(i);
             for (int j = 0; j <listBeen.size() ; j++) {
                 GsonBean.DataBean.ListBean listBean = listBeen.get(j);
                 if(listBean.isChildCb()){
                     price+=listBean.getNum()*listBean.getPrice();
                     count+=listBean.getNum();
                 }
             }
         }
         PriceAndCountEvent priceAndCountEvent = new PriceAndCountEvent();
         priceAndCountEvent.setCount(count);
         priceAndCountEvent.setPrice(price);
         return priceAndCountEvent;
     }
    /**
     * 设置全选、反选
     *
     * @param flag
     */
    public void changeAllListCbState(boolean flag) {
        for (int i = 0; i < dataBeen.size(); i++) {
            changGroupCbState(i, flag);
            changeChildCbState(i, flag);
        }
        //算钱
        EventBus.getDefault().post(compute());
        notifyDataSetChanged();
    }
}