package timeroute.androidbaby.api;

/**
 * Created by chinesejar on 17-7-14.
 */

public class ApiFactory {

    protected static final Object monitor = new Object();
    static FeedApi feedApiSingleton = null;
    static UserApi userApiSingleton = null;
    static TokenApi tokenApiSingleton = null;

    public static FeedApi getFeedApiSingleton(){
        synchronized (monitor){
            if(feedApiSingleton == null){
                feedApiSingleton = new ApiRetrofit().getFeedApiService();
            }
            return feedApiSingleton;
        }
    }

    public static UserApi getUserApiSingleton(){
        synchronized (monitor){
            if(userApiSingleton == null){
                userApiSingleton = new ApiRetrofit().getUserApiService();
            }
            return userApiSingleton;
        }
    }

    public static TokenApi getTokenApiSingleton(){
        synchronized (monitor){
            if(tokenApiSingleton == null){
                tokenApiSingleton = new ApiRetrofit().getTokenApiService();
            }
            return tokenApiSingleton;
        }
    }
}
