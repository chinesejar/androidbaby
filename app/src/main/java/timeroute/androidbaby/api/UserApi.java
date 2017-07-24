package timeroute.androidbaby.api;

import rx.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import timeroute.androidbaby.bean.user.ImageToken;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.bean.user.User;
import timeroute.androidbaby.bean.user.UserToken;

/**
 * Created by chinesejar on 17-7-16.
 */

public interface UserApi {

    @POST("api-token-auth/")
    Observable<UserToken> getToken(@Body User user);

    @GET("avatar/")
    Observable<ImageToken> getImageToken(@Header("Authorization") String authorization);

    @POST("user/")
    Observable<User> getUser();

    @PUT("user/{id}/")
    Observable<Void> putProfile(@Header("Authorization") String authorization, @Path("id") int id, @Body Profile profile);
}
