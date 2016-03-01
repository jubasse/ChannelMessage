package metral.julien.channelmessaging.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import metral.julien.channelmessaging.Adapter.ChannelAdapter;
import metral.julien.channelmessaging.ApiManager;
import metral.julien.channelmessaging.FriendsActivity;
import metral.julien.channelmessaging.MessageActivity;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.ChannelList;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.MyAsyncTask;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.onWsRequestListener;

public class ChannelListFragment extends Fragment {

    private User user;
    private ChannelList channels;
    private ListView channelListView;
    private Button myFriendsButton;

    public ChannelListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUser(User user) {
        this.user = user;
        fillChannelList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_channel_list, container, false);

        myFriendsButton = (Button) view.findViewById(R.id.myFriendsButton);
        channelListView = (ListView) view.findViewById(R.id.channelListView);
        myFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        return view;
    }

    private void fillChannelList() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("accesstoken", user.getToken()));
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_CHANNELS, nameValuePairs);
        task.setOnNewWsRequestListener(new onWsRequestListener() {
            @Override
            public void onCompleted(String json) {
                Gson gson = new Gson();
                Log.wtf("JsonChannels", json.toString());
                try {
                    channels = gson.fromJson(json, ChannelList.class);
                } catch (Exception e) {
                    channels = null;
                }
                final ChannelAdapter adapter = new ChannelAdapter(channels.getChannels(), getActivity());
                channelListView.setAdapter(adapter);
                channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Channel channel = adapter.getItem(position);
                        Intent intent = new Intent(getActivity().getApplicationContext(), MessageActivity.class);
                        intent.putExtra("Channel", channel);
                        intent.putExtra("User", user);
                        startActivity(intent);
                    }
                });
                Log.wtf("ObjectChannels", channels.getChannels().get(1).getName());
            }

            @Override
            public void onError(String error) {

            }
        });
        task.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
