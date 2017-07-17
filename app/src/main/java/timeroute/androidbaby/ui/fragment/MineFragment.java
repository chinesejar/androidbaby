package timeroute.androidbaby.ui.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.activity.MineProfileActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.MinePresenter;
import timeroute.androidbaby.ui.view.IMineView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends IBaseFragment<IMineView, MinePresenter> {

    private LinearLayoutManager mLayoutManager;
    private String avatar;
    private String nickname;
    private String assignment;

    @Bind(R.id.avatar)
    ImageView avatar_imageView;
    @Bind(R.id.nickname)
    TextView nickname_TextView;
    @Bind(R.id.assignment)
    TextView assignment_TextView;
    @Bind(R.id.mine_feed)
    LinearLayout feed_layout;
    @Bind(R.id.mine_profile)
    LinearLayout profile_layout;
    @Bind(R.id.mine_setting)
    LinearLayout setting_layout;
    @Bind(R.id.mine_about)
    LinearLayout about_layout;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View rootView) {
        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(getContext(), "user");
        avatar = sharedPreferenceUtils.getString("avatar");
        if(avatar != null){
            Glide.with(getContext()).load(avatar).asBitmap().into(avatar_imageView);
        }
        nickname = sharedPreferenceUtils.getString("nickname");
        nickname_TextView.setText(nickname);
        assignment = sharedPreferenceUtils.getString("assignment");
        if(assignment.length() == 0){
            assignment_TextView.setText("完善个性签名");
        }else {
            assignment_TextView.setText(assignment);
        }
        mLayoutManager = new LinearLayoutManager(getContext());
        profile_layout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MineProfileActivity.class);
            startActivity(intent);
        });
    }
}
