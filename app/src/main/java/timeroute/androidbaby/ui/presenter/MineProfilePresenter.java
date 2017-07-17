package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.bean.user.Profile;
import timeroute.androidbaby.bean.user.Token;
import timeroute.androidbaby.bean.user.User;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.ILoginView;
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
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(avoid -> {
                        Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                        if(type.equals("nickname") || type.equals("assignment")){
                            sharedPreferenceUtils.setString(type, value);
                        }else if(type.equals("gender")) {
                            if(value == "男") {
                                sharedPreferenceUtils.setString(type, "M");
                            }else if (value == "女"){
                                sharedPreferenceUtils.setString(type, "F");
                            }else if (value == "双性人"){
                                sharedPreferenceUtils.setString(type, "B");
                            }
                        }
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
