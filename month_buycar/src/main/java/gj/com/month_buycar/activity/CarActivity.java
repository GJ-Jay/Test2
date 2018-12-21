package gj.com.month_buycar.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import gj.com.month_buycar.R;

public class CarActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        imageView = findViewById(R.id.image);
        findViewById(R.id.btn_click).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_click:
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(imageView,"rotationX",0,180),
                        ObjectAnimator.ofFloat(imageView,"alpha",0,0.8f)
                );//绕X轴翻转  //透明度
                set.setDuration(1000).start();//时间

                //设置延长跳转
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CarActivity.this,ShowActivity.class);
                        startActivity(intent);
                    }
                },1000);
                break;
        }
    }
}
