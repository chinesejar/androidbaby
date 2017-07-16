package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.bean.user.Token;
import timeroute.androidbaby.bean.user.User;
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
            tokenApi.getToken(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(token -> {
                        displayToken(token, username, password);
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        loginView.displayProgressBar(false);
    }

    private void displayToken(Token token, String username, String password) {
        Log.d("serializer", token.getToken());
        loginView.displayProgressBar(false);
        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context, "user");
        sharedPreferenceUtils.setString("token", token.toString());
        sharedPreferenceUtils.setString("username", username);
        sharedPreferenceUtils.setString("password", password);
        sharedPreferenceUtils.setLong("last_login", System.currentTimeMillis());
        loginView.toMain();
    }
}
