package timeroute.androidbaby.bean.user;

import java.io.Serializable;

/**
 * Created by chinesejar on 17-7-15.
 */

public class User implements Serializable {
    private int id;
    private String username;
    private String nickname = "匿名的安卓宝宝";

    public int getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public String getNickname(){
        return nickname;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setNickname(String nickname){
        if(nickname.length()!=0){
            this.nickname = nickname;
        }
    }
}
