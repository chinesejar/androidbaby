package timeroute.androidbaby.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.MinePresenter;
import timeroute.androidbaby.ui.view.IMineView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends IBaseFragment<IMineView, MinePresenter> {

    private LinearLayoutManager mLayoutManager;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
    }
}
