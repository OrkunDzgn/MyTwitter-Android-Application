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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class EditProfileActivity extends ActionBarActivity {

    ImageView iv;
    EditText picText;
    EditText descText;
    EditText usernameText;
    Button save;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle("Edit Profile");

        Intent i = getIntent();
        username = i.getStringExtra("username");
        String descriptionDB = i.getStringExtra("description");
        String pictureDB = i.getStringExtra("profilePicture");

        iv = (ImageView) findViewById(R.id.imageViewPP);
        usernameText = (EditText) findViewById(R.id.usernameText);
        picText = (EditText) findViewById(R.id.picEdit);
        descText = (EditText) findViewById(R.id.descEdit);
        save = (Button) findViewById(R.id.saveChangesButton);

        usernameText.setText(username, TextView.BufferType.EDITABLE);
        picText.setText(pictureDB, TextView.BufferType.EDITABLE);
        descText.setText(descriptionDB, TextView.BufferType.EDITABLE);
        Picasso.with(EditProfileActivity.this).load(pictureDB).fit().centerCrop().into(iv);

        usernameText.setEnabled(false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Update().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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

    public class Update extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... myStr) {

            String packet = "{\"Function\" : \"UpdateUser\"," +
                    "\"UserCreds\" : {\"username\": null, \"password\": null}," +
                    "\"User\" : {\"username\" : \"" + username + "\", \"description\" : \"" + descText.getText().toString() + "\", \"profilePicture\" : \"" + picText.getText().toString() + "\"}," +
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

                if(!error){
                    return true;
                }
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
        protected void onPostExecute(Boolean ok) {
            if(ok){
                Toast.makeText(EditProfileActivity.this, "Profile updated.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivityForResult(i, 1);
            }
            else {
                Toast.makeText(EditProfileActivity.this, "Problem has occured.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(ok);
        }
    }
}
