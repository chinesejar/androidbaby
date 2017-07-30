package timeroute.androidbaby.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import timeroute.androidbaby.R;
import timeroute.androidbaby.bean.feed.Feed;

public class FeedDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        Intent intent = getIntent();
        Feed feed = (Feed) intent.getSerializableExtra("feed");
    }
}
