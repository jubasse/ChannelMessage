package metral.julien.channelmessaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import javax.xml.datatype.Duration;

import metral.julien.channelmessaging.Adapter.MessageAdapter;
import metral.julien.channelmessaging.Database.FriendsDB;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.Message;
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
    private Button myFriendsButton;
    private boolean firstLaunch;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            channel = (Channel) getIntent().getSerializableExtra("Channel");
            user = (User) getIntent().getSerializableExtra("User");
        }

        firstLaunch = true;
        handler = new Handler();

        r = new Runnable() {
            public void run() {
                loadDatas();
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(r,0);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadDatas() {
        List<NameValuePair> nameValuePairs = new ArrayList<>(1);
        nameValuePairs.add(new BasicNameValuePair("accesstoken", user.getToken()));
        nameValuePairs.add(new BasicNameValuePair("channelid", channel.getChannelID().toString()));
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_MESSAGES,nameValuePairs);

        task.setOnNewWsRequestListener(this);

        task.execute();
    }

    private void sendMessage(final EditText editMessage, User user,Channel channel) {

        final String messageText = editMessage.getText().toString();
        editMessage.setText("");
        List<NameValuePair> nameValuePairs = new ArrayList<>(3);

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

            messageAdapter = new MessageAdapter(messages.getMessages(),MessageActivity.this);
            messageListView.setAdapter(messageAdapter);

            messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Message message = messageAdapter.getItem(position);
                    final User friend = new User();
                    friend.setIdentifiant(message.getUserID().toString());
                    friend.setUsername(message.getUsername());
                    friend.setImageUrl(message.getImageUrl());
                    new AlertDialog.Builder(MessageActivity.this)
                            .setTitle("Ajouter un ami")
                            .setMessage("Voulez vous vraiment ajouter cet utilisateur à vos amis ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(FriendsDB.addFriend(friend,MessageActivity.this)){
                                        Toast.makeText(MessageActivity.this,"Amis ajouté", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MessageActivity.this,"L'ajout à échoué", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_dialer)
                            .show();
                }
            });

            messageListView.setSelection(messageAdapter.getCount() - 1);

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
