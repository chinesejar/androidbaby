package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.bean.user.Register;
import timeroute.androidbaby.bean.user.User;
import timeroute.androidbaby.bean.user.UserToken;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.ILoginView;
import timeroute.androidbaby.ui.view.IRegisterView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class RegisterPresenter extends BasePresenter<IRegisterView> {

    private Context context;
    private IRegisterView registerView;

    public RegisterPresenter(Context context) {
        this.context = context;
    }

    public void postCode(String email) {
        registerView = getView();
        User user = new User();
        user.setEmail(email);
        if (registerView != null) {
            userApi.postCode(user)
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<Object>>>() {
                        @Override
                        public Observable<? extends Response<Object>> call(Throwable throwable) {
                            return Observable.error(ExceptionEngine.handleException(throwable));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<Response<Object>>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, "已发送验证码，30 分钟内不能重复发送", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(Response<Object> objectResponse) {
                            Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void postUser(String email, String code, String password) {
        registerView = getView();
        Register user = new Register();
        user.setEmail(email);
        user.setPassword(password);
        user.setCode(code);
        if (registerView != null) {
            userApi.postUser(user)
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<Object>>>() {
                        @Override
                        public Observable<? extends Response<Object>> call(Throwable throwable) {
                            return Observable.error(ExceptionEngine.handleException(throwable));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<Response<Object>>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(Response<Object> objectResponse) {
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                            registerView.backToLogin();
                        }
                    });
        }
    }
}
