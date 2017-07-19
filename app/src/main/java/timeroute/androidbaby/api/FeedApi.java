package timeroute.androidbaby.api;

import java.util.List;

import retrofit2.http.Header;
import retrofit2.http.Query;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.bean.user.ImageToken;

/**
 * Created by chinesejar on 17-7-14.
 */

public interface FeedApi {

    @GET("feed/")
    Observable<FeedTimeLine> getLatestFeed();

    @GET("feed/")
    Observable<FeedTimeLine> getNextFeed(@Query("page") String page);

    @GET("feed/{id}")
    Observable<Feed> getDetailFeed(@Path("id") String id);

    @GET("feed_token/")
    Observable<List<ImageToken>> getImageToken(@Header("Authorization") String authorization, @Query("count") int count);
}
