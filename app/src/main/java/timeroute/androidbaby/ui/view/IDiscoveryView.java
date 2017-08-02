package timeroute.androidbaby.ui.view;


import timeroute.androidbaby.bean.feed.FeedTimeLine;
import timeroute.androidbaby.widget.ABSwipeRefreshLayout;
import timeroute.androidbaby.widget.OrientedViewPager;

/**
 * Created by chinesejar on 17-7-14.
 */

public interface IDiscoveryView {
    void setDataRefresh(boolean refresh);
    void setFeedList(FeedTimeLine feedTimeLine);

    OrientedViewPager getViewPager();

    ABSwipeRefreshLayout getSwipeLayout();
}
