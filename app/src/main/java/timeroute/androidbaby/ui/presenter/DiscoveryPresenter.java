package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.adapter.FeedListAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IDiscoveryView;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.util.SharedPreferenceUtils;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;
import timeroute.androidbaby.widget.OrientedViewPager;

/**
 * Created by chinesejar on 17-7-14.
 */

public class DiscoveryPresenter extends BasePresenter<IDiscoveryView> {

    private static final String TAG = "DiscoveryPresenter";

    private SharedPreferenceUtils sharedPreferenceUtils;
    private String token;

    private Context context;
    private IDiscoveryView discoveryView;

    public DiscoveryPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
        token = sharedPreferenceUtils.getString("token");
    }

    public void getHotFeed() {
        discoveryView = getView();
        if(discoveryView != null){
            feedApi.getHotFeed("JWT "+token, "like")
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
                            discoveryView.setFeedList(feedTimeLine);
                        }
                    });
        }
    }

    public void scrollSwipeLayout() {
        OrientedViewPager orientedViewPager = discoveryView.getViewPager();
        ABSwipeRefreshLayout swipeRefreshLayout = discoveryView.getSwipeLayout();
        orientedViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position != 0){
                    swipeRefreshLayout.setEnabled(false);
                }else {
                    swipeRefreshLayout.setEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
