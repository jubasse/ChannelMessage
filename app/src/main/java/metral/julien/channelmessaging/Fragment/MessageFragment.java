package metral.julien.channelmessaging.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.victor.loading.book.BookLoading;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import metral.julien.channelmessaging.Activity.Channel.ChannelListFragmentActivity;
import metral.julien.channelmessaging.Activity.Map.MapActivity;
import metral.julien.channelmessaging.Adapter.MessageAdapter;
import metral.julien.channelmessaging.Database.FriendsDB;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.Message;
import metral.julien.channelmessaging.Model.MessageList;
import metral.julien.channelmessaging.Model.Response;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.Utils.ApiManager;
import metral.julien.channelmessaging.Utils.ImageRounder;
import metral.julien.channelmessaging.Utils.MyAsyncTask;
import metral.julien.channelmessaging.Utils.UploadFileToServer;
import metral.julien.channelmessaging.Utils.onWsRequestListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment implements onWsRequestListener {

    private User user;
    private Channel channel;
    private View view;
    private MessageList messages;
    private ListView messageListView;
    private TextView channelName;
    private Button sendButton;
    private User friend;
    private Message message;
    private EditText editMessage;
    private MessageAdapter messageAdapter;
    private BookLoading loader;
    private Button photoButton;
    private static int PICTURE_REQUEST_CODE = 666;
    private Uri uri = null;
    private File photoFile;
    private String mCurrentPhotoPath;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadDatas();
        }
    };

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        getActivity().registerReceiver(mMessageReceiver, intentFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        loader = (BookLoading) view.findViewById(R.id.bookloading);
        loader.setVisibility(View.INVISIBLE);
        loader.start();
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
        loader.setVisibility(View.VISIBLE);
        this.setUser(user);
        this.setChannel(channel);
        loadDatas();
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
        photoButton = (Button) view.findViewById(R.id.photoButton);
        editMessage = (EditText) view.findViewById(R.id.messageText);

        channelName.setText(channel.getName());

        messageAdapter = new MessageAdapter(messages.getMessages(),getActivity());
        messageListView.setAdapter(messageAdapter);

        loader.setVisibility(View.INVISIBLE);

        messageListView.setSelection(messageAdapter.getCount() - 1);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(editMessage, user, channel);
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                message = messageAdapter.getItem(position);
                friend = new User();
                friend.setIdentifiant(message.getUserID().toString());
                friend.setUsername(message.getUsername());
                friend.setImageUrl(message.getImageUrl());
                FragmentManager fm = getFragmentManager();
                new AddToFriendsDialogFragment()
                        .setContext(getActivity())
                        .setListener(alertDialogClick)
                        .show(fm, "Test")
                ;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_REQUEST_CODE){
            try {
                ImageRounder.resizeFile(photoFile, getActivity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<NameValuePair> postDatas = new ArrayList<>();
            postDatas.add(new BasicNameValuePair("accesstoken",user.getToken()));
            postDatas.add(new BasicNameValuePair("channelid",channel.getChannelID().toString()));
            UploadFileToServer upfts = new UploadFileToServer(getActivity(),photoFile.getPath(),postDatas,uploadMessagePhoto);
            upfts.execute();
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, PICTURE_REQUEST_CODE);
            }
        }
    }

    public UploadFileToServer.OnUploadFileListener uploadMessagePhoto = new UploadFileToServer.OnUploadFileListener() {
        @Override
        public void onResponse(String result) {

        }

        @Override
        public void onFailed(IOException error) {

        }
    };

    public DialogInterface.OnClickListener alertDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    if (FriendsDB.addFriend(friend, getActivity())) {
                        Toast.makeText(getActivity(), "Amis ajouté", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "L'ajout à échoué", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1:
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("User",user);
                    intent.putExtra("Message",message);
                    startActivity(intent);
                    break;
                case 2:
                    break;
            }
        }
    };

    private void sendMessage(final EditText editMessage, User user,Channel channel) {

        final String messageText = editMessage.getText().toString();
        editMessage.setText("");
        HashMap<String, String> postDatas = new HashMap<>(3);
        ChannelListFragmentActivity activity = (ChannelListFragmentActivity) getActivity();
        postDatas.put("message", messageText);
        postDatas.put("accesstoken", user.getToken());
        postDatas.put("channelid", channel.getChannelID().toString());
        postDatas.put("latitude", String.valueOf(activity.getLatitude()));
        postDatas.put("longitude",String.valueOf(activity.getLongitude()));

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
                    Toast.makeText(getActivity(), "Cannot send message", Toast.LENGTH_LONG).show();
                    editMessage.setText(messageText);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
