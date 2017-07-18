package timeroute.androidbaby.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.entity.UncapableCause;

import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseActivity;

public class PostActivity extends IBaseActivity {

    private static final String TAG = "PostActivity";

    @Bind(R.id.imageButtonPick)
    ImageButton imageButtonPick;
    @Bind(R.id.imageButtonSend)
    ImageButton imageButtonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        imageButtonPick.setOnClickListener(view -> {
            Matisse.from(this)
                    .choose(MimeType.allOf())
                    .countable(true)
                    .addFilter(new Filter() {
                        @Override
                        protected Set<MimeType> constraintTypes() {
                            return new HashSet<MimeType>() {{
                                add(MimeType.GIF);
                            }};
                        }

                        @Override
                        public UncapableCause filter(Context context, Item item) {
                            return null;
                        }
                    })
                    .maxSelectable(9)
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(20);
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_post;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case 20:
                Log.d(TAG, "haha");
                break;
        }
    }
}
