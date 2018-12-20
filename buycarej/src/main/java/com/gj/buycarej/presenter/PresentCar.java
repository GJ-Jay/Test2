package com.gj.buycarej.presenter;

import com.gj.buycarej.bean.GsonBean;
import com.gj.buycarej.core.Callback;
import com.gj.buycarej.core.IView;
import com.gj.buycarej.http.HttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentCar {
    private IView inv;
 
    public void attachView(IView inv) {
        this.inv = inv;
    }
    public void getNews() {
        //type=top&key=dbedecbcd1899c9785b95cc2d17131c5
        Map<String, String> map = new HashMap<>();
 
        HttpUtils.getInstanse().get("http://120.27.23.105/product/getCarts?uid=100", map, new Callback() {
            @Override
            public void onSuccess(String tag, Object o) {
                GsonBean bean = (GsonBean) o;
                if (bean != null) {
                    List<GsonBean.DataBean> data = bean.getData();
 
                    inv.Success(tag, data);
                }
            }
 
            @Override
            public void onFailure(String tag, Exception e) {
                inv.Failed(tag, e);
            }
 
 
        }, GsonBean.class, "news");
    }
    public void detachView() {
        if (inv != null) {
            inv = null;
        }
    }
}
