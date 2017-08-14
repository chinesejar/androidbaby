package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.widget.Toast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.bean.user.UserToken;
import timeroute.androidbaby.bean.user.User;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.ISplashView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class SplashPresenter extends BasePresenter<ISplashView> {

    private SharedPreferenceUtils sharedPreferenceUtils;

    private Context context;
    private ISplashView splashView;

    public SplashPresenter(Context context){
        this.context = context;
    }

    public void getToken(String email, String password){
        splashView = getView();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        if(splashView != null){
            userApi.getToken(user)
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<UserToken>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(UserToken userToken) {
                            displayToken(userToken, email, password);
                        }
                    });
        }
    }

    public void refreshToken(String token){
        splashView = getView();
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        if(splashView != null){
            userApi.refreshToken(userToken)
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<UserToken>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(UserToken userToken) {
                            displayRefreshToken(userToken);
                        }
                    });
        }
    }

    private void displayRefreshToken(UserToken userToken) {
        Profile profile = userToken.getProfile();
        sharedPreferenceUtils = new SharedPreferenceUtils(context, "user");
        String last_token = sharedPreferenceUtils.getString("token");
        if(!last_token.equals(userToken.getToken())){
            sharedPreferenceUtils.setString("token", userToken.getToken());
        }
        sharedPreferenceUtils.setInt("id", profile.getId());
        sharedPreferenceUtils.setString("nickname", profile.getNickname());
        sharedPreferenceUtils.setString("assignment", profile.getAssignment());
        sharedPreferenceUtils.setString("gender", profile.getGender());
        sharedPreferenceUtils.setString("avatar", profile.getAvatar());
        splashView.toMain();
    }

    private void displayToken(UserToken userToken, String username, String password) {
        Profile profile = userToken.getProfile();
        sharedPreferenceUtils = new SharedPreferenceUtils(context, "user");
        String last_token = sharedPreferenceUtils.getString("token");
        if(!last_token.equals(userToken.getToken())){
            sharedPreferenceUtils.setString("token", userToken.getToken());
        }
        sharedPreferenceUtils.setString("username", username);
        sharedPreferenceUtils.setString("password", password);
        sharedPreferenceUtils.setInt("id", profile.getId());
        sharedPreferenceUtils.setString("nickname", profile.getNickname());
        sharedPreferenceUtils.setString("assignment", profile.getAssignment());
        sharedPreferenceUtils.setString("gender", profile.getGender());
        sharedPreferenceUtils.setString("avatar", profile.getAvatar());
        splashView.toMain();
    }
}
