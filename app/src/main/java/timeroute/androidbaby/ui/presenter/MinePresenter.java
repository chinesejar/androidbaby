package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.bean.user.ImageToken;
import timeroute.androidbaby.ui.adapter.FeedListAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.ui.view.IMineView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class MinePresenter extends BasePresenter<IMineView> {

    private Context context;
    private IMineView mineView;

    private SharedPreferenceUtils sharedPreferenceUtils;

    public MinePresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }

    public void getToken(){
        mineView = getView();
        if(mineView != null){
            userApi.getImageToken("Token "+sharedPreferenceUtils.getString("token"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(token -> {
                        displayToken(token);
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void displayToken(ImageToken token) {
        Log.d("token", token.getToken());
    }

}
