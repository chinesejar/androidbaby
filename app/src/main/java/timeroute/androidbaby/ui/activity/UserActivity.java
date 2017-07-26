package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.UserPresenter;
import timeroute.androidbaby.ui.view.IUserView;

public class UserActivity extends IBaseActivity<IUserView, UserPresenter> implements IUserView {

    private LinearLayoutManager mLayoutManager;
    private boolean mIsRequestDataRefresh = false;

    @Bind(R.id.feed_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setupSwipeRefresh();

        Intent intent = getIntent();
        int user_id = intent.getIntExtra("user_id", -1);
        String nickname = intent.getStringExtra("nickname");
        getSupportActionBar().setTitle(nickname);
        if(user_id >= 0){
            Log.d("USER", String.valueOf(user_id));
            setDataRefresh(true);
            mPresenter.getLatestUserFeed(user_id);
            mPresenter.scrollRecycleView();
        }
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user;
    }

    private void setupSwipeRefresh(){
        if(mRefreshLayout != null){
            mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,getResources().getDisplayMetrics()));
            mRefreshLayout.setOnRefreshListener(this::requestDataRefresh);
        }
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
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

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void setDataRefresh(boolean b) {
        setRefresh(b);
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

}
