package com.orkunduzgun.mongodbandamazonconnection;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


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
                //Save changes and go back to profile class
                //finish();
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

    
}
