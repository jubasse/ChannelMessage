package metral.julien.channelmessaging;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import metral.julien.channelmessaging.Adapter.PrivateMessageAdapter;
import metral.julien.channelmessaging.Model.MessageList;
import metral.julien.channelmessaging.Model.PrivateMessageList;
import metral.julien.channelmessaging.Model.User;

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
                postDatas.put("userid", friend.getIdentifiant().toString());
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

        handler = new Handler();

        r = new Runnable() {
            public void run() {
                loadDatas();
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(r,0);

    }

    private void loadDatas() {
        HashMap<String, String> postDatas = new HashMap<>(1);
        postDatas.put("accesstoken", user.getToken());
        postDatas.put("userid", friend.getIdentifiant().toString());
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_MESSAGES_FROM_FRIENDS,postDatas);

        task.setOnNewWsRequestListener(this);

        task.execute();
    }

    @Override
    public void onCompleted(String response) {
        Gson gson = new Gson();
        Log.wtf("JsonMessages", response.toString());

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
    public void onError(String error) {

    }
}
