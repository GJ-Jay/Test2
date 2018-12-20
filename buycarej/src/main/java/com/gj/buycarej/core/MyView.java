package com.gj.buycarej.core;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gj.buycarej.R;

public class MyView extends LinearLayout {
    private OnAddDelClickLstener lister;
    private Button btn_add;
    private Button txtAdd;
    private EditText et_et;
    private Button btn_del;
 
    //定义一个对外开放的接口
    public interface OnAddDelClickLstener{
        void onAddClick(View v);
        void onDelClick(View v);
    }
    public void setOnAddDelClickLstener(OnAddDelClickLstener lister){
        if(lister!=null){
            this.lister = lister;
        }
    }
    public MyView(Context context) {
        this(context,null);
    }
 
    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
 
    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }
 
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View.inflate(context, R.layout.item_jj,this);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_del = (Button) findViewById(R.id.btn_del);
        et_et = (EditText) findViewById(R.id.et_et);
 
        btn_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("AddDeleteView","点击了减号");
                lister.onDelClick(view);
            }
        });
        btn_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("txtAdd","点击了加号");
                lister.onAddClick(view);
            }
        });
 
    }
 
 
}