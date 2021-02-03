package android_team.gymme_client.customer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerHomeActivity extends AppCompatActivity {

    private int user_id;


    @BindView(R.id.send_notification_button)
    Button _send_notification_button;
    @BindView(R.id.control_notification_button)
    Button _control_notification_button;

    @BindView(R.id.btn_customer_home_profile)
    Button _btn_customer_home_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        ButterKnife.bind(this);
        setTitle("HOME CUSTOMER");
        
        Intent i = getIntent();
        if (!i.hasExtra("user_id")) {
            Toast.makeText(this, "User_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            user_id = i.getIntExtra("user_id", -1);
            Log.w("user_id ricevuto:", String.valueOf(user_id));
            if (user_id == -1) {
                Toast.makeText(this, "Utente non creato.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }
        _send_notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    send_notification();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        _control_notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.e("provaa", "prova");
                    recive_notification();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        _btn_customer_home_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Customer Profile Activity");
                Intent i = new Intent(getApplicationContext(), CustomerProfileActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

    }


    private void send_notification() throws IOException {

        //Da cambiare
        String notification_type = "1";
        String text = "notifica";
        String user_id = "1";
        //
        new SendNotification().execute(notification_type, text, user_id);
    }


    private void recive_notification() throws IOException {
        new ReciveNotification().execute();
    }

    private class SendNotification extends AsyncTask<String, String, JsonObject> {

        String toastMessage = null;


        @Override
        protected JsonObject doInBackground(String... params) {

            Integer params0 = Integer.parseInt(params[0]);
            Integer params2 = Integer.parseInt(params[2]);


            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject notification = null;

            try {
                url = new URL("http://10.0.2.2:4000/insert_notifications/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();


                paramsJson.addProperty("notification_type", params0);
                paramsJson.addProperty("text", params[1]);
                paramsJson.addProperty("user_id", params2);

                Log.e("json", paramsJson.toString());

                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(paramsJson.toString());
                writer.flush();
                writer.close();
                os.close();


                urlConnection.connect();
                Log.e("connection", "connected");

                int responseCode = urlConnection.getResponseCode();
                Log.e("response code", Integer.toString(responseCode));

                String responseMessage = urlConnection.getResponseMessage();

                urlConnection.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
                toastMessage = "Impossibile inviare la notifica!";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return notification;
        }

    }

    private class ReciveNotification extends AsyncTask<String, String, JsonArray> {

        String toastMessage = null;

        @Override
        protected JsonArray doInBackground(String... params) {


            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray notification = null;

            try {
                url = new URL("http://10.0.2.2:4000/get_notifications/1");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();


                if (responseCode == HttpURLConnection.HTTP_OK) {

                    String responseString = readStream(urlConnection.getInputStream());
                    Log.v("Server response", "User found: " + responseString);
                    notification = JsonParser.parseString(responseString).getAsJsonArray();

                /*    int id = notification.get("notification_id").getAsInt();
                    int type = notification.get("notification_type").getAsInt();
                    String text = notification.get("text").getAsString();
                    int userId = notification.get("notification_type").getAsInt();
                */
                    Log.e("gesu", String.valueOf(notification));
                }


            } catch (IOException e) {
                e.printStackTrace();
                toastMessage = "Impossibile ricevere notifiche!";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return notification;
        }

    }


    private String readStream(InputStream in) throws UnsupportedEncodingException {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
