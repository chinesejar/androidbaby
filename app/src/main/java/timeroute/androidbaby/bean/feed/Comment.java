package timeroute.androidbaby.bean.feed;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import timeroute.androidbaby.bean.user.Profile;

/**
 * Created by chinesejar on 17-7-8.
 */

public class Comment implements Serializable {

    private Feed feed_id;
    private Profile creator;
    private Profile at;
    private String content;
    private boolean is_read;
    private String create_time;

    public Feed getFeedId() {
        return feed_id;
    }

    public Profile getCreator() {
        return creator;
    }

    public Profile getAt() {
        return at;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsRead() {
        return is_read;
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

    public void setFeedId(Feed feed_id) {
        this.feed_id = feed_id;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setAt(Profile at) {
        this.at = at;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "feed_id='" + feed_id + '\'' +
                ", creator=" + creator +
                ", at=" + at +
                ", content='" + content + '\'' +
                ", create_time='" + create_time +
                '}';
    }
}
