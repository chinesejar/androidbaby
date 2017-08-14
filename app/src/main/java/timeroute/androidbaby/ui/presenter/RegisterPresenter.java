package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.R;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.user.Register;
import timeroute.androidbaby.bean.user.User;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IRegisterView;

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
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<Response<Object>>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, context.getString(R.string.send_again_hint), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(Response<Object> objectResponse) {
                            Toast.makeText(context, context.getString(R.string.send_success), Toast.LENGTH_SHORT).show();
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
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<Response<Object>>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, context.getString(R.string.invalid_code), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(Response<Object> objectResponse) {
                            Toast.makeText(context, context.getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                            registerView.backToLogin();
                        }
                    });
        }
    }
}
