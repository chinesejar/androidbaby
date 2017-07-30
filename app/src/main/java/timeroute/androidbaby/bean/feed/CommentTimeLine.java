package timeroute.androidbaby.bean.feed;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chinesejar on 17-7-14.
 */

public class CommentTimeLine implements Serializable {

    private String count;
    private String next;
    private List<Comment> results;

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
        return results;
    }

    @Override
    public String toString() {
        return "CommentTimeLine{" +
                "count='" + count + '\'' +
                ",next='" + next + '\'' +
                ",results=" + results +
                "}";
    }
}
