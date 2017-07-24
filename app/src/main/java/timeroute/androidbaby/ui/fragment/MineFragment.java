package timeroute.androidbaby.ui.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.activity.MineProfileActivity;
import timeroute.androidbaby.ui.base.IBaseFragment;
import timeroute.androidbaby.ui.presenter.MinePresenter;
import timeroute.androidbaby.ui.view.IMineView;
import timeroute.androidbaby.util.RoundTransform;
import timeroute.androidbaby.util.SharedPreferenceUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends IBaseFragment<IMineView, MinePresenter> implements IMineView {

    private SharedPreferenceUtils sharedPreferenceUtils;

    private LinearLayoutManager mLayoutManager;
    private String avatar;
    private String nickname;
    private String assignment;
    private boolean loaded = false;

    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_ALBUM = 2;
    public static final int REQUEST_CROP = 3;
    public static final int PERMISSION_CAMERA = 4;
    private static final int PERMISSION_STORAGE_WRITE = 5;
    private static final int PERMISSION_STORAGE_READ = 6;

    private File mImageFile;
    private Uri contentUri;

    @Bind(R.id.avatar)
    ImageView avatar_imageView;
    @Bind(R.id.nickname)
    TextView nickname_TextView;
    @Bind(R.id.assignment)
    TextView assignment_TextView;
    @Bind(R.id.mine_feed)
    LinearLayout feed_layout;
    @Bind(R.id.mine_profile)
    LinearLayout profile_layout;
    @Bind(R.id.mine_setting)
    LinearLayout setting_layout;
    @Bind(R.id.mine_about)
    LinearLayout about_layout;

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
        sharedPreferenceUtils = new SharedPreferenceUtils(getContext(), "user");
        mLayoutManager = new LinearLayoutManager(getContext());

        avatar_imageView.setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                onClickPicker();
            }else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[] {
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        },
                        PERMISSION_CAMERA
                );
            }
        });
        profile_layout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MineProfileActivity.class);
            startActivity(intent);
        });

        if(!loaded){
            refreshProfile();
        }
    }

    public void onClickPicker() {
        new AlertDialog.Builder(getContext())
                .setTitle("选择照片")
                .setItems(new String[]{"拍照", "相册"}, (dialogInterface, i) -> {
                    if (i == 0) {
                        selectCamera();
                    } else {
                        selectAlbum();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA || requestCode == PERMISSION_STORAGE_WRITE || requestCode == PERMISSION_STORAGE_READ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                showWarnDialog();
            }
        }
    }

    public void showWarnDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("警告")
                .setMessage("已禁用该权限，如果想重新使用，进入系统设置->应用->找到该应用打开权限设置")
                .setPositiveButton("确定", null)
                .create()
                .show();
    }

    private void selectCamera() {
        createImageFile();
        if (!mImageFile.exists()) {
            return;
        }

        contentUri = FileProvider.getUriForFile(getActivity(), "timeroute.androidbaby.fileprovider", mImageFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void createImageFile() {
        Log.d("dir", Environment.getExternalStorageDirectory().toString());
        mImageFile = new File(Environment.getExternalStorageDirectory(), "images/avatar.jpg");
        try {
            mImageFile.createNewFile();
        }catch (IOException e){
            Log.d("create", e.getMessage());
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
        avatar = sharedPreferenceUtils.getString("avatar");
        if(avatar != null){
            setAvatar(avatar);
        }
        nickname = sharedPreferenceUtils.getString("nickname");
        nickname_TextView.setText(nickname);
        assignment = sharedPreferenceUtils.getString("assignment");

        Log.d("tag", assignment.toString());
        if(assignment.length() == 0){
            assignment_TextView.setText("完善个性签名");
        }else {
            assignment_TextView.setText(assignment);
        }
    }

    @Override
    public void setAvatar(String url) {
        Picasso.with(getContext())
                .load(url)
                .transform(new RoundTransform())
                .into(avatar_imageView);
    }

    @Override
    public File getAvatar() {
        return mImageFile;
    }
}
