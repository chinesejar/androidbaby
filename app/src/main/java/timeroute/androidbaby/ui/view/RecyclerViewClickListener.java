package timeroute.androidbaby.ui.view;

import timeroute.androidbaby.bean.feed.Feed;

/**
 * Created by chinesejar on 17-7-24.
 */

public interface RecyclerViewClickListener {
    void onAvatarClicked(int user_id, String nickname, String assignment, String avatar);
    void onLikeClicked(Feed feed);
    void onCommentClicked(int feed_id);
}
