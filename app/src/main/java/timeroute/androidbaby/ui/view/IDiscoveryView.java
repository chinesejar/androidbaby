package timeroute.androidbaby.ui.view;


import timeroute.androidbaby.bean.feed.FeedTimeLine;

/**
 * Created by chinesejar on 17-7-14.
 */

public interface IDiscoveryView {
    void setDataRefresh(boolean refresh);
    void setFeedList(FeedTimeLine feedTimeLine);
}
