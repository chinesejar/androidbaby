package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.widget.Toast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.bean.user.UserToken;
import timeroute.androidbaby.bean.user.User;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.ISplashView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class SplashPresenter extends BasePresenter<ISplashView> {

    private Context context;
    private ISplashView splashView;

    public SplashPresenter(Context context){
        this.context = context;
    }

    public void getToken(String username, String password){
        splashView = getView();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if(splashView != null){
            userApi.getToken(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userToken -> {
                        displayToken(userToken, username, password);
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void displayToken(UserToken userToken, String username, String password) {
        Profile profile = userToken.getProfile();
        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context, "user");
        sharedPreferenceUtils.setString("userToken", userToken.getToken());
        sharedPreferenceUtils.setString("username", username);
        sharedPreferenceUtils.setString("password", password);
        sharedPreferenceUtils.setLong("last_login", System.currentTimeMillis());
        sharedPreferenceUtils.setInt("id", profile.getId());
        sharedPreferenceUtils.setString("nickname", profile.getNickname());
        sharedPreferenceUtils.setString("assignment", profile.getAssignment());
        sharedPreferenceUtils.setString("gender", profile.getGender());
        sharedPreferenceUtils.setString("avatar", profile.getAvatar());
        sharedPreferenceUtils.setString("email", profile.getEmail());
        splashView.toMain();
    }
}
