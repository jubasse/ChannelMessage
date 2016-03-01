package metral.julien.channelmessaging;

import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Julien on 02/02/2016.
 */
public class ApiManager {

    public static String BASE_URL_API = "http://raphaelbischof.fr/messaging/";
    public static String BASE_URL_CONNECT = BASE_URL_API+"?function=connect";
    public static String BASE_URL_GET_CHANNELS = BASE_URL_API+"?function=getchannels";
    public static String BASE_URL_GET_MESSAGES = BASE_URL_API+"?function=getmessages";
    public static String BASE_URL_SEND_MESSAGES = BASE_URL_API+"?function=sendmessage";
    public static String BASE_URL_GET_MESSAGES_FROM_FRIENDS = BASE_URL_API+"?function=getmessages";
    public static String BASE_URL_SEND_MESSAGES_TO_FRIENDS = BASE_URL_API+"?function=sendmessage";

    public String connect(List<NameValuePair> nameValuePairs)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL_CONNECT);
        return sendRequest(nameValuePairs, httpclient, httppost);
    }

    public String channels(List<NameValuePair> nameValuePairs)
    {
//        ChannelList channelList = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL_GET_CHANNELS);
        return sendRequest(nameValuePairs, httpclient, httppost);
    }


    public String messages(List<NameValuePair> nameValuePairs)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL_GET_MESSAGES);
        return sendRequest(nameValuePairs, httpclient, httppost);
    }

    public String messagesFromFriend(List<NameValuePair> nameValuePairs)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL_GET_MESSAGES_FROM_FRIENDS);
        return sendRequest(nameValuePairs, httpclient, httppost);
    }

    public String sendMessage(List<NameValuePair> nameValuePairs)
    {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL_SEND_MESSAGES);
        return sendRequest(nameValuePairs, httpclient, httppost);
    }

    public String sendMessageToFriend(List<NameValuePair> nameValuePairs)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL_SEND_MESSAGES_TO_FRIENDS);
        return sendRequest(nameValuePairs, httpclient, httppost);
    }

    @Nullable
    private String sendRequest(List<NameValuePair> nameValuePairs, HttpClient httpclient, HttpPost httppost) {
        String content = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            //TODO Handler
        }
        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            content = EntityUtils.toString(response.getEntity());
            Log.wtf("json", content);
            return content;
        } catch (IOException e) {
            //TODO Handler
        }
        return content;
    }

}
