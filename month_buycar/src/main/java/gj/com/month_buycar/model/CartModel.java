package gj.com.month_buycar.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import gj.com.month_buycar.bean.Result;
import gj.com.month_buycar.bean.Shop;
import gj.com.month_buycar.utils.HttpUtils;

public class CartModel {
    public static Result goodsList(){
        String resultString = HttpUtils.get("http://www.zhaoapi.cn/product/getCarts?uid=71");

       //try {

        //注意了
        Type type = new TypeToken<Result<List<Shop>>>(){}.getType();

        Result result = new Gson().fromJson(resultString, type);
        return result;
    /*} catch (Exception e) {

    }
    Result result = new Result();
        result.setCode(-1);
        result.setMsg("数据解析异常");
        return result;*/
    }
}
