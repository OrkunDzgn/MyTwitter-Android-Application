package com.orkunduzgun.mongodbandamazonconnection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

    String username = "";
    ImageView iv;
    final List<TweetDatas> tweetler =new ArrayList<TweetDatas>();
    ListView listemiz;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Profile isim gelecek");

        SharedPreferences setting = getSharedPreferences("loginSuccess", MODE_PRIVATE);
        username = setting.getString("username", null); //username'i sharedden aldık

        iv = (ImageView) findViewById(R.id.imageView);
        new GetTweets().execute(username);

        listemiz = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(ProfileActivity.this, tweetler);
        listemiz.setAdapter(adapter);

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
        protected String doInBackground(String... myURL) {
            String packet = "{\"Function\" : \"Login\"," +
                    "\"UserCreds\" : {\"username\":\"\",\"password\": \"\"}," +
                    "\"User\" : {\"_id\" : null, \"username\":\""+ username +"\",\"description\": null, \"profilePicture\" : null}," +
                    "\"Tweets\" : null," +
                    "\"Error\" : null," +
                    "\"Following\" : null," +
                    "\"Followers\" : null," +
                    "\"Tweet\" : null}";
            byte[] bayt = packet.getBytes();
            try {
                URL adres = new URL(myURL[0]);
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                HttpURLConnection baglanti = (HttpURLConnection) adres.openConnection();
                YardimciMetodlar.httpPost(baglanti, bayt);
                String tumIcerik = YardimciMetodlar.responseTumString(baglanti);

                JSONObject nesne = new JSONObject(tumIcerik);
                JSONObject nesneTweets = new JSONObject(tumIcerik);
                String tweetsArr = nesne.getString("tweets");
                String username = nesne.getString("username");
                String profilePicture = nesne.getString("profilePicture");

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
                    tweetler.add(new TweetDatas(username, tweets.get(i), times.get(i)));
                    i--;
                }
                return tumIcerik;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }  catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String tumIcerik) {
            //adapter.notifyDataSetChanged();
            super.onPostExecute(tumIcerik);
        }
    }
}
