package timeroute.androidbaby.bean.user;


/**
 * Created by chinesejar on 17-7-15.
 */

public class Profile extends User {
    private String email;
    private String nickname;
    private String assignment;
    private String avatar;
    private String gender;


    public String getEmail() {
        return email;
    }

    public String getNickname(){
        return nickname;
    }

    public String getAssignment() {
        return assignment;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname){
        if(nickname.length()!=0){
            this.nickname = nickname;
        }
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString(){
        return "Profile{"+
                "username='"+getUsername()+'\''+
                "nickname='"+nickname+'\''+
                "avatar='"+avatar+'\''+
                "}";
    }
}
