package gj.com.month_buycar.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import gj.com.month_buycar.R;

public class ShowActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        //初始化组件
        frameLayout = findViewById(R.id.frag);
        radioGroup = findViewById(R.id.group);
        final FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();//开启事务
        //创建视图
        final Frag01 frag01 = new Frag01();
        final Frag02 frag02 = new Frag02();
        final Frag03 frag03 = new Frag03();
        transaction.add(R.id.frag,frag01).show(frag01);//添加视图
        transaction.add(R.id.frag,frag02).hide(frag02);
        transaction.add(R.id.frag,frag03).hide(frag03);
        //提交事务
        transaction.commit();

        //默认第一个选中
        radioGroup.check(radioGroup.getChildAt(0).getId());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction1 = manager.beginTransaction();
                switch (checkedId){
                    case R.id.radio1:
                        transaction1.show(frag01).hide(frag02).hide(frag03);
                        radioGroup.check(radioGroup.getChildAt(0).getId());
                        break;
                    case R.id.radio2:
                        transaction1.show(frag02).hide(frag01).hide(frag03);
                        radioGroup.check(radioGroup.getChildAt(1).getId());
                        break;

                    case R.id.radio3:
                        transaction1.show(frag03).hide(frag01).hide(frag02);
                        radioGroup.check(radioGroup.getChildAt(2).getId());
                        break;
                }
                //提交事务
                transaction1.commit();
            }
        });
    }
}
