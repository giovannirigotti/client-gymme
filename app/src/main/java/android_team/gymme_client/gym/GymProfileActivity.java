package android_team.gymme_client.gym;

import androidx.appcompat.app.AppCompatActivity;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.trainer.TrainerProfileActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GymProfileActivity extends AppCompatActivity {

    private int user_id;

    TextView tv_gym_nome, tv_gym_cognome, tv_gym_nascita, tv_gym_email;
    String nome, cognome, nascita, email;

    Button btn_gym_email, btn_gym_password, btn_gym_logout, btn_gym_orari, btn_gym_dati_palestra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);

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

        tv_gym_nome = (TextView) findViewById(R.id.tv_gym_nome);
        tv_gym_cognome = (TextView) findViewById(R.id.tv_gym_cognome);
        tv_gym_nascita = (TextView) findViewById(R.id.tv_gym_nascita);
        tv_gym_email = (TextView) findViewById(R.id.tv_gym_email);

        btn_gym_email = (Button) findViewById(R.id.btn_gym_email);
        btn_gym_password = (Button) findViewById(R.id.btn_gym_password);
        btn_gym_orari = (Button) findViewById(R.id.btn_gym_orari);
        btn_gym_dati_palestra = (Button) findViewById(R.id.btn_gym_dati_palestra);
        btn_gym_logout = (Button) findViewById(R.id.btn_gym_logout);

        //Get User Data & Customer Data from DB
        GetDataSetView(user_id);
    }

    //USER DATA
    private void GetDataSetView(int user_id) {
        //User Data
        GymProfileActivity.GetUserDataConnection asyncTaskUser = (GymProfileActivity.GetUserDataConnection) new GymProfileActivity.GetUserDataConnection(new GymProfileActivity.GetUserDataConnection.AsyncResponse() {
            @Override
            public void processFinish(String _name, String _cognome, String _email, String _nascita) {

                if (_name.equals("error")) {
                    Toast.makeText(GymProfileActivity.this, "ERRORE CARICAMENTO DATI", Toast.LENGTH_SHORT).show();
                    Intent new_i = new Intent(GymProfileActivity.this, LoginActivity.class);
                    startActivity(new_i);
                    finish();
                } else if (!_name.equals("error")) {
                    nome = _name;
                    cognome = _cognome;
                    email = _email;
                    nascita = _nascita;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymProfileActivity.this, "Ben tornato", Toast.LENGTH_SHORT).show();
                            tv_gym_nome.setText(nome);
                            tv_gym_cognome.setText(cognome);
                            tv_gym_email.setText(email);
                            tv_gym_nascita.setText(nascita.split("T")[0]);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }


    private static class GetUserDataConnection extends AsyncTask<String, String, JsonObject> {

        public interface AsyncResponse {
            void processFinish(String _name, String _cognome, String _email, String _nascita);
        }

        public GymProfileActivity.GetUserDataConnection.AsyncResponse delegate = null;

        public GetUserDataConnection(GymProfileActivity.GetUserDataConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;

            try {
                url = new URL("http://10.0.2.2:4000/user/get_all_data/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.e("Server user response", responseString);
                    user = JsonParser.parseString(responseString).getAsJsonObject();
                    delegate.processFinish(user.get("name").getAsString(), user.get("lastname").getAsString(), user.get("email").getAsString(), user.get("birthdate").getAsString());

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish("error", "error", "error", "error");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return user;
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


}