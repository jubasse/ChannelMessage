package metral.julien.channelmessaging.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Model.Message;
import metral.julien.channelmessaging.R;

/**
 * Created by Julien on 08/02/2016.
 */
public class MessageAdapter extends BaseAdapter{
    private List<Message> list;

    private Context context;

    public MessageAdapter(List<Message> list, Context context) {
        Collections.reverse(list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Message getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(Message message)
    {
        list.add(message);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.message_layout,parent,false);

        TextView message = (TextView) view.findViewById(R.id.message);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView username = (TextView) view.findViewById(R.id.username);

        message.setText(list.get(position).getMessage());
        date.setText(list.get(position).getDate());
        username.setText(list.get(position).getUsername());

        return view;
    }

    public void removeAll() {
        list.clear();
        notifyDataSetChanged();
    }
}
