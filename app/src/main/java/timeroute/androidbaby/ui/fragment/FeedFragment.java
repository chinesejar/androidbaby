package timeroute.androidbaby.ui.fragment;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.ui.activity.FeedDetailActivity;
import timeroute.androidbaby.ui.activity.ImageViewActivity;
import timeroute.androidbaby.ui.activity.PostActivity;
import timeroute.androidbaby.ui.activity.UserActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.FeedPresenter;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.util.SharedPreferenceUtils;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends IBaseFragment<IFeedView, FeedPresenter> implements IFeedView {

    private static final String TAG = "FeedFragment";
    private static final int REQUEST_REFRESH = 1;

    private SharedPreferenceUtils sharedPreferenceUtils;
    private boolean mIsRequestDataRefresh = false;
    private RxPermissions rxPermissions;

    private FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.swipe_refresh)
    ABSwipeRefreshLayout abSwipeRefreshLayout;
    @Bind(R.id.feed_list)
    RecyclerView feed_list;

    @Override
    protected FeedPresenter createPresenter() {
        return new FeedPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_feed;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initView(View rootView) {
        setTracker(TAG);
        sharedPreferenceUtils = new SharedPreferenceUtils(getContext(), "user");
        mLayoutManager = new LinearLayoutManager(getContext());

        feed_list.setLayoutManager(mLayoutManager);
        rxPermissions = new RxPermissions(getActivity());
        floatingActionButton = getActivity().findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> {
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if(granted){
                            startActivityForResult(new Intent(getContext(), PostActivity.class), REQUEST_REFRESH);
                        }else {
                            Toast.makeText(getContext(), getString(R.string.permission), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        if(!sharedPreferenceUtils.getBoolean("post_feed_tip")){
            displayTip();
            sharedPreferenceUtils.setBoolean("post_feed_tip", true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.scrollRecycleView();
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
    }

    @Override
    public void setDataRefresh(boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public void setRefresh(boolean requestDataRefresh) {
        if (abSwipeRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            abSwipeRefreshLayout.setRefreshing(false);
        } else {
            abSwipeRefreshLayout.setRefreshing(true);
            mPresenter.getLatestFeed();
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return feed_list;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    public void goToUser(int user_id, String nickname, String assignment, String avatar) {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("nickname", nickname);
        intent.putExtra("assignment", assignment);
        intent.putExtra("avatar", avatar);
        startActivityForResult(intent, REQUEST_REFRESH);
    }

    @Override
    public void goToImageView(int i, String[] images){
        Intent intent = new Intent(getContext(), ImageViewActivity.class);
        intent.putExtra("index", i);
        intent.putExtra("images", images);
        startActivity(intent);
    }

    @Override
    public void goToFeedDetail(Feed feed){
        Intent intent = new Intent(getContext(), FeedDetailActivity.class);
        intent.putExtra("feed", feed);
        startActivityForResult(intent, REQUEST_REFRESH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "result code: "+resultCode);
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case REQUEST_REFRESH:
                if (data.getExtras().getString("type", "").equals("refresh")){
                    setDataRefresh(true);
                }
                break;
        }
    }

    public void displayTip(){
        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(floatingActionButton, getString(R.string.post_tip_title), getString(R.string.post_tip_content))
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimaryDark)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.colorPrimaryDark)  // Specify the color of the description text
                        .textColor(R.color.textColorDarkGrey)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(getActivity().getResources().getDrawable(R.drawable.ic_mode_edit))                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        //doSomething();
                    }
                });
    }
}
