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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import metral.julien.channelmessaging.Utils.DownloadImageTask;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.Utils.ImageRounder;

/**
 * Created by Julien on 01/03/2016.
 */
public class FriendAdapter extends BaseAdapter {

    private ArrayList<User> friendList;
    private Context context;

    public FriendAdapter(ArrayList<User> friendList,Context context)
    {
        this.context = context;
        this.friendList = friendList;
    }
    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public User getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.friend_layout,parent,false);

        ImageView friendImage = (ImageView) view.findViewById(R.id.friendImageView);
        TextView username = (TextView) view.findViewById(R.id.friendUsername);
        username.setText(friendList.get(position).getUsername());


        if(friendList.get(position).getImageUrl() != null){
            String fileName = friendList.get(position).getIdentifiant();
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("ChannelMessaging", Context.MODE_PRIVATE);

            Bitmap profilePhoto = loadImageFromStorage(directory.getPath(), fileName);
            if( profilePhoto != null) {
                Bitmap rounded = ImageRounder.getRoundedCornerBitmap(profilePhoto, 50);
                friendImage.setImageBitmap(rounded);
            } else {
                new DownloadImageTask(
                        friendList.get(position).getIdentifiant(),
                        friendList.get(position).getImageUrl(),
                        friendImage,
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
