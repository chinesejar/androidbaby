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

public class Feed implements Serializable {

    private int id;
    private Profile creator;
    private String content;
    private List<FeedPic> feed_pic;
    private int like_count = 0;
    private int comment_count = 0;
    private String create_time;

    public int getFeedId() {
        return id;
    }

    public Profile getUser() {
        return creator;
    }

    public String getContent() {
        return content;
    }

    public List<FeedPic> getFeedPic(){
        return feed_pic;
    }

    public int getLikeCount() {
        return like_count;
    }

    public int getCommentCount() {
        return comment_count;
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

    public void setFeedId(int id) {
        this.id = id;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setFeedPic(List<FeedPic> feed_pic){
        this.feed_pic = feed_pic;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public void setComment_count(int comment_count){
        this.comment_count = comment_count;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id='" + id + '\'' +
                ", creator=" + creator +
                ", content='" + content + '\'' +
                ", feed_pic=" + feed_pic +
                ", like_count='" + like_count + '\'' +
                ", comment_count='" + comment_count + '\'' +
                ", create—_time='" + create_time +
                '}';
    }
}
