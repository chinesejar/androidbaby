package timeroute.androidbaby.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

import timeroute.androidbaby.R;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "AboutActivity";
    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null && mAppBar != null) {
            setSupportActionBar(mToolbar); //把Toolbar当做ActionBar给设置
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            if (Build.VERSION.SDK_INT >= 21) {
                mAppBar.setElevation(10.6f);//Z轴浮动
            }
        }

        AboutView view = AboutBuilder.with(this)
                .setPhoto(R.mipmap.ic_launcher)
                .setCover(R.mipmap.profile_cover)
                .setName(getString(R.string.team))
                .setSubTitle(getString(R.string.slogan))
                .setBrief(getString(R.string.introduce))
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addGooglePlayStoreLink("")
                .addGitHubLink(getString(R.string.social_user))
                .addFacebookLink(getString(R.string.social_user))
                .addGooglePlusLink(getString(R.string.social_user))
                .addTwitterLink(getString(R.string.social_user))
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.about);
        frameLayout.addView(view);
    }
}
