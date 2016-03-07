package metral.julien.channelmessaging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import metral.julien.channelmessaging.Model.Response;
import metral.julien.channelmessaging.Model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText identifiant;
    private EditText password;
    private Button buttonValid;
    private String accessToken;
    private String username;
    private String pass;
    private Response res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final User user = new User();

        Context context = LoginActivity.this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                "metral.julien.channelmessaging", Context.MODE_PRIVATE);

//        if(sharedPref.contains("accessToken")){
//
//            accessToken = sharedPref.getString("accessToken",null);
//            username = sharedPref.getString("username",null);
//            pass = sharedPref.getString("pass",null);
//
//            user.setToken(accessToken);
//            user.setIdentifiant(username);
//            user.setPassword(pass);
//        }

        identifiant = (EditText) findViewById(R.id.idTxt);
        password = (EditText) findViewById(R.id.mdpTxt);
        buttonValid = (Button) findViewById(R.id.validBut);

        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = identifiant.getText().toString();
                String pass = password.getText().toString();

                user.setIdentifiant(username);
                user.setPassword(pass);

                HashMap<String, String> postDatas = new HashMap<>(2);

                postDatas.put("username", username);
                postDatas.put("password", pass);

                MyAsyncTask task = new MyAsyncTask(ApiManager.BASE_URL_CONNECT,postDatas);
                task.execute();

                task.setOnNewWsRequestListener(new onWsRequestListener() {
                    @Override
                    public void onCompleted(String json) {
                        Gson gson = new Gson();
                        res = gson.fromJson(json,Response.class);

                        Log.wtf("AccessToken:",res.getAccessToken());
                        accessToken = res.getAccessToken();
//                        sharedPref.edit().putString("accessToken",res.getAccessToken()).apply();
                        user.setToken(res.getAccessToken());
                        Toast.makeText(getApplicationContext(), res.getAccessToken(), Toast.LENGTH_LONG);
                        Intent intent = new Intent(LoginActivity.this,ChannelListFragmentActivity.class);
                        intent.putExtra("User",user);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });

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
