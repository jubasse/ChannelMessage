package metral.julien.channelmessaging.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.HashMap;

import metral.julien.channelmessaging.Activity.Channel.ChannelListFragmentActivity;
import metral.julien.channelmessaging.Activity.Message.MessageActivity;
import metral.julien.channelmessaging.Model.Channel;
import metral.julien.channelmessaging.Utils.ApiManager;
import metral.julien.channelmessaging.Model.Response;
import metral.julien.channelmessaging.Model.User;
import metral.julien.channelmessaging.R;
import metral.julien.channelmessaging.Utils.MyAsyncTask;
import metral.julien.channelmessaging.Utils.onWsRequestListener;

public class LoginActivity extends NotificationActivity {

    private EditText identifiant;
    private EditText password;
    private Button buttonValid;
    private String accessToken;
    private Response res;
    private Channel channel = null;
    private User user;
    private Boolean isValidToken = false;
    private SharedPreferences sharedPref;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = new User();

        Context context = LoginActivity.this;
        sharedPref = context.getSharedPreferences("metral.julien.channelmessaging", Context.MODE_PRIVATE);

        extras = getIntent().getExtras();
        if(getIntent().getAction() == "redirectToChannel"){
            channel = new Channel();
            channel.setChannelID(Integer.valueOf(extras.getString("channelid")));
        }

        this.verifyAccessToken(new onWsRequestListener() {
            @Override
            public void onCompleted(String json) {
                Gson gson = new Gson();
                res = gson.fromJson(json, Response.class);

                if (res.getCode() == 200) {
                    Intent intent = new Intent(LoginActivity.this, ChannelListFragmentActivity.class);
                    intent.putExtra("User", user);
                    if (channel != null) {
                        intent.putExtra("Channel", channel);
                        intent.setAction("redirectToChannel");
                    }
                    startActivity(intent);
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        identifiant = (EditText) findViewById(R.id.idTxt);
        password = (EditText) findViewById(R.id.mdpTxt);
        buttonValid = (Button) findViewById(R.id.validBut);

        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = identifiant.getText().toString();
                String pass = password.getText().toString();

                user.setIdentifiant(username);
                user.setPassword(pass);

                HashMap<String, String> postDatas = new HashMap<>(2);

                postDatas.put("username", username);
                postDatas.put("password", pass);
                postDatas.put("registrationid",getRegistrationId());

                MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_CONNECT,postDatas);
                task.execute();

                task.setOnNewWsRequestListener(new onWsRequestListener() {
                    @Override
                    public void onCompleted(String json) {
                        Gson gson = new Gson();
                        res = gson.fromJson(json,Response.class);

                        Log.wtf("AccessToken:",res.getAccessToken());
                        accessToken = res.getAccessToken();
                        sharedPref.edit().putString("accesstoken", accessToken).apply();
                        sharedPref.edit().putString("username", username).apply();
                        user.setToken(res.getAccessToken());
                        if(channel != null){
                            Intent intent = new Intent(LoginActivity.this,MessageActivity.class);
                            intent.putExtra("User",user);
                            intent.putExtra("Channel",channel);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this,ChannelListFragmentActivity.class);
                            intent.putExtra("User",user);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });

    }

    private void verifyAccessToken(onWsRequestListener listener) {
        String username = sharedPref.getString("username",null);
        String accesstoken = sharedPref.getString("accesstoken",null);
        if(username != null && accesstoken != null){
            user.setToken(accesstoken);
            user.setIdentifiant(username);

            HashMap<String, String> postDatas = new HashMap<>(1);

            postDatas.put("accesstoken", user.getToken());

            MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_VERIFY_TOKEN,postDatas);
            task.execute();

            task.setOnNewWsRequestListener(listener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
