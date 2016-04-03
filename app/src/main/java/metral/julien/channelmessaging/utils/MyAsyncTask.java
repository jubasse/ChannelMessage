package metral.julien.channelmessaging.Utils;


import android.os.AsyncTask;

import com.google.android.gms.common.api.Api;

import java.util.HashMap;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private onWsRequestListener listener;
    private HashMap<String, String> postDatas;
    String url = "";

    public MyAsyncTask(String url, HashMap<String, String> postDatas) {
        this.postDatas = postDatas;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... arg0) {

        ApiManager apiManager = new ApiManager();
        if(url.equals(ApiManager.BASE_URL_CONNECT)){
            return apiManager.connect(postDatas);
        }
        if(url.equals(ApiManager.BASE_URL_GET_CHANNELS)){
            return apiManager.channels(postDatas);
        }
        if(url.equals(ApiManager.BASE_URL_GET_MESSAGES)){
            return apiManager.messages(postDatas);
        }
        if(url.equals(ApiManager.BASE_URL_SEND_MESSAGES)){
            return apiManager.sendMessage(postDatas);
        }
        if(url.equals(ApiManager.BASE_URL_VERIFY_TOKEN)){
            return apiManager.verifyAccessToken(postDatas);
        }
        if(url.equals(ApiManager.BASE_URL_GET_MESSAGES_FROM_FRIENDS)){
            return apiManager.messagesFromFriend(postDatas);
        }
        if(url.equals(ApiManager.BASE_URL_SEND_MESSAGES_TO_FRIENDS)){
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
