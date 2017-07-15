package timeroute.androidbaby.api;

import retrofit2.http.Query;
import rx.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedTimeLine;

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
}
