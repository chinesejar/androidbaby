package timeroute.androidbaby.ui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import timeroute.androidbaby.bean.feed.Feed;

/**
 * Created by chinesejar on 17-7-25.
 */

public interface ILikeCommentView {

    void setDataRefresh(boolean refresh);
    RecyclerView getRecyclerView();
    LinearLayoutManager getLayoutManager();
    String getType();

    void goToFeedDetail(Feed feed);
}
