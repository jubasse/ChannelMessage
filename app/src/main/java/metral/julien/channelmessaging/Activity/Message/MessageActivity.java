package metral.julien.channelmessaging.Activity.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import metral.julien.channelmessaging.Activity.Map.GPSActivity;
import metral.julien.channelmessaging.Activity.Map.MapActivity;
import metral.julien.channelmessaging.Adapter.MessageAdapter;
import metral.julien.channelmessaging.Database.FriendsDB;
import metral.julien.channelmessaging.Fragment.AddToFriendsDialogFragment;
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

public class MessageActivity extends GPSActivity implements onWsRequestListener {

    private MessageList messages;
    private ListView messageListView;
    private TextView channelName;
    private User user;
    private Channel channel;
    private Button sendButton;
    private EditText editMessage;
    private MessageAdapter messageAdapter;
    private Message message;
    private User friend;
    private BookLoading loader;
    private Button photoButton;
    private static int PICTURE_REQUEST_CODE = 666;
    private Uri uri = null;
    private String mCurrentPhotoPath;
    private File photoFile;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            String channelid = intent.getExtras().getString("channelid");
            if(channelid.equals(channel.getChannelID())){
                loadDatas();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        loader = (BookLoading) findViewById(R.id.bookloading);
        loader.setVisibility(View.VISIBLE);
        loader.start();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            channel = (Channel) getIntent().getSerializableExtra("Channel");
            user = (User) getIntent().getSerializableExtra("User");
        }

        loadDatas();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(mMessageReceiver, intentFilter);
    }

    private void loadDatas() {
        HashMap<String, String> postDatas = new HashMap<>(1);
        postDatas.put("accesstoken", user.getToken());
        postDatas.put("channelid", channel.getChannelID().toString());
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_MESSAGES,postDatas);

        task.setOnNewWsRequestListener(this);

        task.execute();
    }

    private void sendMessage(final EditText editMessage, User user,Channel channel) {

        final String messageText = editMessage.getText().toString();
        editMessage.setText("");

        HashMap<String, String> postDatas = new HashMap<>(4);
        postDatas.put("message", messageText);
        postDatas.put("accesstoken", user.getToken());
        postDatas.put("channelid", channel.getChannelID().toString());
        postDatas.put("latitude", String.valueOf(this.getLatitude()));
        postDatas.put("longitude", String.valueOf(this.getLongitude()));

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
                    Toast.makeText(MessageActivity.this, "Cannot send message", Toast.LENGTH_LONG).show();
                    editMessage.setText(messageText);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public DialogInterface.OnClickListener alertDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    if (FriendsDB.addFriend(friend, MessageActivity.this)) {
                        Toast.makeText(MessageActivity.this, "Amis ajouté", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MessageActivity.this, "L'ajout à échoué", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1:
                    Intent intent = new Intent(MessageActivity.this, MapActivity.class);
                    intent.putExtra("User",user);
                    intent.putExtra("Message",message);
                    startActivity(intent);
                    break;
                case 2:
                    break;
            }
        }
    };

    @Override
    public void onCompleted(String json) {
        if(loader.getVisibility() != View.INVISIBLE){
            loader.setVisibility(View.INVISIBLE);
        }

        Gson gson = new Gson();
        Log.wtf("JsonMessages", json);

        try{
            messages = gson.fromJson(json,MessageList.class);
        }catch (Exception e){
            messages = null;
        }
        messageListView = (ListView) findViewById(R.id.messageList);
        channelName = (TextView) findViewById(R.id.channelName);
        sendButton = (Button) findViewById(R.id.sendButton);
        photoButton = (Button) findViewById(R.id.photoButton);
        editMessage = (EditText) findViewById(R.id.messageText);

        channelName.setText(channel.getName());

        messageAdapter = new MessageAdapter(messages.getMessages(),MessageActivity.this);
        messageListView.setAdapter(messageAdapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                message = messageAdapter.getItem(position);
                friend = new User();
                friend.setIdentifiant(message.getUserID().toString());
                friend.setUsername(message.getUsername());
                friend.setImageUrl(message.getImageUrl());
                FragmentManager fm = getSupportFragmentManager();
                new AddToFriendsDialogFragment()
                        .setContext(MessageActivity.this)
                        .setListener(alertDialogClick)
                        .show(fm, "Test")
                ;

            }
        });

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_REQUEST_CODE){
            try {
                ImageRounder.resizeFile(photoFile,MessageActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<NameValuePair> postDatas = new ArrayList<>();
            postDatas.add(new BasicNameValuePair("accesstoken",user.getToken()));
            postDatas.add(new BasicNameValuePair("channelid",channel.getChannelID().toString()));
            UploadFileToServer upfts = new UploadFileToServer(MessageActivity.this,photoFile.getPath(),postDatas,uploadMessagePhoto);
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

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

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

    @Override
    public void onError(String error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }
}
