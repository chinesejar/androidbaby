package timeroute.androidbaby.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.ImageViewPresenter;
import timeroute.androidbaby.ui.view.IImageViewView;

public class ImageViewActivity extends IBaseActivity<IImageViewView, ImageViewPresenter> implements IImageViewView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imageview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_download:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected ImageViewPresenter createPresenter() {
        return new ImageViewPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_image_view;
    }
}
