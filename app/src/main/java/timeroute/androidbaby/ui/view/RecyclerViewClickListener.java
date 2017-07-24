package timeroute.androidbaby.ui.view;

import timeroute.androidbaby.bean.feed.Feed;

/**
 * Created by chinesejar on 17-7-24.
 */

public interface RecyclerViewClickListener {
    void onAvatarClicked(int user_id);
    void onLikeClicked(Feed feed);
    void onCommentClicked(int feed_id);
}
