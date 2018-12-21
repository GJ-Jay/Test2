package gj.com.month_buycar.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import gj.com.month_buycar.R;

public class AddSubLayout extends LinearLayout implements View.OnClickListener {

    private TextView mAddBtn;
    private TextView mSubBtn;
    private TextView mNumText;

    public AddSubLayout(Context context) {
        super(context);
        initView();
    }

    public AddSubLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AddSubLayout(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AddSubLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    //创建布局视图
    private void initView() {
        //加载layout布局，第三个参数ViewGroup一定写成this
        View view = View.inflate(getContext(),
                R.layout.car_add_sub_layout,this);

        //初始化组件
        mAddBtn = view.findViewById(R.id.btn_add);
        mSubBtn = view.findViewById(R.id.btn_sub);
        mNumText = view.findViewById(R.id.text_number);

        //设置加减的点击事件
        mAddBtn.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int width = r-1;//getWidth();
        int hright = b-t;//getHeight();
    }

    @Override
    public void onClick(View v) {
        //得到数量的值
        int number = Integer.parseInt(mNumText.getText().toString());

        switch (v.getId()){
            case R.id.btn_add:
                number++;
                mNumText.setText(number+"");//赋值
                break;

            case R.id.btn_sub:
                if(number==0){
                    Toast.makeText(getContext(),"数量不能小于0",Toast.LENGTH_LONG).show();
                    return;
                }

                number--;
                mNumText.setText(number+"");//赋值
                break;
        }
        if(addSubLinstener!=null){
            addSubLinstener.addSub(number);
        }
    }

    public void setCount(int count){
        mNumText.setText(count+"");
    }

    AddSubLinstener addSubLinstener;

    public void setAddSubLinstener(AddSubLinstener addSubLinstener) {
        this.addSubLinstener = addSubLinstener;
    }

    public  interface AddSubLinstener{
        void addSub(int count);
    }
}
