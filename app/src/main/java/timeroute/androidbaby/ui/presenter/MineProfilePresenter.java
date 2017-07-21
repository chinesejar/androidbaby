package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.widget.Toast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IMineProfileView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class MineProfilePresenter extends BasePresenter<IMineProfileView> {

    private SharedPreferenceUtils sharedPreferenceUtils;

    private Context context;
    private IMineProfileView mineProfileView;

    public MineProfilePresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }

    public void putProfile(String type, String value){
        int id = sharedPreferenceUtils.getInt("id");
        String token = sharedPreferenceUtils.getString("token");
        mineProfileView = getView();
        Profile profile = new Profile();
        if(type == "nickname"){
            profile.setNickname(value);
        }else if (type == "assignment"){
            profile.setAssignment(value);
        }else if (type == "gender"){
            if(value == "男") {
                profile.setGender("M");
            }else if (value == "女"){
                profile.setGender("F");
            }else if (value == "双性人"){
                profile.setGender("B");
            }
        }
        if(mineProfileView != null){
            userApi.putProfile("Token "+token, id, profile)
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends Void>>() {
                        @Override
                        public Observable<? extends Void> call(Throwable throwable) {
                            return Observable.error(ExceptionEngine.handleException(throwable));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<Void>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(Void aVoid) {
                            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                            if(type.equals("nickname") || type.equals("assignment")){
                                sharedPreferenceUtils.setString(type, value);
                            }else if(type.equals("gender")) {
                                sharedPreferenceUtils.setString(type, profile.getGender());
                            }
                            mineProfileView.setTextView(type, value);
                        }
                    });
        }
    }

}
