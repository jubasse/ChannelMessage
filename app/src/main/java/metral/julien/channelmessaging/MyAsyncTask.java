package metral.julien.channelmessaging;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.List;

import metral.julien.channelmessaging.Model.Response;


public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private onWsRequestListener listener;
    private List<NameValuePair> nameValuePairs;
    String url = "";

    public MyAsyncTask(String url, List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... arg0) {

        ApiManager apiManager = new ApiManager();
        if(url == ApiManager.BASE_URL_CONNECT){
            return apiManager.connect(nameValuePairs);
        }
        if(url == ApiManager.BASE_URL_GET_CHANNELS){
            return apiManager.channels(nameValuePairs);
        }
        if(url == ApiManager.BASE_URL_GET_MESSAGES){
            return apiManager.messages(nameValuePairs);
        }
        if(url == ApiManager.BASE_URL_SEND_MESSAGES){
            return apiManager.sendMessage(nameValuePairs);
        }
        if(url == ApiManager.BASE_URL_GET_MESSAGES_FROM_FRIENDS){
            return apiManager.messagesFromFriend(nameValuePairs);
        }
        if(url == ApiManager.BASE_URL_SEND_MESSAGES_TO_FRIENDS){
            return apiManager.sendMessageToFriend(nameValuePairs);
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
