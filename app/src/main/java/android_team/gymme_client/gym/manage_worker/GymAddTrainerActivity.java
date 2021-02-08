package android_team.gymme_client.gym.manage_worker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.trainer.TrainerObject;

public class GymAddTrainerActivity extends AppCompatActivity {

    private int user_id;
    public static ArrayList<TrainerObject> trainers_list;
    static CustomGymTrainerAdapter trainer_adapter;
    static ListView lv_trainer;
    EditText inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_add_trainer);

        trainers_list = new ArrayList<TrainerObject>();

        //region CHECK INTENT EXTRAS
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
        //endregion

        lv_trainer = (ListView) findViewById(R.id.lv_free_trainers);
        inputSearch = (EditText) findViewById(R.id.et_search_trainer);

        getTrainers();

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                GymAddTrainerActivity.this.trainer_adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }
    public static ArrayList<TrainerObject> getAllTrainers(){
        return trainers_list;
    };

    private void getTrainers() {
        GymAddTrainerActivity.ReceiveTrainersConn asyncTaskUser = (GymAddTrainerActivity.ReceiveTrainersConn) new GymAddTrainerActivity.ReceiveTrainersConn(new GymAddTrainerActivity.ReceiveTrainersConn.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<TrainerObject> trainers) {
                trainers_list = trainers;
                if (trainers_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            trainer_adapter = new CustomGymTrainerAdapter(GymAddTrainerActivity.this, trainers_list);
                            lv_trainer.setAdapter(trainer_adapter);

                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymAddTrainerActivity.this, "Nessun personal trainer disponibile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }

    private static class ReceiveTrainersConn extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<TrainerObject> trainers);
        }

        public GymAddTrainerActivity.ReceiveTrainersConn.AsyncResponse delegate = null;

        public ReceiveTrainersConn(GymAddTrainerActivity.ReceiveTrainersConn.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray _trainers = null;
            ArrayList<TrainerObject> t_objects = new ArrayList<TrainerObject>();

            try {
                url = new URL("http://10.0.2.2:4000/gym/get_new_trainers/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    _trainers = JsonParser.parseString(responseString).getAsJsonArray();

                    for (int i = 0; i < _trainers.size(); i++) {
                        JsonObject trainer = (JsonObject) _trainers.get(i);

                        String user_id = trainer.get("user_id").getAsString().trim();
                        String name = trainer.get("name").getAsString().trim();
                        String lastname = trainer.get("lastname").getAsString().trim();
                        String email = trainer.get("email").getAsString().trim();
                        String qualification = trainer.get("qualification").getAsString().trim();
                        String fiscal_code = trainer.get("fiscal_code").getAsString().trim();

                        TrainerObject t_obj = new TrainerObject(user_id, name, lastname, email, qualification, fiscal_code);
                        t_objects.add(t_obj);

                    }
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(t_objects);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("GET TRAINER", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<TrainerObject>());
                } else {
                    Log.e("GET TRAINER", "SERVER ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("GET TRAINER", "I/O EXCEPTION ERROR");
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return _trainers;
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


    public static void redirectManage(Activity context) {
        Log.e("REDIRECT", "Gym Menege Worker");
        Intent i = new Intent(context, GymMenageWorkerActivity.class);
        i.putExtra("user_id", Integer.valueOf(GymMenageWorkerActivity.getGymId()));
        context.startActivity(i);
        context.finish();
    }


}