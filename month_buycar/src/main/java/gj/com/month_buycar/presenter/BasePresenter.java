package gj.com.month_buycar.presenter;

import android.os.Handler;
import android.os.Message;

import gj.com.month_buycar.bean.Result;
import gj.com.month_buycar.core.DataCall;

public abstract class BasePresenter {

    //接口
    DataCall dataCall;

    public BasePresenter(DataCall dataCall) {
        this.dataCall = dataCall;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Result result = (Result) msg.obj;
            //判断是否成功
            if(result.getCode()==0){
                //将商铺信息传过去
                dataCall.success(result.getData());
            }else{
                dataCall.fail(result);
            }
        }
    };

    //得到请求数据
    public void requestData(final Object...args){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                message.obj = getData(args);//定义一个抽象方法调用
                mHandler.sendMessage(message);
            }
        }).start();
    }

    protected abstract Result getData(Object...args);

    //避免内存溢出
    public void unBindCall(){
        this.dataCall = null;
    }
}
