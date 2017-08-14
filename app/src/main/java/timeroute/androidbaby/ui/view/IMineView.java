package timeroute.androidbaby.ui.view;

import java.io.File;

/**
 * Created by chinesejar on 17-7-14.
 */

public interface IMineView {
    void setNotification(boolean has);
    void setAvatar(String url);
    File getAvatar();
    void goToUser(int user_id, String nickname, String assignment, String avatar);
}
