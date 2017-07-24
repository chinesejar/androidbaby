package timeroute.androidbaby.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import timeroute.androidbaby.R;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent intent = getIntent();
        int user_id = intent.getIntExtra("user_id", -1);
        if(user_id >= 0){
            Log.d("USER", String.valueOf(user_id));
        }
    }
}
