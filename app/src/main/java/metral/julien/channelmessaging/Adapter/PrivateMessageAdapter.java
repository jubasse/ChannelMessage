package metral.julien.channelmessaging.Adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import metral.julien.channelmessaging.DownloadImageTask;
import metral.julien.channelmessaging.Model.PrivateMessage;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.utils.ImageRounder;

/**
 * Created by Julien on 01/03/2016.
 */
public class PrivateMessageAdapter extends BaseAdapter {

    private Context context;
    private List<PrivateMessage> messageList;
    private User user;

    public PrivateMessageAdapter(List<PrivateMessage> messageList,User user, Context context)
    {
        Collections.reverse(messageList);
        this.messageList = messageList;
        this.user = user;
        this.context = context;
    }
    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public PrivateMessage getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(PrivateMessage message)
    {
        messageList.add(message);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;

        if(messageList.get(position).getSendbyme() != 1){
            view = inflater.inflate(R.layout.private_message_layout,parent,false);
        } else {
            view = inflater.inflate(R.layout.private_message_layout_me,parent,false);
        }


        TextView messageFromFriend = (TextView) view.findViewById(R.id.privateMessageFromFriend);
        TextView friendUsername = (TextView) view.findViewById(R.id.privateMessageFriendUsername);
        ImageView friendProfileImage = (ImageView) view.findViewById(R.id.privateMessageProfileImage);

        messageFromFriend.setText(messageList.get(position).getMessage());
        if(messageList.get(position).getEverRead() == 0){
            messageFromFriend.setTypeface(null, Typeface.BOLD);
        }

        friendUsername.setText(messageList.get(position).getUsername());

        if(messageList.get(position).getSendbyme() == 0){
            this.setImageOfFriend(messageList.get(position),friendProfileImage);
        } else {
            this.setImageOfUser(messageList.get(position),friendProfileImage);
        }

        return view;
    }

    private void setImageOfUser(PrivateMessage privateMessage, ImageView friendProfileImage) {

        if(privateMessage.getImageUrl() != null){
            String fileName = user.getIdentifiant().toString();
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("ChannelMessaging", Context.MODE_PRIVATE);

            Bitmap profilePhoto = loadImageFromStorage(directory.getPath(), fileName);
            if( profilePhoto != null) {
                Bitmap rounded = ImageRounder.getRoundedCornerBitmap(profilePhoto, 50);
                friendProfileImage.setImageBitmap(rounded);
            } else {
                new DownloadImageTask(
                        user.getIdentifiant().toString(),
                        privateMessage.getImageUrl(),
                        friendProfileImage,
                        this.context
                ).execute();
            }
        }
    }

    private void setImageOfFriend(PrivateMessage privateMessage, ImageView friendProfileImage) {
        if(privateMessage.getImageUrl() != null){
            String fileName = privateMessage.getUserID().toString();
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("ChannelMessaging", Context.MODE_PRIVATE);

            Bitmap profilePhoto = loadImageFromStorage(directory.getPath(), fileName);
            if( profilePhoto != null) {
                Bitmap rounded = ImageRounder.getRoundedCornerBitmap(profilePhoto, 50);
                friendProfileImage.setImageBitmap(rounded);
            } else {
                new DownloadImageTask(
                        privateMessage.getUserID().toString(),
                        privateMessage.getImageUrl(),
                        friendProfileImage,
                        this.context
                ).execute();
            }
        }
        
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
