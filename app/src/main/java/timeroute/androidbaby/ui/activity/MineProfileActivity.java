package timeroute.androidbaby.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseActivity;

public class MineProfileActivity extends IBaseActivity {

    @Bind(R.id.layout_nickname)
    LinearLayout layoutNickname;
    @Bind(R.id.nickname)
    TextView textViewNickname;
    @Bind(R.id.layout_assignment)
    LinearLayout layoutAssignment;
    @Bind(R.id.assignment)
    TextView textViewAssignment;
    @Bind(R.id.layout_gender)
    LinearLayout layoutGender;
    @Bind(R.id.gender)
    TextView textViewGender;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_mine_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean canBack() {
        return true;
    }
}
