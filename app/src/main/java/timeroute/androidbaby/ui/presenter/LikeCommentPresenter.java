package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.feed.CommentTimeLine;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.LikeTimeLine;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.adapter.CommentNotificationListAdapter;
import timeroute.androidbaby.ui.adapter.LikeNotificationListAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.ILikeCommentView;
import timeroute.androidbaby.ui.view.RecyclerViewClickListener;
import timeroute.androidbaby.util.SharedPreferenceUtils;

import static timeroute.androidbaby.ui.adapter.FeedListAdapter.LOAD_MORE;
import static timeroute.androidbaby.ui.adapter.FeedListAdapter.LOAD_NONE;
import static timeroute.androidbaby.ui.adapter.FeedListAdapter.LOAD_PULL_TO;

/**
 * Created by chinesejar on 17-7-14.
 */

public class LikeCommentPresenter extends BasePresenter<ILikeCommentView> {

    private static final String TAG = "LikeCommentPresenter";

    private SharedPreferenceUtils sharedPreferenceUtils;
    private String token;

    private Context context;
    private ILikeCommentView likeCommentView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private CommentTimeLine timeLine_comment;
    private LikeTimeLine timeLine_like;
    private CommentNotificationListAdapter commentNotificationListAdapter;
    private LikeNotificationListAdapter likeNotificationListAdapter;
    private int lastVisibleItem;
    private boolean isLoadMore = false;
    private String next;

    public LikeCommentPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
        token = sharedPreferenceUtils.getString("token");
    }

    public void getLatestCommentNotification(){
        likeCommentView = getView();
        if(likeCommentView != null){
            mRecyclerView = likeCommentView.getRecyclerView();
            layoutManager = likeCommentView.getLayoutManager();

            feedApi.getLatestCommentNotification("JWT "+token)
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<CommentTimeLine>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(CommentTimeLine commentTimeLine) {
                            Log.d(TAG, commentTimeLine.toString());
                            disPlayCommentList(commentTimeLine, likeCommentView, mRecyclerView);
                        }
                    });
        }
    }

    public void getNextCommentNotification(){
        likeCommentView = getView();
        if(likeCommentView!=null){
            mRecyclerView = likeCommentView.getRecyclerView();
            layoutManager = likeCommentView.getLayoutManager();

            if(next == null){
                Log.d(TAG, "next is null");
                return;
            }
            feedApi.getNextCommentNotification("JWT "+token, Uri.parse(next).getQueryParameter("page"))
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<CommentTimeLine>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(CommentTimeLine commentTimeLine) {
                            disPlayCommentList(commentTimeLine, likeCommentView, mRecyclerView);
                        }
                    });
        }
    }

    public void getLatestLikeNotification(){
        likeCommentView = getView();
        if(likeCommentView != null){
            mRecyclerView = likeCommentView.getRecyclerView();
            layoutManager = likeCommentView.getLayoutManager();

            feedApi.getLatestLikeNotification("JWT "+token)
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<LikeTimeLine>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(LikeTimeLine likeTimeLine) {
                            disPlayLikeList(likeTimeLine, likeCommentView, mRecyclerView);
                        }
                    });
        }
    }

    public void getNextLikeNotification(){
        likeCommentView = getView();
        if(likeCommentView!=null){
            mRecyclerView = likeCommentView.getRecyclerView();
            layoutManager = likeCommentView.getLayoutManager();

            if(next == null){
                Log.d(TAG, "next is null");
                return;
            }
            feedApi.getNextLikeNotification("JWT "+token, Uri.parse(next).getQueryParameter("page"))
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<LikeTimeLine>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(LikeTimeLine likeTimeLine) {
                            disPlayLikeList(likeTimeLine, likeCommentView, mRecyclerView);
                        }
                    });
        }
    }

    private void disPlayCommentList(CommentTimeLine commentTimeLine, ILikeCommentView likeCommentView, RecyclerView recyclerView) {
        if (isLoadMore) {
            if (next == null) {
                commentNotificationListAdapter.updateLoadStatus(LOAD_NONE);
                likeCommentView.setDataRefresh(false);
                return;
            }
            else {
                timeLine_comment.getComments().addAll(commentTimeLine.getComments());
            }
            commentNotificationListAdapter.notifyDataSetChanged();
        } else {
            timeLine_comment = commentTimeLine;
            commentNotificationListAdapter = new CommentNotificationListAdapter(context, timeLine_comment, new RecyclerViewClickListener() {
                @Override
                public void onAvatarClicked(int user_id, String nickname, String assignment, String avatar) {

                }

                @Override
                public void onLikeClicked(Feed feed) {

                }

                @Override
                public void onCommentClicked(Feed feed) {

                }

                @Override
                public void onCardViewClick(Feed feed) {
                    likeCommentView.goToFeedDetail(feed);
                }

                @Override
                public void onImageViewClick(int i, String[] images) {

                }

                @Override
                public void onAtClicked(Profile profile) {

                }
            });
            recyclerView.setAdapter(commentNotificationListAdapter);
            commentNotificationListAdapter.notifyDataSetChanged();
        }
        next = commentTimeLine.getNext();
        if(next.equals("null")){
            next = null;
        }
        likeCommentView.setDataRefresh(false);
    }

    private void disPlayLikeList(LikeTimeLine likeTimeLine, ILikeCommentView likeCommentView, RecyclerView recyclerView) {
        if (isLoadMore) {
            if (next == null) {
                likeNotificationListAdapter.updateLoadStatus(LOAD_NONE);
                likeCommentView.setDataRefresh(false);
                return;
            }
            else {
                timeLine_like.getLikes().addAll(likeTimeLine.getLikes());
            }
            likeNotificationListAdapter.notifyDataSetChanged();
        } else {
            timeLine_like = likeTimeLine;
            likeNotificationListAdapter = new LikeNotificationListAdapter(context, timeLine_like, new RecyclerViewClickListener() {
                @Override
                public void onAvatarClicked(int user_id, String nickname, String assignment, String avatar) {

                }

                @Override
                public void onLikeClicked(Feed feed) {

                }

                @Override
                public void onCommentClicked(Feed feed) {

                }

                @Override
                public void onCardViewClick(Feed feed) {
                    likeCommentView.goToFeedDetail(feed);
                }

                @Override
                public void onImageViewClick(int i, String[] images) {

                }

                @Override
                public void onAtClicked(Profile profile) {

                }
            });
            recyclerView.setAdapter(likeNotificationListAdapter);
            likeNotificationListAdapter.notifyDataSetChanged();
        }
        next = likeTimeLine.getNext();
        if(next.equals("null")){
            next = null;
        }
        likeCommentView.setDataRefresh(false);
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
                        if(likeCommentView.getType().equals("like")){
                            likeNotificationListAdapter.updateLoadStatus(LOAD_MORE);
                        }else if(likeCommentView.getType().equals("comment")){
                            commentNotificationListAdapter.updateLoadStatus(LOAD_MORE);
                        }
                        return;
                    }
                    if(next == null){
                        if(likeNotificationListAdapter != null){
                            likeNotificationListAdapter.updateLoadStatus(LOAD_NONE);
                        }
                        if(commentNotificationListAdapter != null){
                            commentNotificationListAdapter.updateLoadStatus(LOAD_MORE);
                        }
                        return;
                    }
                    if (lastVisibleItem + 1 == layoutManager.getItemCount()) {
                        if(likeCommentView.getType().equals("like")){
                            likeNotificationListAdapter.updateLoadStatus(LOAD_PULL_TO);
                            isLoadMore = true;
                            likeNotificationListAdapter.updateLoadStatus(LOAD_MORE);
                            getNextLikeNotification();
                        }else if(likeCommentView.getType().equals("comment")){
                            commentNotificationListAdapter.updateLoadStatus(LOAD_PULL_TO);
                            isLoadMore = true;
                            commentNotificationListAdapter.updateLoadStatus(LOAD_MORE);
                            getNextCommentNotification();
                        }
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
