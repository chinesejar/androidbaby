package timeroute.androidbaby.api;

import retrofit2.http.POST;
import rx.Observable;
import timeroute.androidbaby.bean.user.User;

/**
 * Created by chinesejar on 17-7-16.
 */

public interface UserApi {
    @POST("user/")
    Observable<User> getUser();
}
