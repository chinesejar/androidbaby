package timeroute.androidbaby.bean.feed;

import java.io.Serializable;

/**
 * Created by chinesejar on 17-7-20.
 */

public class FeedPic implements Serializable{
    private String url;

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    @Override
    public String toString(){
        return "FeedPic{"+
                "url='"+url+'\''+
                "}";
    }
}
