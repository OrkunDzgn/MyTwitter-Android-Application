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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FeedActivity extends ActionBarActivity {

    TextView tvGelen;
    List<String> userTweets;
    ImageView iv;
    final List<TweetDatas> tweetler =new ArrayList<TweetDatas>();
    ListView listemiz;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setTitle("Your Feed");

        /*
        new GetTweets().execute("http://52.34.254.140:80/api/Packet");

        listemiz = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(FeedActivity.this, tweetler);
        listemiz.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int secilen = item.getItemId();

        if(secilen == R.id.newtweet){
            Intent i = new Intent(FeedActivity.this, NewTweet.class);
            startActivity(i);
        }else if(secilen == R.id.profile){
            Intent i = new Intent(FeedActivity.this, ProfileActivity.class);
            startActivity(i);
        }else if(secilen == R.id.search){
            //Intent i = new Intent(FeedActivity.this, SearchActivity.class);
            //startActivity(i);
        }
        if (secilen == R.id.action_logout) {
            //Delete username from shared prefs
            SharedPreferences setting = getSharedPreferences("loginSuccess", MODE_PRIVATE);
            SharedPreferences.Editor mPrefsEditor = setting.edit();
            mPrefsEditor.putString("username", null);
            mPrefsEditor.commit();

            Intent i = new Intent(FeedActivity.this, LoginActivity.class);
            finish();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }



    public class GetTweets extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... myURL) {
            String myStr = "{\"Function\": \"GetUser\", \"_id\" : 5, \"username\": \"hey\"}";
            byte[] bayt = myStr.getBytes();
            try {
                URL adres = new URL(myURL[0]);
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                HttpURLConnection baglanti = (HttpURLConnection) adres.openConnection();
                YardimciMetodlar.httpPost(baglanti, bayt);
                String tumIcerik = YardimciMetodlar.responseTumString(baglanti);

                JSONObject nesne = new JSONObject(tumIcerik);
                String tweetsArr = nesne.getString("tweets");
                String username = nesne.getString("username");
                String profilePicture = nesne.getString("profilePicture");

                JSONArray tweetsJArr = new JSONArray(tweetsArr);
                ArrayList<String> times = new ArrayList<>();
                ArrayList<String> imgLinks = new ArrayList<>();
                ArrayList<String> tweets = new ArrayList<>();

                for(int i = 0; i < tweetsJArr.length(); i++){
                    JSONObject tweetJObj = tweetsJArr.getJSONObject(i);
                    tweets.add(tweetJObj.getString("tweet"));
                    times.add(tweetJObj.getString("timePosted"));
                }
                int i = tweetsJArr.length();
                i--;
                while(i != -1){
                    tweetler.add(new TweetDatas(username, tweets.get(i), imgLinks.get(i), times.get(i).toString()));
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


