package timeroute.androidbaby.api;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.Response;
import rx.Observable;
import timeroute.androidbaby.bean.feed.CommentTimeLine;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.bean.feed.Like;
import timeroute.androidbaby.bean.user.ImageToken;

/**
 * Created by chinesejar on 17-7-14.
 */

public interface FeedApi {

    @GET("feed/")
    Observable<FeedTimeLine> getLatestFeed(@Header("Authorization") String authorization);

    @GET("feed/")
    Observable<FeedTimeLine> getNextFeed(@Header("Authorization") String authorization, @Query("page") String page);

    @GET("feed/")
    Observable<FeedTimeLine> getLatestUserFeed(@Header("Authorization") String authorization, @Query("user") int user_id);

    @GET("feed/")
    Observable<FeedTimeLine> getNextUserFeed(@Header("Authorization") String authorization, @Query("page") String page, @Query("user") int user_id);

    @GET("comment/")
    Observable<CommentTimeLine> getLatestComment(@Header("Authorization") String authorization, @Query("feed") int feed_id);

    @GET("feed/{id}")
    Observable<Feed> getDetailFeed(@Header("Authorization") String authorization, @Path("id") String id);

    @GET("feed_token/")
    Observable<List<ImageToken>> getImageToken(@Header("Authorization") String authorization, @Query("count") int count);

    @POST("feed/")
    Observable<Feed> postFeed(@Header("Authorization") String authorization, @Body Feed feed);

    @POST("like/")
    Observable<Response<Object>> postLike(@Header("Authorization") String authorization, @Body Like like);
}
