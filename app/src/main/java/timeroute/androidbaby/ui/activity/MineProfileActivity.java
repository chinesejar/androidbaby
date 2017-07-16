package timeroute.androidbaby.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import timeroute.androidbaby.R;
import timeroute.androidbaby.ui.base.BasePresenter;
import timeroute.androidbaby.ui.base.IBaseActivity;

public class MineProfileActivity extends IBaseActivity {

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
    protected BasePresenter createPresenter() {
        return null;
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
        layoutNickname.setOnClickListener(view -> {
            EditText editText = new EditText(this);
            editText.setSingleLine(true);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.nickname))
                    .setView(editText)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        String input = editText.getText().toString();
                        if(input.length() == 0){
                            Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            textViewNickname.setText(input);
                        }
                    })
                    .setNegativeButton("取消", null).show();
        });
        layoutAssignment.setOnClickListener(view -> {
            EditText editText = new EditText(this);
            editText.setSingleLine(true);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.assignment))
                    .setView(editText)
                    .setPositiveButton("确定", ((dialogInterface, i) -> {
                        String input = editText.getText().toString();
                        if(input.length() == 0){
                            Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            textViewAssignment.setText(input);
                        }
                    }))
                    .setNegativeButton("取消", null).show();
        });
        layoutGender.setOnClickListener(view -> {
            String[] gender = new String[]{"男", "女", "双性人"};
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.gender))
                    .setSingleChoiceItems(
                            gender,
                            0,
                            ((dialogInterface, i) -> {
                                textViewGender.setText(gender[i]);
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
}