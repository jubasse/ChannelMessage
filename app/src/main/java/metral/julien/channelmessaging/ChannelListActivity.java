package metral.julien.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import metral.julien.channelmessaging.Adapter.ChannelAdapter;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.ChannelList;
import metral.julien.channelmessaging.Model.User;

public class ChannelListActivity extends Activity {

    private User user;
    private ChannelList channels;
    private ListView channelListView;
    private Button myFriendsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user = (User) getIntent().getSerializableExtra("User");
        }
        Log.wtf("TokenChannelList", user.getToken());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("accesstoken", user.getToken()));
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_CHANNELS,nameValuePairs);
        task.execute();

        myFriendsButton = (Button) findViewById(R.id.myFriends);
        myFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChannelListActivity.this, FriendsActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        task.setOnNewWsRequestListener(new onWsRequestListener() {
            @Override
            public void onCompleted(String json) {
                Gson gson = new Gson();
                Log.wtf("JsonChannels",json.toString());
                try{
                    channels = gson.fromJson(json,ChannelList.class);
                }catch (Exception e){
                    channels = null;
                }
                channelListView = (ListView) findViewById(R.id.channelListView);
                final ChannelAdapter adapter = new ChannelAdapter(channels.getChannels(),ChannelListActivity.this);
                channelListView.setAdapter(adapter);
                channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Channel channel = adapter.getItem(position);
                        Intent intent = new Intent(ChannelListActivity.this,MessageActivity.class);
                        intent.putExtra("Channel",channel);
                        intent.putExtra("User",user);
                        startActivity(intent);
                    }
                });
                Log.wtf("ObjectChannels",channels.getChannels().get(1).getName());
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
