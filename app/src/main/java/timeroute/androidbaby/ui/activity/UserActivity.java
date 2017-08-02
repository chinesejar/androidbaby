package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.UserPresenter;
import timeroute.androidbaby.ui.view.AppBarStateChangeListener;
import timeroute.androidbaby.ui.view.IUserView;
import timeroute.androidbaby.util.RoundTransform;

public class UserActivity extends IBaseActivity<IUserView, UserPresenter> implements IUserView {

    private static final int REQUEST_REFRESH = 1;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsRequestDataRefresh = false;
    private int user_id;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.avatar)
    ImageView imageViewAvatar;
    @Bind(R.id.nickname)
    TextView textViewNickname;
    @Bind(R.id.assignment)
    TextView textViewAssignment;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setupSwipeRefresh();

        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", -1);
        String nickname = intent.getStringExtra("nickname");
        String assignment = intent.getStringExtra("assignment");
        String avatar = intent.getStringExtra("avatar");
        textViewNickname.setText(nickname);
        textViewAssignment.setText(assignment);
        collapsingToolbarLayout.setTitle(nickname);
        Picasso.with(this)
                .load(avatar)
                .transform(new RoundTransform())
                .into(imageViewAvatar);
        if(user_id >= 0){
            Log.d("USER", String.valueOf(user_id));
            setDataRefresh(true);
            mPresenter.scrollRecycleView();
        }

        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    Log.d("STATE", state.name());
                    collapsingToolbarLayout.setTitleEnabled(false);
                }else if(state == State.COLLAPSED){
                    Log.d("STATE", state.name());
                }else {
                    Log.d("STATE", state.name());
                    collapsingToolbarLayout.setTitleEnabled(true);
                }
            }
        });
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
        setDataRefresh(true);
    }

    public void setRefresh(boolean requestDataRefresh) {
        if (mRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            mRefreshLayout.setRefreshing(false);
        } else {
            mRefreshLayout.setRefreshing(true);
            mPresenter.getLatestUserFeed(user_id);
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

    @Override
    public int getUserId() {
        return user_id;
    }

    @Override
    public void goToImageView(int i, String[] images){
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra("index", i);
        intent.putExtra("images", images);
        startActivity(intent);
    }

    @Override
    public void goToFeedDetail(Feed feed) {
        Intent intent = new Intent(this, FeedDetailActivity.class);
        intent.putExtra("feed", feed);
        startActivityForResult(intent, REQUEST_REFRESH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_REFRESH:
                setDataRefresh(true);
                break;
        }
    }

}
