package timeroute.androidbaby.ui.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;
import timeroute.androidbaby.MyApp;
import timeroute.androidbaby.R;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;

/**
 * Created by chinesejar on 17-7-14.
 */

public abstract class IBaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    protected T mPresenter;

    private Tracker mTracker;
    private boolean mIsRequestDataRefresh = false;
    private ABSwipeRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(createViewLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
        mTracker = getDefaultTracker();
        initView(rootView);
        if(isSetRefresh()){
            setupSwipeRefresh(rootView);
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void setupSwipeRefresh(View view){
        mRefreshLayout = view.findViewById(R.id.swipe_refresh);
        if(mRefreshLayout != null){
            mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,getResources().getDisplayMetrics()));
            mRefreshLayout.setOnRefreshListener(this::requestDataRefresh);
        }
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    public void setTracker(String name){
        mTracker.setScreenName("Fragment: " + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(getContext());
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public void setRefresh(boolean requestDataRefresh) {
        if (mRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            mRefreshLayout.postDelayed(() -> {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        } else {
            mRefreshLayout.setRefreshing(true);
        }
    }

    protected abstract T createPresenter();

    protected abstract int createViewLayoutId();

    protected  void initView(View rootView){}

    public Boolean isSetRefresh(){
        return true;
    }

}
