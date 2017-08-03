package timeroute.androidbaby;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by chinesejar on 17-7-13.
 */

public class MyApp extends Application {
    private static final String DB_NAME = "Androidbaby.db";
    public static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }
}
