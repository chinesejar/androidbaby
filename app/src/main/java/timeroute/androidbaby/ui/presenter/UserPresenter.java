package timeroute.androidbaby.ui.presenter;

import android.content.Context;
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
import rx.schedulers.Schedulers;
import timeroute.androidbaby.R;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.bean.feed.Like;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.adapter.FeedListAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IUserView;
import timeroute.androidbaby.ui.view.RecyclerViewClickListener;
import timeroute.androidbaby.util.SharedPreferenceUtils;

import static timeroute.androidbaby.ui.adapter.FeedListAdapter.LOAD_MORE;
import static timeroute.androidbaby.ui.adapter.FeedListAdapter.LOAD_NONE;
import static timeroute.androidbaby.ui.adapter.FeedListAdapter.LOAD_PULL_TO;

/**
 * Created by chinesejar on 17-7-25.
 */

public class UserPresenter extends BasePresenter<IUserView> {

    private static final String TAG = "UserPresenter";
    private SharedPreferenceUtils sharedPreferenceUtils;
    private String token;

    private Context context;
    private IUserView userView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private FeedTimeLine timeLine;
    private FeedListAdapter adapter;
    private int lastVisibleItem;
    private boolean isLoadMore = false;
    private String next;

    public UserPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
        token = sharedPreferenceUtils.getString("token");
    }

    public void getLatestUserFeed(int user_id){
        userView = getView();
        if(userView != null){
            mRecyclerView = userView.getRecyclerView();
            layoutManager = userView.getLayoutManager();

            feedApi.getLatestUserFeed("JWT "+token, user_id)
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
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
                            disPlayFeedList(feedTimeLine, context, userView, mRecyclerView);
                        }
                    });
        }
    }

    public void getNextUserFeed(int user_id){
        userView = getView();
        if(userView != null){
            mRecyclerView = userView.getRecyclerView();
            layoutManager = userView.getLayoutManager();

            if(next == null){
                Log.d(TAG, "next is null");
                return;
            }
            feedApi.getNextUserFeed("JWT "+token, Uri.parse(next).getQueryParameter("page"), user_id)
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
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
                            disPlayFeedList(feedTimeLine, context, userView, mRecyclerView);
                        }
                    });
        }
    }

    public void postLike(Feed feed){
        userView = getView();
        if(userView!=null){
            mRecyclerView = userView.getRecyclerView();
            layoutManager = userView.getLayoutManager();
            Like like = new Like();
            like.setFeed_id(feed);
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
                                Toast.makeText(context, context.getString(R.string.like_again), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void disPlayFeedList(FeedTimeLine feedTimeLine, Context context, IUserView userView, RecyclerView recyclerView) {
        Log.d(TAG, "next: "+next);
        if (isLoadMore) {
            if (next == null) {
                adapter.updateLoadStatus(LOAD_NONE);
                userView.setDataRefresh(false);
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
                public void onAvatarClicked(int user_id, String nickname, String assignment, String avatar) {
                }

                @Override
                public void onLikeClicked(Feed feed) {
                    postLike(feed);
                }

                @Override
                public void onCommentClicked(Feed feed) {
                    Log.d("feedid", "feedid:" + feed.getFeedId());
                    userView.goToFeedDetail(feed);
                }

                @Override
                public void onCardViewClick(Feed feed) {
                    userView.goToFeedDetail(feed);
                }

                @Override
                public void onImageViewClick(int i, String[] images) {
                    userView.goToImageView(i, images);
                }

                @Override
                public void onAtClicked(Profile profile) {
                }
            });
            recyclerView.setAdapter(adapter);
            new Handler().postDelayed(() -> {
                adapter.notifyDataSetChanged();
            }, 2000);
        }
        next = feedTimeLine.getNext();
        if(next == "null"){
            next = null;
        }
        userView.setDataRefresh(false);
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
                        adapter.updateLoadStatus(LOAD_NONE);
                        return;
                    }
                    if(next == null){
                        if(adapter != null){
                            adapter.updateLoadStatus(LOAD_NONE);
                        }
                        return;
                    }
                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        adapter.updateLoadStatus(LOAD_PULL_TO);
                        isLoadMore = true;
                        adapter.updateLoadStatus(LOAD_MORE);
                        new Handler().postDelayed(() -> getNextUserFeed(userView.getUserId()), 1000);
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
