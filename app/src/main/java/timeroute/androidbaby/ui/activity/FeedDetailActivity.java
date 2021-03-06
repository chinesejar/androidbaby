package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedPic;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.ui.adapter.FeedListPicAdapter;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.FeedDetailPresenter;
import timeroute.androidbaby.ui.view.IFeedDetailView;
import timeroute.androidbaby.util.RoundTransform;
import timeroute.androidbaby.util.SharedPreferenceUtils;

public class FeedDetailActivity extends IBaseActivity<IFeedDetailView, FeedDetailPresenter> implements IFeedDetailView {

    private static final String TAG="FeedDetailActivity";
    private LinearLayoutManager mLayoutManager;
    private SharedPreferenceUtils sharedPreferenceUtils;
    private boolean mIsRequestDataRefresh = false;
    private int feed_id;
    private boolean from_notification = false;

    private FeedListPicAdapter feedListPicAdapter;
    private Feed feed;
    private String[] images;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.nickname)
    TextView nickname;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.feed_pic)
    RecyclerView recyclerViewPic;
    @Bind(R.id.like)
    TextView like;
    @Bind(R.id.imageButtonLike)
    ImageButton imageButtonLike;
    @Bind(R.id.comment)
    TextView comment;
    @Bind(R.id.imageButtonComment)
    ImageButton imageButtonComment;
    @Bind(R.id.create_time)
    TextView create_time;
    @Bind(R.id.recyclerViewComment)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        setTracker(TAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        mLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPic.setLayoutManager(linearLayoutManager);
        mRecyclerView.setLayoutManager(mLayoutManager);
        sharedPreferenceUtils = new SharedPreferenceUtils(this, "user");
        setupSwipeRefresh();

        Intent intent = getIntent();
        feed = (Feed) intent.getSerializableExtra("feed");
        feed_id = feed.getFeedId();
        if(feed.getUser()!=null){
            Picasso.with(this)
                    .load(feed.getUser().getAvatar())
                    .transform(new RoundTransform())
                    .into(avatar);
            setAvatarClick(feed.getUser());
            nickname.setText(feed.getUser().getNickname());
            content.setText(feed.getContent());
            like.setText(String.valueOf(feed.getLikeCount()));
            comment.setText(String.valueOf(feed.getCommentCount()));
            setImages(feed.getFeedPic());
            if(feed_id>0){
                Log.d("feed", String.valueOf(feed_id));
                setDataRefresh(true);
                mPresenter.scrollRecycleView();
            }
            imageButtonLike.setOnClickListener(view -> mPresenter.postLike(feed));
            floatingActionButton.setOnClickListener(view -> openCommentDialog(false, null));
        }else {
            from_notification = true;
            mPresenter.getFeedDetail(feed_id);
        }
    }

    private void setAvatarClick(Profile user) {
        avatar.setOnClickListener(view -> {
            goToUser(user.getId(), user.getNickname(), user.getAssignment(), user.getAvatar());
        });
    }

    @Override
    public void openCommentDialog(boolean isReply, Profile profile) {
        View view = LayoutInflater.from(this).inflate(R.layout.alertdialog_comment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setView(view).setCancelable(true).create();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        assert window != null;
        window.setAttributes(layoutParams);
        alertDialog.setContentView(view);
        TextView comment_or_reply = view.findViewById(R.id.comment_or_reply);
        EditText editTextContent = view.findViewById(R.id.content);
        Button buttonSure = view.findViewById(R.id.sure);
        Button buttonCancel = view.findViewById(R.id.cancel);
        if(isReply){
            comment_or_reply.setText(getString(R.string.reply));
        }else {
            comment_or_reply.setText(getString(R.string.comment));
        }
        buttonCancel.setOnClickListener(view1 -> alertDialog.dismiss());
        buttonSure.setOnClickListener(view1 -> {
            mPresenter.postComment(feed_id, profile!=null?profile.getId():-1, editTextContent.getText().toString());
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    @Override
    public void updateLikeStatus() {
        like.setText(String.valueOf(Integer.valueOf(like.getText().toString())+1));
        Toast.makeText(this, getString(R.string.like_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backToParent() {
        Intent intent = new Intent();
        intent.putExtra("type", "refresh");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setFeed(Feed feed) {
        Picasso.with(this)
                .load(feed.getUser().getAvatar())
                .transform(new RoundTransform())
                .into(avatar);
        setAvatarClick(feed.getUser());
        nickname.setText(feed.getUser().getNickname());
        content.setText(feed.getContent());
        like.setText(String.valueOf(feed.getLikeCount()));
        comment.setText(String.valueOf(feed.getCommentCount()));
        setImages(feed.getFeedPic());
        if(feed_id>0){
            Log.d("feed", String.valueOf(feed_id));
            setDataRefresh(true);
            mPresenter.scrollRecycleView();
        }
        imageButtonLike.setOnClickListener(view -> {
            mPresenter.postLike(feed);
        });
        floatingActionButton.setOnClickListener(view -> {
            openCommentDialog(false, null);
        });
    }

    @Override
    public void goToUser(int user_id, String nickname, String assignment, String avatar) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("nickname", nickname);
        intent.putExtra("avatar", avatar);
        intent.putExtra("assignment", assignment);
        startActivity(intent);
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected FeedDetailPresenter createPresenter() {
        return new FeedDetailPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_feed_detail;
    }

    public void setImages(List<FeedPic> feedPics) {
        if (feedPics.size() > 0) {
            recyclerViewPic.setVisibility(View.VISIBLE);
            ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            images = new String[feedPics.size()];
            for (int i = 0; i < feedPics.size(); i++) {
                images[i] = feedPics.get(i).getUrl();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("url", feedPics.get(i).getUrl());
                list.add(map);
            }
            feedListPicAdapter = new FeedListPicAdapter(this, list);
            recyclerViewPic.setAdapter(feedListPicAdapter);
        }
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
            mPresenter.getLatestComment(feed_id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(from_notification){
            getMenuInflater().inflate(R.menu.feed_detail, menu);
        }else {
            int feed_id = sharedPreferenceUtils.getInt("id");
            if(feed_id == feed.getUser().getId()) {
                getMenuInflater().inflate(R.menu.feed_detail, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                mPresenter.deleteFeed(feed);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
    public int getFeedId() {
        return feed_id;
    }
}
