package metral.julien.channelmessaging.Activity.Message;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import metral.julien.channelmessaging.Adapter.PrivateMessageAdapter;
import metral.julien.channelmessaging.Utils.ApiManager;
import metral.julien.channelmessaging.Model.PrivateMessageList;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.Utils.MyAsyncTask;
import metral.julien.channelmessaging.Utils.onWsRequestListener;

public class PrivateMessagesActivity extends AppCompatActivity implements onWsRequestListener {

    private User user;
    private User friend;
    private Handler handler;
    private Runnable r;
    private ListView messageListView;
    private PrivateMessageAdapter privateMessageAdapter;
    private PrivateMessageList messages;
    private EditText privateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_messages);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        privateMessage = (EditText) findViewById(R.id.privateMessageField);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> postDatas = new HashMap<>(2);
                postDatas.put("accesstoken", user.getToken());
                postDatas.put("userid", friend.getIdentifiant());
                postDatas.put("message", privateMessage.getText().toString());
                MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_SEND_MESSAGES_TO_FRIENDS,postDatas);
                task.setOnNewWsRequestListener(new onWsRequestListener() {
                    @Override
                    public void onCompleted(String response) {
                        privateMessage.setText("");
                        loadDatas();
                    }

                    @Override
                    public void onError(String error) {
                        //do nothing
                    }
                });
                task.execute();
            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (User) getIntent().getSerializableExtra("User");
            friend = (User) getIntent().getSerializableExtra("Friend");
        }

        if(handler != null && r != null){
            handler.removeCallbacks(r);
        }

        handler = new Handler();

        r = new Runnable() {
            public void run() {
                loadDatas();
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(r,0);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(handler != null && r != null){
                handler.removeCallbacks(r);
            }
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDatas() {
        HashMap<String, String> postDatas = new HashMap<>(1);
        postDatas.put("accesstoken", user.getToken());
        postDatas.put("userid", friend.getIdentifiant());
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_MESSAGES_FROM_FRIENDS,postDatas);

        task.setOnNewWsRequestListener(this);

        task.execute();
    }

    @Override
    public void onCompleted(String response) {
        Gson gson = new Gson();
        Log.wtf("JsonMessages", response);

        try{
            messages = gson.fromJson(response,PrivateMessageList.class);
        }catch (Exception e){
            messages = null;
        }
        Log.wtf("JsonMessages", messages.toString());
        messageListView = (ListView) findViewById(R.id.privateMessagesListView);
        privateMessageAdapter = new PrivateMessageAdapter(messages.getMessages(),user,PrivateMessagesActivity.this);
        messageListView.setAdapter(privateMessageAdapter);

        messageListView.setSelection(messageListView.getAdapter().getCount() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(r != null && handler != null){
            handler.removeCallbacks(r);
        }
    }

    @Override
    public void onError(String error) {
        if(r != null && handler != null){
            handler.removeCallbacks(r);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(r != null && handler != null){
            handler.removeCallbacks(r);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(r != null && handler != null){
            handler.removeCallbacks(r);
        }
    }
}
