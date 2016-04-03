package metral.julien.channelmessaging.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import metral.julien.channelmessaging.Adapter.ChannelAdapter;
import metral.julien.channelmessaging.Utils.ApiManager;
import metral.julien.channelmessaging.Activity.Channel.ChannelListFragmentActivity;
import metral.julien.channelmessaging.Activity.FriendsActivity;
import metral.julien.channelmessaging.Model.ChannelList;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.Utils.MyAsyncTask;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.Utils.onWsRequestListener;

public class ChannelListFragment extends Fragment {

    private User user;
    private ChannelList channels;
    private ListView channelListView;
    private Button myFriendsButton;
    private ChannelAdapter adapter;

    public ChannelListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setUser(User user) {
        this.user = user;
        fillChannelList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(user != null){
            fillChannelList();
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_channel, menu);
        SearchView search = (SearchView) menu.findItem(R.id.search_channel_input).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setSearchResult(newText);
                return false;
            }
        });
    }

    private void fillChannelList() {
        HashMap<String, String> postDatas = new HashMap<>(1);
        postDatas.put("accesstoken", user.getToken());
        MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_GET_CHANNELS, postDatas);
        task.setOnNewWsRequestListener(new onWsRequestListener() {
            @Override
            public void onCompleted(String json) {
                Gson gson = new Gson();
                Log.wtf("JsonChannels", json);
                try {
                    channels = gson.fromJson(json, ChannelList.class);
                } catch (Exception e) {
                    channels = null;
                }
                adapter = new ChannelAdapter(channels.getChannels(), getActivity());
                channelListView.setAdapter(adapter);
                ChannelListFragmentActivity activity = (ChannelListFragmentActivity) getActivity();
                if(activity != null){
                    channelListView.setOnItemClickListener(activity.OnItemChannelClickListener);
                }
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

    public ChannelAdapter getAdapter() {
        return adapter;
    }
}
