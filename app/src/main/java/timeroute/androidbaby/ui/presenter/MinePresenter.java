package timeroute.androidbaby.ui.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import timeroute.androidbaby.ui.adapter.FeedListAdapter;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.view.IFeedView;
import timeroute.androidbaby.ui.view.IMineView;

/**
 * Created by chinesejar on 17-7-14.
 */

public class MinePresenter extends BasePresenter<IMineView> {

    private Context context;
    private IFeedView feedView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private FeedListAdapter adapter;
    private int lastVisibleItem;

    public MinePresenter(Context context){
        this.context = context;
    }

}
