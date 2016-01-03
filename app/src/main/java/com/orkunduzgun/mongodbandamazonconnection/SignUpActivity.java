package com.orkunduzgun.mongodbandamazonconnection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SignUpActivity extends ActionBarActivity {

    EditText usernameEntered;
    EditText passwordEntered;
    EditText passwordReEntered;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        Button loginButton = (Button) findViewById(R.id.loginButton);
        usernameEntered = (EditText) findViewById(R.id.userText);
        passwordEntered = (EditText) findViewById(R.id.passText);
        passwordReEntered = (EditText) findViewById(R.id.passRE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = usernameEntered.getText().toString();
                String pass1 = passwordEntered.getText().toString();
                String pass2 = passwordReEntered.getText().toString();
                if (!uname.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()) {
                    if (pass1.equals(pass2)) {
                        String enteredTexts = usernameEntered.getText().toString() + ":" + passwordEntered.getText().toString();
                        new SignUp().execute(enteredTexts);
                    } else {
                        Toast.makeText(SignUpActivity.this, "Passwords you entered must be the same!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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


    public class SignUp extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... myStr) {
            String userEnteredCreds = myStr[0];
            String[] str = userEnteredCreds.split(":");
            String usernamePost = str[0];
            String passPost = str[1];

            String packet = "{\"Function\" : \"SignUp\"," +
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
                JSONObject userObj = nesne.getJSONObject("Error");
                boolean error = userObj.getBoolean("error");
                String errorDesc = userObj.getString("errorDescription");

                if(!error){
                    username = usernamePost;
                    return true;
                }

                //JSONObject nesne = new JSONObject(tumIcerik);
                //JSONObject userObj = nesne.getJSONObject("User");
                //String userid = userObj.getString("_id");
                //String userName = userObj.getString("username");
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


            return false;
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            if(ok){
                Toast.makeText(SignUpActivity.this, "SignUp Succesful", Toast.LENGTH_SHORT).show();

                SharedPreferences setting = getSharedPreferences("loginSuccess", MODE_PRIVATE);
                SharedPreferences.Editor mPrefsEditor = setting.edit();
                mPrefsEditor.putString("username", username);
                mPrefsEditor.commit();

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.putExtra("uname", usernameEntered.getText().toString());
                startActivity(intent);
            }
            else {
                Toast.makeText(SignUpActivity.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(ok);
        }
    }

}
