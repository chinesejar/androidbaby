package timeroute.androidbaby.ui.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.AboutPresenter;
import timeroute.androidbaby.ui.view.IAboutView;

public class AboutActivity extends IBaseActivity<IAboutView, AboutPresenter> implements IAboutView {

    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        AboutView view = AboutBuilder.with(this)
                .setPhoto(R.drawable.avatar)
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

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected AboutPresenter createPresenter(){
        return new AboutPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about;
    }
}
