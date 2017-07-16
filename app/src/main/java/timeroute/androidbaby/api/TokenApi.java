package timeroute.androidbaby.api;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import rx.Observable;
import timeroute.androidbaby.bean.user.Token;
import timeroute.androidbaby.bean.user.User;

/**
 * Created by chinesejar on 17-7-16.
 */

public interface TokenApi {
    @POST("token/")
    Observable<Token> getToken(@Body User user);
}
