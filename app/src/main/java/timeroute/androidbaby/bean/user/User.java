package timeroute.androidbaby.bean.user;

import java.io.Serializable;

/**
 * Created by chinesejar on 17-7-15.
 */

public class User implements Serializable {
    private int id;
    private String email;
    private String password;

    public int getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
