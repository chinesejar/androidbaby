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
import timeroute.androidbaby.ui.view.ILoginView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

    private Context context;
    private ILoginView loginView;

    public LoginPresenter(Context context){
        this.context = context;
    }

    public void getToken(String username, String password){
        loginView = getView();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        if(loginView != null){
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
                    loginView.displayProgressBar(false);
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

    private void displayToken(UserToken userToken, String username, String password) {
        Profile profile = userToken.getProfile();
        loginView.displayProgressBar(false);
        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context, "user");
        sharedPreferenceUtils.setString("token", userToken.getToken());
        sharedPreferenceUtils.setString("username", username);
        sharedPreferenceUtils.setString("password", password);
        sharedPreferenceUtils.setInt("id", profile.getId());
        sharedPreferenceUtils.setString("nickname", profile.getNickname());
        sharedPreferenceUtils.setString("assignment", profile.getAssignment());
        sharedPreferenceUtils.setString("gender", profile.getGender());
        sharedPreferenceUtils.setString("avatar", profile.getAvatar());
        //sharedPreferenceUtils.setString("email", profile.getEmail());
        loginView.toMain();
    }
}
