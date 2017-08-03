package com.julieanne2864184gmail.postjson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by julieanneglennon on 3/15/17.
 */

public class Index_Page extends AppCompatActivity {
    private volatile Boolean hasKey = false;
    private SharedPreferences sp_prefs;
    private static final String TAG = "";
    EditText editName, editText;
    Button SignIn;
    String name;
    String KEY;
    String key = null;
    String email;
    StoreKey storeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        SignIn = (Button) findViewById(R.id.SignIn);
        editText = (EditText) findViewById(R.id.editText);
        editName = (EditText) findViewById(R.id.editName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View.OnClickListener GetKey = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText.getText().toString().trim();
                name = editName.getText().toString().trim();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference PJDatabase = database.getReference("Child");

                PJDatabase.orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            key = child.getKey();
                            KEY = key;
                            hasKey = true;
                            storeKey = new StoreKey();
                            storeKey.execute();
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }


        };


        SignIn.setOnClickListener(GetKey);


    }

    class StoreKey extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //
            EditText editText;
            Button SignIn;
            String name;
            String KEY;
            String key;
            String email;
            FirebaseDatabase database;
            DatabaseReference PJDatabase;

        }

        @Override
        protected String doInBackground(String... params) {
            while (hasKey != true) {
                    try{
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

            }
            if(KEY != null) {
                sp_prefs = getSharedPreferences("com.julieanne2864184gmail.postjson_User_keys", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp_prefs.edit();
                editor.putString("KEY", KEY);
                editor.putString("Name", name);
                editor.putString("Email", email);
                editor.apply();
            }
            return KEY;
        }

        @Override
        protected void onPostExecute(String KEY) {

            Intent intent = new Intent(Index_Page.this, MainActivity.class);
            startActivity(intent);

        }

    }



    public void GoRegister(View view){
        startActivity(new Intent(Index_Page.this, GoRegister.class));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    protected void onDestroy() {
        sp_prefs = getSharedPreferences("com.julieanne2864184gmail.postjson_User_keys", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_prefs.edit();
        editor.putString("KEY", KEY);
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.apply();
        super.onDestroy();
    }

}
