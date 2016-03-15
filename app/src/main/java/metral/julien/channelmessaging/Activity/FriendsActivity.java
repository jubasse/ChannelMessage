package metral.julien.channelmessaging.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import metral.julien.channelmessaging.Activity.Message.PrivateMessagesActivity;
import metral.julien.channelmessaging.Adapter.FriendAdapter;
import metral.julien.channelmessaging.Database.FriendsDB;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;

public class FriendsActivity extends AppCompatActivity {

    private User user;
    private GridView friendGrid;
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (User) getIntent().getSerializableExtra("User");
        }

        final ArrayList<User> friendList = FriendsDB.all(FriendsActivity.this);
        Log.wtf("Friends",friendList.toString());
        friendGrid = (GridView) findViewById(R.id.friendGridView);
        friendAdapter = new FriendAdapter(friendList,FriendsActivity.this);
        friendGrid.setAdapter(friendAdapter);

        friendGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User friend = friendAdapter.getItem(position);
                Intent intent = new Intent(FriendsActivity.this,PrivateMessagesActivity.class);
                intent.putExtra("Friend",friend);
                intent.putExtra("User",user);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
