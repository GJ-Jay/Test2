package gj.com.month_buycar.presenter;

import gj.com.month_buycar.bean.Result;
import gj.com.month_buycar.core.DataCall;
import gj.com.month_buycar.model.CartModel;

public class CartPresenter extends BasePresenter{
    public CartPresenter(DataCall dataCall) {
        super(dataCall);
    }

    @Override
    protected Result getData(Object... args) {
        //调用网络请求获取数据M层
        Result result = CartModel.goodsList();
        return result;
    }
}
