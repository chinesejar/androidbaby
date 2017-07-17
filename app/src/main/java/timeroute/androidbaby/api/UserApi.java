package timeroute.androidbaby.api;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.bean.user.User;

/**
 * Created by chinesejar on 17-7-16.
 */

public interface UserApi {
    @POST("user/")
    Observable<User> getUser();

    @PUT("user/{id}/")
    Observable<Void> putProfile(@Header("Authorization") String authorization, @Path("id") int id, @Body Profile profile);
}
