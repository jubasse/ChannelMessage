package metral.julien.channelmessaging;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import metral.julien.channelmessaging.Database.FriendsDB;
import metral.julien.channelmessaging.Model.User;

public class FriendsActivity extends AppCompatActivity {

    private Runnable r;
    private Handler handler;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (User) getIntent().getSerializableExtra("User");
        }

        ArrayList<User> friendList = FriendsDB.all(FriendsActivity.this);
        Log.wtf("tag",friendList.toString());

    }

}
