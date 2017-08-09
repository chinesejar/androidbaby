package timeroute.androidbaby;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.leakcanary.LeakCanary;

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by chinesejar on 17-7-13.
 */

public class MyApp extends Application {
    private static final String DB_NAME = "Androidbaby.db";
    private List<SoftReference<Activity>> activityList = new LinkedList<>();
    private static MyApp instance;
    public static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        LeakCanary.install(this);
        //JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    //单例模式中获取唯一的MyApplication实例
    public static MyApp getInstance() {
        if(null == instance) {
            instance = new MyApp();
        }
        return instance;
    }
    //添加Activity到容器中
    public void addActivity(SoftReference<Activity> softReference)  {
        activityList.add(softReference);
    }
    //遍历所有Activity并finish
    public void exit(){
        for(int i=0;i<activityList.size();i++){
            Activity activity = activityList.get(i).get();
            if(activity != null){
                activity.finish();
            }
        }
        System.exit(0);
    }
}
