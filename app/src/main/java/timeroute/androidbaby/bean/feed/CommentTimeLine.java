package timeroute.androidbaby.bean.feed;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chinesejar on 17-7-14.
 */

public class CommentTimeLine implements Serializable {

    private String count;
    private String next;
    private List<Comment> comments;

    public String getCount() {
        return count;
    }

    public String getNext() {
        if(next == null){
            return "null";
        }else{
            return next;
        }
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "FeedTimeLine{" +
                "count='" + count + '\'' +
                ",next='" + next + '\'' +
                ",comments=" + comments +
                "}";
    }
}
