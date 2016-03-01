package metral.julien.channelmessaging;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import metral.julien.channelmessaging.Fragment.ChannelListFragment;
import metral.julien.channelmessaging.Model.User;


public class ChannelListFragmentActivity extends FragmentActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            User user = (User) getIntent().getSerializableExtra("User");
            ChannelListFragment channelListFragment = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_channel_list);
            channelListFragment.setUser(user);
        }
    }

}
