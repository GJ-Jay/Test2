package gj.com.month_buycar.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import gj.com.month_buycar.R;
import gj.com.month_buycar.bean.Goods;
import gj.com.month_buycar.core.GJApplication;
import gj.com.month_buycar.view.AddSubLayout;

/**
 * 右半边的recyclerview
 */
public class RightAdapter extends RecyclerView.Adapter<RightAdapter.ChildHolder> {

    //创建集合（商品店铺）
    List<Goods> mList = new ArrayList<>();


    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(),R.layout.recycler_right_item,null);
        return new ChildHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildHolder childHolder, int i) {
        final Goods goods = mList.get(i);
        //获得商品后 开始赋值
        childHolder.text.setText(goods.getTitle());
        childHolder.price.setText("单价:"+goods.getPrice());//单价

        //设置图片
        String imageurl = "https" + goods.getImages().split("https")[1];
        Log.i("dt", "imageUrl: " + imageurl);
        imageurl = imageurl.substring(0, imageurl.lastIndexOf(".jpg") + ".jpg".length());
        Glide.with(GJApplication.getInstance()).
                load(imageurl).into(childHolder.image);//加载图片

        childHolder.addSub.setCount(goods.getNum());//设置商品数量
        childHolder.addSub.setAddSubLinstener(new AddSubLayout.AddSubLinstener() {
            @Override
            public void addSub(int count) {
                goods.setNum(count);
                onNumListener.onNum();//计算价格
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clearList() {
        mList.clear();
    }

    public void addAll(List<Goods> list) {
        mList.addAll(list);
    }

    public class ChildHolder extends RecyclerView.ViewHolder {


        private final ImageView image;
        private final TextView text;
        private final TextView price;
        private final AddSubLayout addSub;

        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            //初始化组件
            image = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            price = itemView.findViewById(R.id.text_price);
            addSub = itemView.findViewById(R.id.add_sub_layout);
        }
    }

    private OnNumListener onNumListener;

    public void setOnNumListener(OnNumListener onNumListener) {
        this.onNumListener = onNumListener;
    }

    public interface OnNumListener{
        void onNum();
    }
}
