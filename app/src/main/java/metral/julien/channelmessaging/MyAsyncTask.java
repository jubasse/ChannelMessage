package metral.julien.channelmessaging;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;

import metral.julien.channelmessaging.Model.Response;


public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private onWsRequestListener listener;
    private List<NameValuePair> nameValuePairs;
    private HashMap<String, String> postDatas;
    String url = "";

    public MyAsyncTask(String url, List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
        this.url = url;
    }

    public MyAsyncTask(String url, HashMap<String, String> postDatas) {
        this.postDatas = postDatas;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... arg0) {

        ApiManager apiManager = new ApiManager();
        if(url == ApiManager.BASE_URL_CONNECT){
            return apiManager.connect(postDatas);
        }
        if(url == ApiManager.BASE_URL_GET_CHANNELS){
            return apiManager.channels(postDatas);
        }
        if(url == ApiManager.BASE_URL_GET_MESSAGES){
            return apiManager.messages(postDatas);
        }
        if(url == ApiManager.BASE_URL_SEND_MESSAGES){
            return apiManager.sendMessage(postDatas);
        }
        if(url == ApiManager.BASE_URL_GET_MESSAGES_FROM_FRIENDS){
            return apiManager.messagesFromFriend(postDatas);
        }
        if(url == ApiManager.BASE_URL_SEND_MESSAGES_TO_FRIENDS){
            return apiManager.sendMessageToFriend(postDatas);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onCompleted(s);
    }

    public void setOnNewWsRequestListener(onWsRequestListener listener) {
        this.listener = listener;
    }
}
