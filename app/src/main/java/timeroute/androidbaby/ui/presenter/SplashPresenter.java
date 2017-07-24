package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    public void getToken(String username, String password){
        splashView = getView();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if(splashView != null){
            userApi.getToken(user)
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends UserToken>>() {
                        @Override
                        public Observable<? extends UserToken> call(Throwable throwable) {
                            return Observable.error(ExceptionEngine.handleException(throwable));
                        }
                    })
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
                            displayToken(userToken, username, password);
                        }
                    });
        }
    }

    public void refreshToken(String token){
        splashView = getView();
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        Log.d("token", userToken.toString());
        if(splashView != null){
            userApi.refreshToken(userToken)
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends UserToken>>() {
                        @Override
                        public Observable<? extends UserToken> call(Throwable throwable) {
                            return Observable.error(ExceptionEngine.handleException(throwable));
                        }
                    })
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
        Log.d("tag", userToken.toString());
        sharedPreferenceUtils = new SharedPreferenceUtils(context, "user");
        String last_token = sharedPreferenceUtils.getString("token");
        Log.d("last_token", last_token);
        Log.d("cur_token", userToken.getToken());
        if(last_token != userToken.getToken()){
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
        Log.d("tag", userToken.toString());
        sharedPreferenceUtils = new SharedPreferenceUtils(context, "user");
        String last_token = sharedPreferenceUtils.getString("token");
        Log.d("last_token", last_token);
        Log.d("cur_token", userToken.getToken());
        if(last_token != userToken.getToken()){
            sharedPreferenceUtils.setString("token", userToken.getToken());
        }
        sharedPreferenceUtils.setString("username", username);
        sharedPreferenceUtils.setString("password", password);
        sharedPreferenceUtils.setInt("id", profile.getId());
        sharedPreferenceUtils.setString("nickname", profile.getNickname());
        sharedPreferenceUtils.setString("assignment", profile.getAssignment());
        sharedPreferenceUtils.setString("gender", profile.getGender());
        sharedPreferenceUtils.setString("avatar", profile.getAvatar());
        //sharedPreferenceUtils.setString("email", profile.getEmail());
        splashView.toMain();
    }
}
