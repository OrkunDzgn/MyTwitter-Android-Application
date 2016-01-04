package com.orkunduzgun.mongodbandamazonconnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends ActionBarActivity {

    EditText usernameEntered;
    EditText passwordEntered;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");


        SharedPreferences setting = getSharedPreferences("loginSuccess", Context.MODE_PRIVATE);
        username = setting.getString("username", null); //token'ı sharedden aldık
        if(username != null){
            Intent i2 = new Intent(LoginActivity.this, FeedActivity.class);
            finish(); //Kill this activity to not to let user go back again
            startActivity(i2);
        }

        Intent i = new Intent();
        String uname = "";
        uname = i.getStringExtra("uname");


        Button loginButton = (Button) findViewById(R.id.loginButton);
        usernameEntered = (EditText) findViewById(R.id.userText);
        passwordEntered = (EditText) findViewById(R.id.passText);

        usernameEntered.setText(uname);

        TextView signup = (TextView) findViewById(R.id.signupText);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = usernameEntered.getText().toString();
                String upass = passwordEntered.getText().toString();
                if(!uname.isEmpty() && !upass.isEmpty()) {
                    String enteredTexts = usernameEntered.getText().toString() + ":" + passwordEntered.getText().toString();
                    new Login().execute(enteredTexts);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Enter valid username or password", Toast.LENGTH_SHORT).show();
                }
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


    public class Login extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... myStr) {
            String userEnteredCreds = myStr[0];
            String[] str = userEnteredCreds.split(":");
            String usernamePost = str[0];
            String passPost = str[1];

            String packet = "{\"Function\" : \"Login\"," +
                            "\"UserCreds\" : {\"username\":\""+ usernamePost +"\",\"password\": \""+ passPost +"\"}," +
                            "\"User\" : {\"_id\" : null, \"username\":\"orkundzgn\",\"description\": null, \"profilePicture\" : null}," +
                            "\"Tweets\" : null," +
                            "\"Error\" : null," +
                            "\"Following\" : null," +
                            "\"Followers\" : null," +
                            "\"Tweet\" : null}";
            byte[] bayt = packet.getBytes();
            try {
                URL adres = new URL("http://52.34.254.140/api/Packet/");
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                HttpURLConnection baglanti = (HttpURLConnection) adres.openConnection();
                YardimciMetodlar.httpPost(baglanti, bayt);
                String tumIcerik = YardimciMetodlar.responseTumString(baglanti);

                JSONObject nesne = new JSONObject(tumIcerik);
                JSONObject userObj = nesne.getJSONObject("User");
                String userid = userObj.getString("_id");
                String userName = userObj.getString("username");
                username = userName;

                if(!userName.isEmpty()){
                    return userName;
                }

                //String tweetsArr = nesne.getString("tweets");
                //String username = nesne.getString("User");
                //String profilePicture = nesne.getString("profilePicture");
/*
                JSONArray tweetsJArr = new JSONArray(tweetsArr);
                ArrayList<String> times = new ArrayList<>();
                ArrayList<String> tweets = new ArrayList<>();

                for(int i = 0; i < tweetsJArr.length(); i++){
                    JSONObject tweetJObj = tweetsJArr.getJSONObject(i);
                    tweets.add(tweetJObj.getString("tweet"));
                    times.add(tweetJObj.getString("timePosted"));
                }
                int i = tweetsJArr.length();
                i--;
                while(i != -1){
                    String[] separated = times.get(i).split(":");
                    tweetler.add(new TweetDatas(username, tweets.get(i), profilePicture, separated[0] + ":" +  separated[1]));
                    i--;
                }
                */
                //return tumIcerik;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String tumIcerik) {
            if(tumIcerik == null){
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_SHORT).show();

                SharedPreferences setting = getSharedPreferences("loginSuccess", MODE_PRIVATE);
                SharedPreferences.Editor mPrefsEditor = setting.edit();
                mPrefsEditor.putString("username", username);
                mPrefsEditor.commit();


                Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                finish();
                startActivity(intent);
            }
            super.onPostExecute(tumIcerik);
        }
    }
}
