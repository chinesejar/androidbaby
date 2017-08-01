package timeroute.androidbaby.bean.user;


/**
 * Created by chinesejar on 17-7-15.
 */

public class Register extends User {
    private String code;

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    @Override
    public String toString(){
        return "Profile{"+
                "email='"+getEmail()+'\''+
                "code='"+code+'\''+
                "}";
    }
}
