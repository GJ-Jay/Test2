package com.gj.buycarej.http;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


import com.gj.buycarej.core.Callback;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
 
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
 
/**
 * @作者 
 * @时间 2017/11/18 10:34
 */
 
public class HttpUtils {
    private static final String TAG = "HttpUtils";
    private static volatile HttpUtils instanse;
    final static Handler handler = new Handler();
 
    private HttpUtils() {
 
    }
    public static HttpUtils getInstanse() {
        if (null == instanse) {
            synchronized (HttpUtils.class) {
                if (instanse == null) {
                    instanse = new HttpUtils();
                }
            }
        }
        return instanse;
    }
    public void get(String url, Map<String, String> map, final Callback callBack,
                    final Class cls, final String tag) {
        // http://www.baoidu.com/login?mobile=11111&password=11111&age=1&name=zw
 
        // 1.http://www.baoidu.com/login                --------？ key=value&key=value
        // 2.http://www.baoidu.com/login?               --------- key=value&key=value
        // 3.http://www.baoidu.com/login?mobile=11111   -----&key=value&key=value
        final StringBuffer sb = new StringBuffer();
        sb.append(url);
 
        if (TextUtils.isEmpty(url)) {
            return;
        }
        // 如果包含？说明是2.3类型
        if (url.contains("?")) {
            // 如果包含？并且？是最后一位，对应是2类型
            if (url.indexOf("?") == url.length() - 1) {
 
            } else {
                // 如果包含？并且？不是最后一位，对应是3类型
                sb.append("&");
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if (sb.indexOf("&") != -1) {
            sb.deleteCharAt((sb.lastIndexOf("&")));
        }
        Log.i(TAG, "get:url " + sb.toString());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .get()
                .url(sb.toString())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(tag,e);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                handler.post(new Runnable() {
                    Object o;
                    @Override
                    public void run() {
                        if(TextUtils.isEmpty(s)){
                            o=null;
                        }else
                        {
                            o = new Gson().fromJson(s, cls);
                        }
                        callBack.onSuccess(tag,o);
                    }
                });
            }
        });
    }
}