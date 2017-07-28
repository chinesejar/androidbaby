package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.Bind;
import me.relex.circleindicator.CircleIndicator;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.adapter.ViewPagerImageAdapter;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.ImageViewPresenter;
import timeroute.androidbaby.ui.view.IImageViewView;
import timeroute.androidbaby.ui.view.ImageViewClickListener;
import timeroute.androidbaby.widget.TouchImageView;

public class ImageViewActivity extends IBaseActivity<IImageViewView, ImageViewPresenter> implements IImageViewView {

    private static final String TAG = "ImageViewActivity";
    private ArrayList<String> arrayImages;
    private ViewPagerImageAdapter imageAdapter;
    private ImageViewClickListener imageViewClickListener;
    private boolean isShow = true;

    @Bind(R.id.viewPagerImages)
    ViewPager viewPager;
    @Bind(R.id.indicator)
    CircleIndicator indicator;

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
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        actionBar.setElevation(0);

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);
        Log.d(TAG, "index: " + index);
        arrayImages = new ArrayList<String>();
        String[] images = intent.getStringArrayExtra("images");
        for (int i = 0; i < images.length; i++) {
            Log.d(TAG, "image: " + images[i]);
            arrayImages.add(images[i]);
        }
        imageViewClickListener = () -> {
            if(isShow){
                getSupportActionBar().hide();
            }else {
                getSupportActionBar().show();
            }
            isShow = !isShow;
        };
        imageAdapter = new ViewPagerImageAdapter(this, arrayImages, imageViewClickListener);
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(index);
        indicator.setViewPager(viewPager);
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
                ImageView imageView = (TouchImageView) imageAdapter.getPrimaryItem().findViewById(R.id.image);
                mPresenter.saveImage(imageView.getDrawable());
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
