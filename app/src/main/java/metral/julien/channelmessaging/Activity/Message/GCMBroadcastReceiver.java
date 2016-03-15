package metral.julien.channelmessaging.Activity.Message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Set;

import metral.julien.channelmessaging.Activity.LoginActivity;
import metral.julien.channelmessaging.R;

public class GCMBroadcastReceiver extends BroadcastReceiver {
    public GCMBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Set<String> keys = intent.getExtras().keySet();
        if(keys.contains("message") && keys.contains("channelid") && keys.contains("fromUser")){
            showNotification(
                    context,
                    intent.getExtras().getString("message"),
                    intent.getExtras().getString("channelid"),
                    intent.getExtras().getString("fromUser")
            );
        }
    }

    private void showNotification(Context context, String message, String channelid, String fromUser) {
        Intent resultIntent = new Intent(context, LoginActivity.class);
        resultIntent.setAction("redirectToChannel");
        resultIntent.putExtra("channelid",channelid);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder noti = new NotificationCompat.Builder(context)
                .setContentTitle(fromUser)
                .setContentText(message)
                .setVibrate(new long[]{ 0,250,250,250,250,250,250,250,250,100,100,250,250,250,250,100,100,250})
                        //Set the color of the notification led (example on Nexus 5)
                .setLights(Color.parseColor("#006ab9"), 2000, 1000)
                .setSmallIcon(R.drawable.ic_media_play);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            noti.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        noti.setContentIntent(resultPendingIntent);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle(fromUser);
        inboxStyle.addLine(message);

        int mNotificationId = 99;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, noti.build());
    }
}
