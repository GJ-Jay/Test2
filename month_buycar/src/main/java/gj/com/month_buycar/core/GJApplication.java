package gj.com.month_buycar.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class GJApplication extends Application {

    private static GJApplication instance;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mSharedPreferences = getSharedPreferences("application",
                Context.MODE_PRIVATE);

    }

    public static GJApplication getInstance() {
        return instance;
    }

    public SharedPreferences getShare() {
        return mSharedPreferences;
    }
}
