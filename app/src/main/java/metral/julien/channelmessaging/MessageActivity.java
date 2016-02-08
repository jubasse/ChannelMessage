package metral.julien.channelmessaging;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import metral.julien.channelmessaging.Adapter.MessageAdapter;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.MessageList;
import metral.julien.channelmessaging.Model.Response;
import metral.julien.channelmessaging.Model.User;

public class MessageActivity extends Activity implements onWsRequestListener {

    private Response res;
    private MessageList messages;
    private ListView messageListView;
    private TextView channelName;
    private User user;
    private Channel channel;
    private Button sendButton;
    private EditText editMessage;
    private Handler handler;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            channel = (Channel) getIntent().getSerializableExtra("Channel");
            user = (User) getIntent().getSerializableExtra("User");
        }

        handler = new Handler();

        r = new Runnable() {
            public void run() {
                loadDatas();
                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(r,0);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadDatas() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("accesstoken", user.getToken()));
        nameValuePairs.add(new BasicNameValuePair("channelid", channel.getChannelID().toString()));
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_MESSAGES,nameValuePairs);

        task.setOnNewWsRequestListener(this);

        task.execute();
    }

    private void sendMessage(final EditText editMessage, User user,Channel channel) {

        final String messageText = editMessage.getText().toString();
        editMessage.setText("");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

        nameValuePairs.add(new BasicNameValuePair("message", messageText));
        nameValuePairs.add(new BasicNameValuePair("accesstoken", user.getToken()));
        nameValuePairs.add(new BasicNameValuePair("channelid", channel.getChannelID().toString()));

        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_SEND_MESSAGES,nameValuePairs);
        task.execute();

        task.setOnNewWsRequestListener(new onWsRequestListener() {
            @Override
            public void onCompleted(String json) {
                Gson gson = new Gson();
                Response res = gson.fromJson(json, Response.class);
                if (res.getCode() == 200) {
                    loadDatas();
                }
                if (res.getCode() == 500) {
                    Toast.makeText(MessageActivity.this, "Cannot send message", Toast.LENGTH_LONG);
                    editMessage.setText(messageText);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onCompleted(String json) {
            Gson gson = new Gson();
            Log.wtf("JsonMessages", json.toString());
            try{
                messages = gson.fromJson(json,MessageList.class);
            }catch (Exception e){
                messages = null;
            }
            messageListView = (ListView) findViewById(R.id.messageList);
            channelName = (TextView) findViewById(R.id.channelName);
            sendButton = (Button) findViewById(R.id.sendButton);
            editMessage = (EditText) findViewById(R.id.messageText);

            channelName.setText(channel.getName());

            final MessageAdapter adapter = new MessageAdapter(messages.getMessages(),MessageActivity.this);
            messageListView.setAdapter(adapter);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(editMessage, user, channel);
                }
            });
//                if(messages.getMessages().get(1) != null){
//                    Log.wtf("ObjectChannels", messages.getMessages().get(1).getMessage());
//                }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(r);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }
}
