package timeroute.androidbaby.ui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import timeroute.androidbaby.bean.feed.Feed;

/**
 * Created by chinesejar on 17-7-14.
 */

public interface IFeedView {
    void setDataRefresh(boolean refresh);
    RecyclerView getRecyclerView();
    LinearLayoutManager getLayoutManager();

    void goToUser(int user_id, String nickname, String assignment, String avatar);
    void goToFeedDetail(Feed feed);
    void goToImageView(int i, String[] images);
}
