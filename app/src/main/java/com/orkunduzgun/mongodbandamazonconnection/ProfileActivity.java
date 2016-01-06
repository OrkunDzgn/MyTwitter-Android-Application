package com.orkunduzgun.mongodbandamazonconnection;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends ActionBarActivity {

    String userNameDB = "";
    String userDescDB = "";
    String userDateJoinedDB = "";
    String userProfilePictureDB = "";

    String username = "";
    ImageView iv;
    TextView usernameText;
    TextView descriptionText;
    TextView dateJoinedText;

    final List<TweetDatas> tweetler =new ArrayList<TweetDatas>();
    ListView listemiz;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences setting = getSharedPreferences("loginSuccess", MODE_PRIVATE);
        username = setting.getString("username", null); //username'i sharedden aldık

        setTitle(username + "'s Profile");

        Button editProfileButton = (Button) findViewById(R.id.editProfile);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                i.putExtra("username", userNameDB);
                i.putExtra("profilePicture", userProfilePictureDB);
                i.putExtra("description", userDescDB);
                startActivity(i);
            }
        });

        iv = (ImageView) findViewById(R.id.imageViewProf);
        usernameText = (TextView) findViewById(R.id.usernameText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        dateJoinedText = (TextView) findViewById(R.id.dateJoinedText);

        new GetUser().execute(username);
        new GetTweets().execute(username);

        listemiz = (ListView) findViewById(R.id.listView2);
        adapter = new CustomAdapter(ProfileActivity.this, tweetler);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    public class GetTweets extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... myStr) {
            String userShared = myStr[0];

            String packet = "{\"Function\" : \"GetTweets\"," +
                    "\"UserCreds\" : {\"username\":\"\",\"password\": \"\"}," +
                    "\"User\" : {\"_id\" : null, \"username\":\"\",\"description\": null, \"profilePicture\" : null}," +
                    "\"Tweets\" : null," +
                    "\"Error\" : null," +
                    "\"Following\" : null," +
                    "\"Followers\" : null," +
                    "\"Tweet\" : {\"username\" : \"" + userShared + "\"}}";
            byte[] bayt = packet.getBytes();
            try {
                URL adres = new URL("http://52.34.254.140/api/Packet/");
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                HttpURLConnection baglanti = (HttpURLConnection) adres.openConnection();
                YardimciMetodlar.httpPost(baglanti, bayt);
                String tumIcerik = YardimciMetodlar.responseTumString(baglanti);

                JSONObject nesneTum = new JSONObject(tumIcerik);
                JSONObject userObj = nesneTum.getJSONObject("Tweets");
                JSONArray liste = userObj.getJSONArray("tweets");

                ArrayList<String> users = new ArrayList<>();
                ArrayList<String> tweets = new ArrayList<>();
                ArrayList<String> imgLinks = new ArrayList<>();
                ArrayList<String> times = new ArrayList<>();

                for(int i = 0; i < liste.length(); i++){
                    JSONObject nesne = liste.getJSONObject(i);
                    users.add( nesne.getString("username") );
                    tweets.add(nesne.getString("tweet"));
                    imgLinks.add(nesne.getString("profilePicture"));
                    times.add(nesne.getString("dateTimePosted"));
                }
                int i = liste.length();
                i--;
                while(i != -1){
                    tweetler.add(new TweetDatas(users.get(i), tweets.get(i), imgLinks.get(i), times.get(i)));
                    i--;
                }
                return "basd";

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
            if(listemiz != null) {
                listemiz.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            super.onPostExecute(tumIcerik);
        }
    }


    public class GetUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... myStr) {
            String userShared = myStr[0];

            String packet = "{\"Function\" : \"GetUser\"," +
                    "\"UserCreds\" : {\"username\":\"\",\"password\": \"\"}," +
                    "\"User\" : {\"_id\" : null, \"username\":\"" + userShared + "\",\"description\": null, \"profilePicture\" : null}," +
                    "\"Tweets\" : null," +
                    "\"Error\" : null," +
                    "\"Following\" : null," +
                    "\"Followers\" : null," +
                    "\"Tweet\" : {\"username\" : \"\"}}";
            byte[] bayt = packet.getBytes();
            try {
                URL adres = new URL("http://52.34.254.140/api/Packet/");
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                HttpURLConnection baglanti = (HttpURLConnection) adres.openConnection();
                YardimciMetodlar.httpPost(baglanti, bayt);
                String tumIcerik = YardimciMetodlar.responseTumString(baglanti);



                return tumIcerik;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String tumIcerik) {

            JSONObject nesneTum = null;

            try {
                nesneTum = new JSONObject(tumIcerik);
                JSONObject userObj = nesneTum.getJSONObject("User");
                userNameDB = userObj.getString("username");
                userDescDB = userObj.getString("description");
                userDateJoinedDB = userObj.getString("dateJoined");
                userProfilePictureDB = userObj.getString("profilePicture");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            usernameText.setText(userNameDB);
            descriptionText.setText(userDescDB);
            dateJoinedText.setText(userDateJoinedDB);
            Picasso.with(ProfileActivity.this).load(userProfilePictureDB).into(iv);

            super.onPostExecute(tumIcerik);
        }
    }
}
