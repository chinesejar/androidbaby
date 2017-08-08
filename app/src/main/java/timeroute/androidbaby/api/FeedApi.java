package timeroute.androidbaby.api;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.Response;
import rx.Observable;
import timeroute.androidbaby.bean.feed.Comment;
import timeroute.androidbaby.bean.feed.CommentTimeLine;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.bean.feed.Like;
import timeroute.androidbaby.bean.feed.LikeTimeLine;
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

    @GET("feed/")
    Observable<FeedTimeLine> getHotFeed(@Header("Authorization") String authorization, @Query("order_by") String order_by);

    @POST("feed/")
    Observable<Feed> postFeed(@Header("Authorization") String authorization, @Body Feed feed);

    @DELETE("feed/{feed_id}/")
    Observable<Response<Object>> deleteFeed(@Header("Authorization") String authorization, @Path("feed_id") int feed_id);

    @GET("comment/")
    Observable<CommentTimeLine> getLatestComment(@Header("Authorization") String authorization, @Query("feed") int feed_id);

    @GET("comment_notification/")
    Observable<CommentTimeLine> getLatestCommentNotification(@Header("Authorization") String authorization);

    @GET("comment_notification/")
    Observable<CommentTimeLine> getNextCommentNotification(@Header("Authorization") String authorization, @Query("page") String page);

    @GET("like_notification/")
    Observable<LikeTimeLine> getLatestLikeNotification(@Header("Authorization") String authorization);

    @GET("like_notification/")
    Observable<LikeTimeLine> getNextLikeNotification(@Header("Authorization") String authorization, @Query("page") String page);

    @GET("feed_token/")
    Observable<List<ImageToken>> getImageToken(@Header("Authorization") String authorization, @Query("count") int count);

    @POST("like/")
    Observable<Response<Object>> postLike(@Header("Authorization") String authorization, @Body Like like);

    @POST("comment/")
    Observable<Comment> postComment(@Header("Authorization") String authorization, @Body Comment comment);
}
