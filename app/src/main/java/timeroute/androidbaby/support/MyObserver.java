package timeroute.androidbaby.support;

import rx.Subscriber;
import timeroute.androidbaby.api.exception.ApiException;

/**
 * Created by chinesejar on 17-7-21.
 */

public abstract class MyObserver<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e){
        if(e instanceof ApiException){
            onError((ApiException)e);
        }else {
            onError(new ApiException(e, 123));
        }
    }

    protected abstract void onError(ApiException ex);
}
