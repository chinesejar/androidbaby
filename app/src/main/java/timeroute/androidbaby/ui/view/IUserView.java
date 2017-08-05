package timeroute.androidbaby.ui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import timeroute.androidbaby.bean.feed.Feed;

/**
 * Created by chinesejar on 17-7-25.
 */

public interface IUserView {
    RecyclerView getRecyclerView();

    void setDataRefresh(boolean b);

    LinearLayoutManager getLayoutManager();

    int getUserId();

    void goToFeedDetail(Feed feed);

    void goToImageView(int i, String[] images);
}
