package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timeroute.androidbaby.R;
import timeroute.androidbaby.api.exception.ApiException;
import timeroute.androidbaby.api.exception.ExceptionEngine;
import timeroute.androidbaby.bean.feed.Feed;
import timeroute.androidbaby.bean.feed.FeedPic;
import timeroute.androidbaby.bean.user.ImageToken;
import timeroute.androidbaby.support.MyObserver;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IPostView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

/**
 * Created by chinesejar on 17-7-14.
 */

public class PostPresenter extends BasePresenter<IPostView> {

    private static final String TAG = "PostPresenter";

    private Context context;
    private IPostView postView;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public PostPresenter(Context context){
        this.context = context;
        sharedPreferenceUtils = new SharedPreferenceUtils(this.context, "user");
    }

    public void getImageToken(List<Map<String, Object>> list, String content){
        String token = sharedPreferenceUtils.getString("token");
        postView = getView();
        if(postView != null){
            Log.d(TAG, "list size: "+list.size());
            feedApi.getImageToken("JWT "+token, list.size())
                    .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<List<ImageToken>>() {
                        @Override
                        protected void onError(ApiException ex) {
                            Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(List<ImageToken> tokens) {
                            postImage(list, content, tokens);
                        }
                    });
        }
    }

    public void postImage(List<Map<String, Object>> list, String content, List<ImageToken> tokens){
        UploadManager uploadManager = new UploadManager();
        int id = sharedPreferenceUtils.getInt("id");
        long cur_timestamp = System.currentTimeMillis();

        Feed feed = new Feed();
        feed.setContent(content);
        List<FeedPic> feedPics = new ArrayList<>();

        for(int i=0;i<tokens.size();i++){
            String pic_path = (String)list.get(i).get("pic");
            String[] seps = pic_path.split("/");
            String pic_name = seps[seps.length-1];
            String[] formats = pic_name.split("\\.");
            String format = formats[formats.length-1];
            String name = id+"_"+cur_timestamp+"_"+i+"."+format;
            FeedPic feedPic = new FeedPic();
            feedPic.setUrl(baseFeedUrl + name);
            feedPics.add(feedPic);
            uploadManager.put(new File((String)list.get(i).get("pic")), name, tokens.get(i).getToken(), (key, info, response) -> {
                if(info.isOK()) {
                    Log.i("qiniu", "Upload Success");
                } else {
                    Log.i("qiniu", "Upload Fail");
                    Toast.makeText(context, context.getString(R.string.update_fail), Toast.LENGTH_SHORT).show();
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
            }, null);
        }
        feed.setFeedPic(feedPics);
        String token = sharedPreferenceUtils.getString("token");
        feedApi.postFeed("JWT "+token, feed)
                .onErrorResumeNext(throwable -> Observable.error(ExceptionEngine.handleException(throwable)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Feed>() {
                    @Override
                    protected void onError(ApiException ex) {
                        Toast.makeText(context, ex.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(Feed feed) {
                        Toast.makeText(context, context.getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                        postView.sendSuccess();
                    }
                });
    }

}
