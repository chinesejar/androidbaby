package timeroute.androidbaby.bean.user;

import java.io.Serializable;

/**
 * Created by chinesejar on 17-7-16.
 */

public class Token implements Serializable {
    private String token;
    private Profile profile;

    public String getToken(){
        return token;
    }

    public Profile getProfile(){ return profile; }

    public void setToken(String token){
        this.token = token;
    }

    public void setProfile(Profile profile){this.profile = profile;}
}
