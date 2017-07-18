package timeroute.androidbaby.ui.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import timeroute.androidbaby.api.ApiFactory;
import timeroute.androidbaby.api.FeedApi;
import timeroute.androidbaby.api.UserApi;


public abstract class BasePresenter<V> {

    protected Reference<V> mViewRef;

    public static final FeedApi feedApi = ApiFactory.getFeedApiSingleton();
    public static final UserApi userApi = ApiFactory.getUserApiSingleton();

    public static final String baseAvatarUrl = "http://osczzc4f6.bkt.clouddn.com/";

    public void attachView(V view){
        mViewRef = new WeakReference<V>(view);
    }

    protected V getView(){
        return mViewRef.get();
    }

    public boolean isViewAttached(){
        return mViewRef != null&&mViewRef.get()!=null;
    }

    public void detachView(){
        if(mViewRef!=null){
            mViewRef.clear();
            mViewRef = null;
        }
    }

}