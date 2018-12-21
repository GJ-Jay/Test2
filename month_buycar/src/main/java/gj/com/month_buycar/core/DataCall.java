package gj.com.month_buycar.core;

import gj.com.month_buycar.bean.Result;

public interface DataCall<T> {
    void success(T data);
    void fail(Result result);
}
