package android_team.gymme_client.customer;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.GymEditDataActivity;
import android_team.gymme_client.gym.GymProfileActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.nutritionist.NutritionistProfileActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;

public class CustomerNotificationActivity extends AppCompatActivity {

    static ListView notificationView;
    public static int user_id;
    static CustomNotificationAdapter adapter;
    public static ArrayList<String> listNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_notification);

        notificationView = (ListView) findViewById(R.id.lv_notification);
        listNotification = new ArrayList<String>();


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

        //Ricevo le notifiche dal server
        getNotification();

        //


    }


    private void getNotification() {
        CustomerNotificationActivity.ReciveNotification asyncTaskUser = (CustomerNotificationActivity.ReciveNotification) new CustomerNotificationActivity.ReciveNotification(new CustomerNotificationActivity.ReciveNotification.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<String> _texts) {
                Log.e("texts.get", _texts.get(0));
                if (_texts.get(0).equals("error")) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerNotificationActivity.this, "ERRORE CARICAMENTO NOTIFICA", Toast.LENGTH_SHORT).show();
                            Intent new_i = new Intent(CustomerNotificationActivity.this, LoginActivity.class);
                            startActivity(new_i);
                            finish();
                        }
                    });
                } else if (_texts.get(0).equals("empty")) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerNotificationActivity.this, "NESSUNA NUOVA NOTIFICA DA VISUALIZZARE", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (!_texts.get(0).equals("error")) {
                    //se la chiamata al server va a buon fine popolo listnotification con i valori ricevuti dal server
                    listNotification = _texts;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista delle notifiche da visualizzare nella recycler view(notificationView)
                            adapter = new CustomNotificationAdapter(CustomerNotificationActivity.this, listNotification);
                            notificationView.setAdapter(adapter);


                            notificationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //cancello notifica su db
                                    Toast.makeText(CustomerNotificationActivity.this, "Elemento: " + i + "; testo: " + listNotification.get(i), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                    Log.e("listNotification", listNotification.toString());
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerNotificationActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }


    public static void deleteNotification(int position, Activity context) {
        deleteNotificationOnDb(listNotification.get(position).split(",")[0]);
        listNotification.remove(position);
        adapter = new CustomNotificationAdapter(context, listNotification);
        notificationView.setAdapter(adapter);
        //Toast.makeText(CustomerNotificationActivity.this, "Elemento: " + i +"; testo: " + listNotification.get(i), Toast.LENGTH_SHORT).show();
    }

    private static class ReciveNotification extends AsyncTask<String, String, JsonArray> {
        public interface AsyncResponse {
            void processFinish(ArrayList<String> _texts);
        }

        public CustomerNotificationActivity.ReciveNotification.AsyncResponse delegate = null;

        public ReciveNotification(CustomerNotificationActivity.ReciveNotification.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray notification = null;
            ArrayList<String> testi = new ArrayList<String>();
            ArrayList<String> errorList = new ArrayList<>();
            ArrayList<String> emptyList = new ArrayList<>();

            errorList.add("error");
            emptyList.add("empty");

            try {
                url = new URL("http://10.0.2.2:4000/get_notifications/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.e("Server customer", responseString);
                    notification = JsonParser.parseString(responseString).getAsJsonArray();
                    Log.e("ARRAY", notification.toString());
                    for (int i = 0; i < notification.size(); i++) {
                        JsonObject notifica = (JsonObject) notification.get(i);
                        String readtext = notifica.get("text").getAsString();
                        String readid = notifica.get("notification_id").getAsString();
                        testi.add(readid + "," + readtext);

                    }

                    delegate.processFinish(testi);
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish(emptyList);
                } else {
                    Log.e("Server error", "errore :(");
                    delegate.processFinish(errorList);
                }
            } catch (IOException e) {
                e.printStackTrace();
                delegate.processFinish(errorList);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return notification;
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

    public static void deleteNotificationOnDb(String notification_id) {
        CustomerNotificationActivity.DeleteNotificationConnection asyncTask = (CustomerNotificationActivity.DeleteNotificationConnection) new CustomerNotificationActivity.DeleteNotificationConnection(new CustomerNotificationActivity.DeleteNotificationConnection.AsyncResponse() {

            @Override
            public void processFinish(Integer output) {
                if (output == 200) {
                    Log.e("NOTIFICA", "OK");
                } else {
                    Log.e("NOTIFICA", "Server error");
                }
            }

        }).execute(notification_id);

    }

    public static class DeleteNotificationConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public CustomerNotificationActivity.DeleteNotificationConnection.AsyncResponse delegate = null;

        public DeleteNotificationConnection(CustomerNotificationActivity.DeleteNotificationConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/update_a_notification/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("NOTIFICATION", "cancellata da DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("NOTIFICATION", "Error");
                    responseCode = 500;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 69;
                delegate.processFinish(responseCode);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }
    }

}