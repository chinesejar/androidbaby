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

    private static final String TAG = "MineProfileActivity";

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

        setTracker(TAG);

        sharedPreferenceUtils = new SharedPreferenceUtils(this, "user");

        getSupportActionBar().setTitle(getString(R.string.title_activity_profile));

        String nickname = sharedPreferenceUtils.getString("nickname");
        if (nickname == null){
            textViewNickname.setText(getString(R.string.nickname_hint));
        }else {
            textViewNickname.setText(nickname);
        }
        String assignment = sharedPreferenceUtils.getString("assignment");
        if(assignment == null){
            textViewAssignment.setText(getString(R.string.assignment_hint));
        }else {
            textViewAssignment.setText(assignment);
        }
        String gender = sharedPreferenceUtils.getString("gender");
        if(gender != null){
            switch (gender) {
                case "F":
                    textViewGender.setText(getString(R.string.gender_female));
                    break;
                case "M":
                    textViewGender.setText(getString(R.string.gender_male));
                    break;
                case "B":
                    textViewGender.setText(getString(R.string.gender_double));
                    break;
                default:
                    textViewGender.setText(getString(R.string.gender_select));
                    break;
            }
        }

        layoutNickname.setOnClickListener(view -> {
            EditText editText = new EditText(this);
            editText.setSingleLine(true);
            editText.setText(textViewNickname.getText().toString());
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.nickname))
                    .setView(editText)
                    .setPositiveButton(getString(R.string.sure), (dialogInterface, i) -> {
                        String input = editText.getText().toString();
                        if(input.length() == 0){
                            Toast.makeText(this, getString(R.string.empty_prompt), Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            mPresenter.putProfile("nickname", input);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null).show();
        });
        layoutAssignment.setOnClickListener(view -> {
            EditText editText = new EditText(this);
            editText.setSingleLine(true);
            editText.setText(textViewAssignment.getText().toString());
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.assignment))
                    .setView(editText)
                    .setPositiveButton(getString(R.string.sure), ((dialogInterface, i) -> {
                        String input = editText.getText().toString();
                        if(input.length() == 0){
                            Toast.makeText(this, getString(R.string.empty_prompt), Toast.LENGTH_SHORT).show();
                        }else {
                            mPresenter.putProfile("assignment", input);
                        }
                    }))
                    .setNegativeButton(getString(R.string.cancel), null).show();
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
                    .setNegativeButton(getString(R.string.cancel), null).show();
        });
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    public void setTextView(String type, String value) {
        switch (type) {
            case "nickname":
                textViewNickname.setText(value);
                break;
            case "assignment":
                textViewAssignment.setText(value);
                break;
            case "gender":
                textViewGender.setText(value);
                break;
        }
    }
}
