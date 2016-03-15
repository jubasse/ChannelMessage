package metral.julien.channelmessaging.Activity.Channel;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;

import com.victor.loading.newton.NewtonCradleLoading;

import metral.julien.channelmessaging.Activity.Map.GPSActivity;
import metral.julien.channelmessaging.Activity.Message.MessageActivity;
import metral.julien.channelmessaging.Fragment.ChannelListFragment;
import metral.julien.channelmessaging.Fragment.MessageFragment;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;


public class ChannelListFragmentActivity extends GPSActivity {

    private User user;
    private Channel channel;
    private ChannelListFragment channelListFragment;
    private MessageFragment messageFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user = (User) getIntent().getSerializableExtra("User");
            if(getIntent().getAction() == "redirectToChannel"){
                channel = (Channel) getIntent().getSerializableExtra("Channel");
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("Channel", channel);
                intent.putExtra("User", user);
                startActivity(intent);
            }
            channelListFragment = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_channel_list);
            channelListFragment.setUser(user);
        }
        messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_message);
    }

    public AdapterView.OnItemClickListener OnItemChannelClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            channel = channelListFragment.getAdapter().getItem(position);
            if(messageFragment != null && messageFragment.isInLayout()){
                messageFragment.setUserAndChannel(user,channel);
            } else {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("Channel", channel);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        }
    };

}
