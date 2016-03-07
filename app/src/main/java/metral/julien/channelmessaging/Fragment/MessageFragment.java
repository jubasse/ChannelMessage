package metral.julien.channelmessaging.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import metral.julien.channelmessaging.Adapter.MessageAdapter;
import metral.julien.channelmessaging.ApiManager;
import metral.julien.channelmessaging.ChannelListFragmentActivity;
import metral.julien.channelmessaging.Database.FriendsDB;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.Message;
import metral.julien.channelmessaging.Model.MessageList;
import metral.julien.channelmessaging.Model.Response;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.MyAsyncTask;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.onWsRequestListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment implements onWsRequestListener {

    private User user;
    private Channel channel;
    private Handler handler;
    private Runnable r;
    private View view;
    private Boolean firstLaunch;
    private MessageList messages;
    private ListView messageListView;
    private TextView channelName;
    private Button sendButton;
    private EditText editMessage;
    private MessageAdapter messageAdapter;

    public MessageFragment() {
        // Required empty public constructor
    }


    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setUserAndChannel(User user, Channel channel)
    {
        this.setUser(user);
        this.setChannel(channel);
        firstLaunch = true;
        handler = new Handler();

        r = new Runnable() {
            public void run() {
                loadDatas();
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(r, 0);
    }

    private void loadDatas() {
        HashMap<String, String> postDatas = new HashMap<>(1);
        postDatas.put("accesstoken", user.getToken());
        postDatas.put("channelid", channel.getChannelID().toString());
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_MESSAGES,postDatas);

        task.setOnNewWsRequestListener(this);

        task.execute();
    }

    @Override
    public void onCompleted(String json) {
        Gson gson = new Gson();
        Log.wtf("JsonMessages", json);

        try{
            messages = gson.fromJson(json,MessageList.class);
        }catch (Exception e){
            messages = null;
        }
        messageListView = (ListView) view.findViewById(R.id.messageList);
        channelName = (TextView) view.findViewById(R.id.channelName);
        sendButton = (Button) view.findViewById(R.id.sendButton);
        editMessage = (EditText) view.findViewById(R.id.messageText);

        channelName.setText(channel.getName());

        messageAdapter = new MessageAdapter(messages.getMessages(),getActivity());
        messageListView.setAdapter(messageAdapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Message message = messageAdapter.getItem(position);
                final User friend = new User();
                friend.setIdentifiant(message.getUserID().toString());
                friend.setUsername(message.getUsername());
                friend.setImageUrl(message.getImageUrl());
                new AlertDialog.Builder(getActivity())
                        .setTitle("Ajouter un ami")
                        .setMessage("Voulez vous vraiment ajouter cet utilisateur à vos amis ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (FriendsDB.addFriend(friend, getActivity())) {
                                    Toast.makeText(getActivity(), "Amis ajouté", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "L'ajout à échoué", Toast.LENGTH_LONG).show();
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
    }

    private void sendMessage(final EditText editMessage, User user,Channel channel) {

        final String messageText = editMessage.getText().toString();
        editMessage.setText("");
        HashMap<String, String> postDatas = new HashMap<>(3);
        ChannelListFragmentActivity activity = (ChannelListFragmentActivity) getActivity();
        postDatas.put("message", messageText);
        postDatas.put("accesstoken", user.getToken());
        postDatas.put("channelid", channel.getChannelID().toString());
        postDatas.put("latitude",String.valueOf(activity.getmCurrentLocation().getLatitude()));
        postDatas.put("longitude",String.valueOf(activity.getmCurrentLocation().getLongitude()));

        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_SEND_MESSAGES,postDatas);
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
                    Toast.makeText(getActivity(), "Cannot send message", Toast.LENGTH_LONG);
                    editMessage.setText(messageText);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onError(String error) {

    }


}
