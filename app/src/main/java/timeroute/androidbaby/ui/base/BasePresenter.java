package timeroute.androidbaby.ui.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import timeroute.androidbaby.api.ApiFactory;
import timeroute.androidbaby.api.FeedApi;


public abstract class BasePresenter<V> {

    protected Reference<V> mViewRef;

    public static final FeedApi feedApi = ApiFactory.getFeedApiSingleton();

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