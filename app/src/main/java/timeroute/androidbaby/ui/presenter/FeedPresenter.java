package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;


import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.bean.feed.Like;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.adapter.FeedListAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.ui.view.RecyclerViewClickListener;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class FeedPresenter extends BasePresenter<IFeedView> {

    private static final String TAG = "FeedPresenter";

    private SharedPreferenceUtils sharedPreferenceUtils;
    private String token;

    private Context context;
    private IFeedView feedView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private FeedTimeLine timeLine;
    private FeedListAdapter adapter;
    private int lastVisibleItem;
    private boolean isLoadMore = false;
    private String next;

    public FeedPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
        token = sharedPreferenceUtils.getString("token");
    }

    public void getLatestFeed(){
        feedView = getView();
        if(feedView != null){
            mRecyclerView = feedView.getRecyclerView();
            layoutManager = feedView.getLayoutManager();

            feedApi.getLatestFeed("JWT "+token)
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends FeedTimeLine>>() {
                        @Override
                        public Observable<? extends FeedTimeLine> call(Throwable throwable) {
                            return Observable.error(ExceptionEngine.handleException(throwable));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<FeedTimeLine>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(FeedTimeLine feedTimeLine) {
                            disPlayFeedList(feedTimeLine, context, feedView, mRecyclerView);
                        }
                    });
        }
    }

    public void getNextFeed(){
        feedView = getView();
        if(feedView!=null){
            mRecyclerView = feedView.getRecyclerView();
            layoutManager = feedView.getLayoutManager();

            if(next == null){
                Log.d(TAG, "next is null");
                return;
            }
            feedApi.getNextFeed("JWT "+token, Uri.parse(next).getQueryParameter("page"))
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends FeedTimeLine>>() {
                        @Override
                        public Observable<? extends FeedTimeLine> call(Throwable throwable) {
                            return Observable.error(ExceptionEngine.handleException(throwable));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<FeedTimeLine>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(FeedTimeLine feedTimeLine) {
                            disPlayFeedList(feedTimeLine, context, feedView, mRecyclerView);
                        }
                    });
        }
    }

    public void postLike(Feed feed){
        feedView = getView();
        if(feedView!=null){
            mRecyclerView = feedView.getRecyclerView();
            layoutManager = feedView.getLayoutManager();
            Like like = new Like();
            like.setFeed_id(feed.getFeedId());
            feedApi.postLike("JWT "+ token, like)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<Object>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Response<Object> objectResponse) {
                            if(objectResponse.code() == 201){
                                adapter.updateLikeStatus(feed);
                            }else if(objectResponse.code() == 200){
                                Toast.makeText(context, "已经点过赞了", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void disPlayFeedList(FeedTimeLine feedTimeLine, Context context, IFeedView feedView, RecyclerView recyclerView) {
        if (isLoadMore) {
            if (next == null) {
                adapter.updateLoadStatus(adapter.LOAD_NONE);
                feedView.setDataRefresh(false);
                return;
            }
            else {
                timeLine.getFeeds().addAll(feedTimeLine.getFeeds());
            }
            adapter.notifyDataSetChanged();
        } else {
            timeLine = feedTimeLine;
            adapter = new FeedListAdapter(context, timeLine, new RecyclerViewClickListener() {
                @Override
                public void onAvatarClicked(int user_id, String nickname) {
                    feedView.goToUser(user_id, nickname);
                }

                @Override
                public void onLikeClicked(Feed feed) {
                    postLike(feed);
                }

                @Override
                public void onCommentClicked(int feed_id) {
                    Log.d("feedid", "feedid:" + feed_id);
                }
            });
            recyclerView.setAdapter(adapter);
            new Handler().postDelayed(() -> {
                adapter.notifyDataSetChanged();
            }, 2000);
        }
        next = feedTimeLine.getNext();
        if(next.equals("null")){
            next = null;
        }
        feedView.setDataRefresh(false);
    }

    public void scrollRecycleView() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();
                    if (layoutManager.getItemCount() == 1) {
                        adapter.updateLoadStatus(adapter.LOAD_NONE);
                        return;
                    }
                    if(next == null){
                        if(adapter != null){
                            adapter.updateLoadStatus(adapter.LOAD_NONE);
                        }
                        return;
                    }
                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        adapter.updateLoadStatus(adapter.LOAD_PULL_TO);
                        isLoadMore = true;
                        adapter.updateLoadStatus(adapter.LOAD_MORE);
                        new Handler().postDelayed(() -> getNextFeed(), 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

}
