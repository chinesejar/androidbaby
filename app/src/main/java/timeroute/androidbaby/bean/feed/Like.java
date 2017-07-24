package timeroute.androidbaby.bean.feed;

import java.io.Serializable;

/**
 * Created by chinesejar on 17-7-24.
 */

public class Like implements Serializable {
    private int feed_id;

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }
}
