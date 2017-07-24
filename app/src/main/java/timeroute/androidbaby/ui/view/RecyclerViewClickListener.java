package timeroute.androidbaby.ui.view;

/**
 * Created by chinesejar on 17-7-24.
 */

public interface RecyclerViewClickListener {
    void onAvatarClicked(int user_id);
    void onLikeClicked(int feed_id);
    void onCommentClicked(int feed_id);
}
