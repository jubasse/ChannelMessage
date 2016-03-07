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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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

    public String connect(HashMap<String, String> postDatas){
        return this.performPostCall(BASE_URL_CONNECT,postDatas);
    }

    public String channels(HashMap<String, String> postDatas){
        return this.performPostCall(BASE_URL_GET_CHANNELS, postDatas);
    }

    public String messages(HashMap<String, String> postDatas)
    {
        return this.performPostCall(BASE_URL_GET_MESSAGES,postDatas);
    }

    public String messagesFromFriend(HashMap<String, String> postDatas)
    {
        return this.performPostCall(BASE_URL_GET_MESSAGES_FROM_FRIENDS,postDatas);
    }

    public String sendMessage(HashMap<String, String> postDatas)
    {
        return this.performPostCall(BASE_URL_SEND_MESSAGES,postDatas);
    }

    public String sendMessageToFriend(HashMap<String, String> postDatas)
    {
        return this.performPostCall(BASE_URL_SEND_MESSAGES_TO_FRIENDS,postDatas);
    }

    public String performPostCall(String requestURL, HashMap<String, String>
            postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws
            UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }



}
