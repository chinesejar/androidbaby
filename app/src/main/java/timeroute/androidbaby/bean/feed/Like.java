package timeroute.androidbaby.bean.feed;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timeroute.androidbaby.bean.user.Profile;

/**
 * Created by chinesejar on 17-7-24.
 */

public class Like implements Serializable {
    private Feed feed_id;
    private Profile creator;
    private String create_time;

    public Feed getFeed_id() {
        return feed_id;
    }

    public Profile getCreator() {
        return creator;
    }

    public String getCreate_time() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = format.parse(create_time);
            long timestamp = date.getTime();
            long cur_timestamp = System.currentTimeMillis();
            long diff = (cur_timestamp - timestamp) / 1000;
            if (diff < 60) {
                return String.valueOf(diff + 1) + " 秒前";
            } else if (diff / 60 < 60) {
                return String.valueOf(diff / 60 + 1) + " 分钟前";
            } else if (diff / 3600 < 24) {
                return String.valueOf(diff / 3600 + 1) + " 小时前";
            } else {
                return String.valueOf(diff / 86400 + 1) + " 天前";
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "未知时间";
        }
    }

    public void setFeed_id(Feed feed_id) {
        this.feed_id = feed_id;
    }

    @Override
    public String toString() {
        return "Like{" +
                "feed_id='" + feed_id + '\'' +
                ", creator=" + creator +
                ", create_time='" + create_time +
                '}';
    }
}
