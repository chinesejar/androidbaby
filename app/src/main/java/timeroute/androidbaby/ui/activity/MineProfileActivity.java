package timeroute.androidbaby.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseActivity;
import timeroute.androidbaby.ui.presenter.MineProfilePresenter;
import timeroute.androidbaby.ui.view.IMineProfileView;
import timeroute.androidbaby.util.SharedPreferenceUtils;

public class MineProfileActivity extends IBaseActivity<IMineProfileView, MineProfilePresenter> implements IMineProfileView {

    private SharedPreferenceUtils sharedPreferenceUtils;

    @Bind(R.id.layout_nickname)
    LinearLayout layoutNickname;
    @Bind(R.id.nickname)
    TextView textViewNickname;
    @Bind(R.id.layout_assignment)
    LinearLayout layoutAssignment;
    @Bind(R.id.assignment)
    TextView textViewAssignment;
    @Bind(R.id.layout_gender)
    LinearLayout layoutGender;
    @Bind(R.id.gender)
    TextView textViewGender;

    @Override
    protected MineProfilePresenter createPresenter() {
        return new MineProfilePresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_mine_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        sharedPreferenceUtils = new SharedPreferenceUtils(this, "user");

        getSupportActionBar().setTitle(getString(R.string.title_activity_profile));

        String nickname = sharedPreferenceUtils.getString("nickname");
        if (nickname == null){
            textViewNickname.setText("请输入昵称");
        }else {
            textViewNickname.setText(nickname);
        }
        String assignment = sharedPreferenceUtils.getString("assignment");
        if(assignment == null){
            textViewAssignment.setText("请输入签名");
        }else {
            textViewAssignment.setText(assignment);
        }
        String gender = sharedPreferenceUtils.getString("gender");
        Log.d("gender", gender);
        if(gender.equals("F")){
            textViewGender.setText("女");
        }else if(gender.equals("M")){
            textViewGender.setText("男");
        }else if(gender.equals("B")){
            textViewGender.setText("双性人");
        }else {
            textViewGender.setText("请选择性别");
        }

        layoutNickname.setOnClickListener(view -> {
            EditText editText = new EditText(this);
            editText.setSingleLine(true);
            editText.setText(textViewNickname.getText().toString());
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.nickname))
                    .setView(editText)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        String input = editText.getText().toString();
                        if(input.length() == 0){
                            Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            mPresenter.putProfile("nickname", input);
                        }
                    })
                    .setNegativeButton("取消", null).show();
        });
        layoutAssignment.setOnClickListener(view -> {
            EditText editText = new EditText(this);
            editText.setSingleLine(true);
            editText.setText(textViewAssignment.getText().toString());
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.assignment))
                    .setView(editText)
                    .setPositiveButton("确定", ((dialogInterface, i) -> {
                        String input = editText.getText().toString();
                        if(input.length() == 0){
                            Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            mPresenter.putProfile("assignment", input);
                        }
                    }))
                    .setNegativeButton("取消", null).show();
        });
        layoutGender.setOnClickListener(view -> {
            String[] gender_items = new String[]{"男", "女", "双性人"};
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.gender))
                    .setSingleChoiceItems(
                            gender_items,
                            Arrays.asList(gender_items).indexOf(textViewGender.getText().toString()),
                            ((dialogInterface, i) -> {
                                mPresenter.putProfile("gender", gender_items[i]);
                                dialogInterface.dismiss();
                            })
                    )
                    .setNegativeButton("取消", null).show();
        });
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public void setTextView(String type, String value) {
        if(type == "nickname"){
            textViewNickname.setText(value);
        }else if (type == "assignment"){
            textViewAssignment.setText(value);
        }else if (type == "gender"){
            textViewGender.setText(value);
        }
    }
}
