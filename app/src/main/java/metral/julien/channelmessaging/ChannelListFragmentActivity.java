package metral.julien.channelmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import metral.julien.channelmessaging.Fragment.ChannelListFragment;
import metral.julien.channelmessaging.Fragment.MessageFragment;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.User;


public class ChannelListFragmentActivity extends GPSActivity {

    private User user;
    private ChannelListFragment channelListFragment;
    private MessageFragment messageFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            user = (User) getIntent().getSerializableExtra("User");
            channelListFragment = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_channel_list);
            channelListFragment.setUser(user);
        }
        messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_message);
    }

    public AdapterView.OnItemClickListener OnItemChannelClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(messageFragment != null && messageFragment.isInLayout()){
                Channel channel = channelListFragment.getAdapter().getItem(position);
                messageFragment.setUserAndChannel(user,channel);
            } else {
                Channel channel = channelListFragment.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("Channel", channel);
                intent.putExtra("User", user);
                startActivity(intent);
            }

        }
    };

    public View.OnClickListener OnSendButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };



}
