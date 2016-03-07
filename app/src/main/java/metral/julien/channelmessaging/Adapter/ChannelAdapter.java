package metral.julien.channelmessaging.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.R;

/**
 * Created by Julien on 08/02/2016.
 */
public class ChannelAdapter extends BaseAdapter {

    private List<Channel> list;
    private List<Channel> original;

    private Context context;

    public ChannelAdapter(List<Channel> list, Context context) {
        this.list = list;
        this.original = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Channel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(Channel channel)
    {
        list.add(channel);
        notifyDataSetChanged();
    }

    public void setSearchResult(String searched)
    {
        List<Channel> newList = new ArrayList<>();
        for (Channel channel:original) {
            if(channel.getName().toString().contains(searched)){
                newList.add(channel);
            }
        }
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.channel_layout,parent,false);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView nbreConnected = (TextView) view.findViewById(R.id.nbrConnected);
        name.setText(list.get(position).getName());
        nbreConnected.setText("Nombre de connectés : "+ list.get(position).getConnectedUsers());

        return view;
    }

    public void removeAll() {
        list.clear();
        notifyDataSetChanged();
    }
}
