package com.julieanne2864184gmail.postjson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;




/**
 * Created by julieanneglennon on 3/15/17.
 */

public class GoRegister extends AppCompatActivity {

    // private fields of the class
    private SharedPreferences sp_prefs;

    Child Kid;
    String KEY;
    String firstname;
    String lastname;
    String DateoB;
    String email;
    String token;
    String json;

    EditText edfName;
    EditText edlName;
    EditText DoB;
    EditText edGender;
    EditText edEmailAddress;
    EditText edUserName;
    EditText edPassword;
    EditText PasswordCK;
    Button Register;
    private volatile Boolean hasKey = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference PJDatabase = database.getReference("Child");
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // get reference to the views
        edfName = (EditText) findViewById(R.id.edfName);
        edlName = (EditText) findViewById(R.id.edlName);
        DoB = (EditText) findViewById(R.id.DoB);
        edGender = (EditText) findViewById(R.id.edGender);
        edEmailAddress = (EditText) findViewById(R.id.edEmailAddress);
        edUserName = (EditText) findViewById(R.id.edUserName);
        edPassword = (EditText) findViewById(R.id.edPassword);
        PasswordCK = (EditText) findViewById(R.id.PasswordCK);
        Register = (Button) findViewById(R.id.Register);

        View.OnClickListener TheClick = new View.OnClickListener(){
            @Override
            public void onClick(View v){


            }
        };

        // add click listener to edit Age
        edfName.setOnClickListener(TheClick);



        View.OnClickListener PostData = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                firstname = edfName.getText().toString().trim();
                lastname = edlName.getText().toString().trim();
                DateoB = DoB.getText().toString().trim();
                email = edEmailAddress.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "Postdata "+firstname+" "+lastname+" "+DateoB, Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                firebase = new Firebase();
                firebase.execute();


            }
        };

        // add click listener to Button "POST"
        Register.setOnClickListener(PostData);

        token = FirebaseInstanceId.getInstance().getToken();



    }
    class Firebase extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //
            EditText etName, etAge;
            Button Register;
            Child Kid;
            String KEY;
            String json = "";
            String token;
            String firstname;
            String lastname;
            String email;
            String DateoB;
            FirebaseDatabase database;
            DatabaseReference PJDatabase;

        }

        @Override
        protected String doInBackground(String... params) {
            //connect to firebase
            DateoB = DateoB.replaceAll( "[^\\d]", "" );
            int dob = Integer.parseInt(DateoB);

            Kid = new Child();
            Kid.setFirstname(firstname);
            Kid.setAge(dob);
            Kid.setEmail(email);

            //now have firebase send us the KEY it created to implement security later and have access to checks
            KEY = PJDatabase.push().getKey();
            PJDatabase.child(KEY).child("name").setValue(firstname);
            hasKey = true;
            PJDatabase.child(KEY).child("surname").setValue(lastname);
            PJDatabase.child(KEY).child("Birthday").setValue(dob);
            PJDatabase.child(KEY).child("Email").setValue(email);

            return null;
        }


        //Once we have the Firebase KEY have info to shared preferences
        @Override
        protected void onPostExecute(String file_url) {

            while(hasKey == true){

                sp_prefs = getSharedPreferences("com.julieanne2864184gmail.postjson_User_keys", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp_prefs.edit();
                editor.putString("KEY", KEY);
                editor.putString("Name", firstname);
                editor.putString("Email", email);
                editor.apply();

                hasKey = false;

                Toast.makeText(getApplicationContext(), "Completed " + json, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GoRegister.this, MainActivity.class);
                // Bundle bundle = new Bundle();
                // bundle.putString("key", newdata);
                // intent.putExtras(bundle);
                startActivity(intent);

            }
        }
    }
    protected void onDestroy() {
        if(KEY == null) {
            SharedPreferences.Editor editor = sp_prefs.edit();
            editor.putString("KEY", KEY);
            editor.putString("Name", firstname);
            editor.putString("Email", email);
            editor.apply();
            super.onDestroy();
        }
    }
}
