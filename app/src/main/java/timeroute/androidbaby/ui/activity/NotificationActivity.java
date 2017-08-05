package timeroute.androidbaby.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.NotificationPresenter;
import timeroute.androidbaby.ui.view.INotificationView;

public class NotificationActivity extends IBaseActivity<INotificationView, NotificationPresenter> implements INotificationView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    @Override
    protected NotificationPresenter createPresenter() {
        return new NotificationPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_notification;
    }
}
