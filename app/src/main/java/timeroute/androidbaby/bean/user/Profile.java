package timeroute.androidbaby.bean.user;

import java.io.Serializable;

/**
 * Created by chinesejar on 17-7-15.
 */

public class Profile extends User {
    private String nickname = "匿名的安卓宝宝";

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        if(nickname.length()!=0){
            this.nickname = nickname;
        }
    }
}
