package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.ui.adapter.FeedListAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IFeedView;

/**
 * Created by chinesejar on 17-7-14.
 */

public class FeedPresenter extends BasePresenter<IFeedView> {

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
    }

    public void getLatestFeed(){
        feedView = getView();
        if(feedView != null){
            mRecyclerView = feedView.getRecyclerView();
            layoutManager = feedView.getLayoutManager();

            feedApi.getLatestFeed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(feedTimeLine -> {
                        disPlayFeedList(feedTimeLine, context, feedView, mRecyclerView);
                    }, this::loadError);
        }
    }

    public void getNextFeed(){
        feedView = getView();
        if(feedView!=null){
            mRecyclerView = feedView.getRecyclerView();
            layoutManager = feedView.getLayoutManager();

            feedApi.getNextFeed(next.substring(next.length()-1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(feedTimeLine -> {
                        disPlayFeedList(feedTimeLine, context, feedView, mRecyclerView);
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, "网络有问题", Toast.LENGTH_SHORT).show();
    }

    private void disPlayFeedList(FeedTimeLine feedTimeLine, Context context, IFeedView feedView, RecyclerView recyclerView) {
        Log.d("serializer", feedTimeLine.toString());
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
            adapter = new FeedListAdapter(context, timeLine);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        next = feedTimeLine.getNext();
        if(next == "null"){
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
                        adapter.updateLoadStatus(adapter.LOAD_NONE);
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
