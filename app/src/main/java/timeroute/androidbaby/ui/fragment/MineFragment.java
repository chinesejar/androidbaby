package timeroute.androidbaby.ui.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.activity.MineProfileActivity;
import timeroute.androidbaby.ui.activity.NotificationActivity;
import timeroute.androidbaby.ui.activity.SettingActivity;
import timeroute.androidbaby.ui.activity.UserActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.MinePresenter;
import timeroute.androidbaby.ui.view.IMineView;
import timeroute.androidbaby.util.DensityUtil;
import timeroute.androidbaby.util.RoundTransform;
import timeroute.androidbaby.util.SharedPreferenceUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends IBaseFragment<IMineView, MinePresenter> implements IMineView {

    private static final String TAG = "MineFragment";

    private SharedPreferenceUtils sharedPreferenceUtils;
    private RxPermissions rxPermissions;

    private LinearLayoutManager mLayoutManager;
    private int user_id;
    private String avatar;
    private String nickname;
    private String assignment;
    private boolean loaded = false;

    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_ALBUM = 2;
    public static final int REQUEST_CROP = 3;

    private File mImageFile;
    private Uri contentUri;

    @Bind(R.id.background)
    ImageView background_imageView;
    @Bind(R.id.avatar)
    ImageView avatar_imageView;
    @Bind(R.id.nickname)
    TextView nickname_TextView;
    @Bind(R.id.assignment)
    TextView assignment_TextView;
    @Bind(R.id.mine_feed)
    RelativeLayout feed_layout;
    @Bind(R.id.mine_profile)
    RelativeLayout profile_layout;
    @Bind(R.id.mine_setting)
    RelativeLayout setting_layout;
    @Bind(R.id.mine_notify)
    RelativeLayout notification_layout;
    @Bind(R.id.has_notification)
    TextView has_notification;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initView(View rootView) {
        setTracker(TAG);
        sharedPreferenceUtils = new SharedPreferenceUtils(getContext(), "user");
        user_id = sharedPreferenceUtils.getInt("id");
        nickname = sharedPreferenceUtils.getString("nickname");
        assignment = sharedPreferenceUtils.getString("assignment");
        avatar = sharedPreferenceUtils.getString("avatar");
        mLayoutManager = new LinearLayoutManager(getContext());

        rxPermissions = new RxPermissions(getActivity());

        avatar_imageView.setOnClickListener(view -> {
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if(granted){
                            onClickPicker();
                        }else {
                            Toast.makeText(getActivity(), getString(R.string.permission), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        mPresenter.getNotification();

        feed_layout.setOnClickListener(view -> {
            goToUser(user_id, nickname, assignment, avatar);
        });
        profile_layout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MineProfileActivity.class);
            startActivity(intent);
        });
        setting_layout.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });
        notification_layout.setOnClickListener(view -> {
            setNotification(false);
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        });

        if(!loaded){
            refreshProfile();
        }
    }

    public void onClickPicker() {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.avatar_title))
                .setItems(new String[]{getString(R.string.avatar_capture), getString(R.string.avatar_gallery)}, (dialogInterface, i) -> {
                    if (i == 0) {
                        selectCamera();
                    } else {
                        selectAlbum();
                    }
                })
                .create()
                .show();
    }

    private void selectCamera() {
        createImageFile();
        if (!mImageFile.exists()) {
            return;
        }

        contentUri = FileProvider.getUriForFile(getContext(), "timeroute.androidbaby.fileprovider", mImageFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void createImageFile() {
        File mImagePath = new File(Environment.getExternalStorageDirectory(), "AndroidBaby");
        if(!mImagePath.exists()) {
            mImagePath.mkdir();
        }
        mImageFile = new File(Environment.getExternalStorageDirectory(), "AndroidBaby/avatar.jpg");
        try {
            mImageFile.createNewFile();
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    private void selectAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(albumIntent, REQUEST_ALBUM);
    }

    private void cropImage(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("circleCrop", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
        startActivityForResult(intent, REQUEST_CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CAMERA:
                cropImage(FileProvider.getUriForFile(getContext(), "timeroute.androidbaby.fileprovider", mImageFile));
                break;

            case REQUEST_ALBUM:
                createImageFile();
                if (!mImageFile.exists()) {
                    return;
                }

                Uri uri = data.getData();
                if (uri != null) {
                    cropImage(uri);
                }
                break;

            case REQUEST_CROP:
                mPresenter.getToken();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProfile();
    }

    private void refreshProfile() {
        String background = "http://ou3l05aqj.bkt.clouddn.com/background.jpg?imageMogr2/thumbnail/600x"+String.valueOf(DensityUtil.dip2px(getContext(), 200))+"/format/webp/blur/1x0/quality/75|imageslim";
        setBackground(background);
        avatar = sharedPreferenceUtils.getString("avatar");
        if(avatar != null){
            setAvatar(avatar);
        }
        nickname = sharedPreferenceUtils.getString("nickname");
        nickname_TextView.setText(nickname);
        assignment = sharedPreferenceUtils.getString("assignment");

        if(assignment != null){
            if(assignment.length() == 0){
                assignment_TextView.setText(getString(R.string.assignment_prompt));
            }else {
                assignment_TextView.setText(assignment);
            }
        }
    }

    @Override
    public void setNotification(boolean has) {
        if(has){
            has_notification.setVisibility(View.VISIBLE);
        }else {
            has_notification.setVisibility(View.GONE);
        }
    }

    @Override
    public void setAvatar(String url) {
        Picasso.with(getContext())
                .load(url)
                .transform(new RoundTransform())
                .into(avatar_imageView);
    }

    public void setBackground(String url) {
        Picasso.with(getContext())
                .load(url)
                .into(background_imageView);
    }

    @Override
    public File getAvatar() {
        return mImageFile;
    }

    @Override
    public void goToUser(int user_id, String nickname, String assignment, String avatar) {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("nickname", nickname);
        intent.putExtra("assignment", assignment);
        intent.putExtra("avatar", avatar);
        startActivity(intent);
    }

}
