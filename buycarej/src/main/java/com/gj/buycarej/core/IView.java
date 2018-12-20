package com.gj.buycarej.core;

import com.gj.buycarej.bean.GsonBean;

import java.util.List;

public interface IView {
    void Success (String tag, List<GsonBean.DataBean> list);
    void Failed(String tag,Exception e);
}