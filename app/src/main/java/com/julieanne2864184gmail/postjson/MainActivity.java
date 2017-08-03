package com.julieanne2864184gmail.postjson;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private SharedPreferences sp_prefs;
    TextView username;
    TextView tvIsConnected;
    TextView tvJson;
    EditText etGlucose;
    Button btnPost;


    private static final String TAG = "";
    Child Kid;
    String KEY;
    String email;
    String Tage;
    int glucose;
    String firstname;
    String token;
    //connect to firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference PJDatabase = database.getReference("Child");
    FirebaseRails firebaserails;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    String json = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get reference to the views
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        etGlucose = (EditText) findViewById(R.id.etGlucose);
        username = (TextView) findViewById(R.id.username);
        btnPost = (Button) findViewById(R.id.btnPost);


        tvJson = (TextView) findViewById(R.id.tvJSON);
        // check if you are connected or not
        if (isConnected()) {
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        } else {
            tvIsConnected.setText("You are NOT conncted");
        }

        View.OnClickListener TheClick = new View.OnClickListener(){
            @Override
            public void onClick(View v){

              //  tvJson.setText(Kid.getName().toString());
            }
        };

        // add click listener to edit Age
        etGlucose.setOnClickListener(TheClick);


View.OnClickListener PostData = new View.OnClickListener(){
    @Override
    public void onClick(View v){

        Tage = etGlucose.getText().toString();
        //Toast.makeText(getApplicationContext(), "Postdata "+" "+Tage, Toast.LENGTH_LONG).show();
        firebaserails = new FirebaseRails();
        firebaserails.execute();
    }
};

        // add click listener to Button "POST"
        btnPost.setOnClickListener(PostData);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        token = FirebaseInstanceId.getInstance().getToken();

        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent);

        // get access to the shared preferences for this application
        sp_prefs = getSharedPreferences("com.julieanne2864184gmail.postjson_User_keys",
                Activity.MODE_PRIVATE);
        KEY = sp_prefs.getString("KEY", KEY );
        if(KEY == null){
            Intent intent1 = new Intent(this, Index_Page.class);
            startActivity(intent1);
        }
        else{
            KEY = sp_prefs.getString("KEY", KEY);
        }
        username.setText("Hi "+ sp_prefs.getString("Name",firstname));
    }

    //AsyncTask to facilite database update AND Rails Anaylysis

    class FirebaseRails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //
            EditText etName, etGlucose;
            Button btnPost;
            Child Kid;
            String newdata;
            String json = "";
            String token;
            int glucose;
            String name;
            String firstname;
            String Firebasename;
            String email;
            String Tage;
            String firebaseid;
            FirebaseDatabase database;
            DatabaseReference PJDatabase;

        }
        @Override
        protected String doInBackground(String... params) {
            glucose = Integer.parseInt(Tage);

            Kid = new Child();
            Kid.setID(sp_prefs.getString("KEY", KEY));
            Kid.setFirstname(sp_prefs.getString("Name", firstname));
            Kid.setAge(glucose);
            Kid.setEmail(sp_prefs.getString("Email", email));

            //Update firebase with the glucose reading
            PJDatabase.child(KEY).child("glucose").setValue(glucose);

            //create JSONobject for child to post to Rails!
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", Kid.getID());
                jsonObject.put("name", Kid.getName());
                jsonObject.put("glucose", Kid.getAge());
                jsonObject.put("token",token);
            } catch (JSONException e){
                e.printStackTrace();
            }
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            //check json value


            URL url;
            HttpURLConnection connection = null;
            try{
                //Create connection
                url = new URL("http://172.20.10.2:3000/children/android");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                // Send POST output.
                DataOutputStream printout = new DataOutputStream(connection.getOutputStream ());

                printout.writeBytes(json);
                printout.flush ();
                printout.close ();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                 return response.toString();

            }catch (Exception e) {
                e.printStackTrace();
                  return null;

            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

        }


        @Override
        protected void onPostExecute(String file_url) {

            //Toast.makeText(getApplicationContext(), "Completed "+json, Toast.LENGTH_LONG).show();

        }
    }


    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private boolean validate() {
       if (etGlucose.getText().toString().trim().equals(""))
            return false;

        else
            return true;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    protected void onDestroy() {
        SharedPreferences.Editor editor = sp_prefs.edit();
        editor.putString("KEY", KEY);
        editor.apply();
        super.onDestroy();
    }

}


