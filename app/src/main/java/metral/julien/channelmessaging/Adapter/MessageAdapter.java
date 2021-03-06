package metral.julien.channelmessaging.Adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import metral.julien.channelmessaging.Utils.DownloadImageTask;
import metral.julien.channelmessaging.Model.Message;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.Utils.ImageRounder;

/**
 * Created by Julien on 08/02/2016.
 */
public class MessageAdapter extends BaseAdapter {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        String messageUrl = list.get(position).getMessageImageUrl();
        if(messageUrl != "" && messageUrl != null){
            view = inflater.inflate(R.layout.message_image_layout,parent,false);
        } else {
            view = inflater.inflate(R.layout.message_layout,parent,false);
        }

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView username = (TextView) view.findViewById(R.id.username);
        ImageView messageImage = (ImageView) view.findViewById(R.id.messageImage);
        if(messageUrl != "" && messageUrl != null){
            ImageView messagingImage = (ImageView) view.findViewById(R.id.messagingImage);
            Picasso.with(context).load(messageUrl).into(messagingImage);
        } else {
            TextView message = (TextView) view.findViewById(R.id.message);
            message.setText(list.get(position).getMessage());
        }

        date.setText(list.get(position).getDate());
        username.setText(list.get(position).getUsername());

        if(list.get(position).getImageUrl() != null){
            String fileName = list.get(position).getUserID().toString();
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("ChannelMessaging", Context.MODE_PRIVATE);

            Bitmap profilePhoto = loadImageFromStorage(directory.getPath(), fileName);
            if( profilePhoto != null) {
                Bitmap rounded = ImageRounder.getRoundedCornerBitmap(profilePhoto,50);
                messageImage.setImageBitmap(rounded);
            } else {
                new DownloadImageTask(
                        list.get(position).getUserID().toString(),
                        list.get(position).getImageUrl(),
                        messageImage,
                        this.context
                ).execute();
            }
        }
        return view;
    }

    private Bitmap loadImageFromStorage(String path, String fileName)
    {
        File f = new File(path, fileName+".png");
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }
}
