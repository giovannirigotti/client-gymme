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
import android_team.gymme_client.nutritionist.NutritionistObject;

public class GymAddNutritionistActivity extends AppCompatActivity {

    private int user_id;
    static CustomGymNutritionistAdapter nutritionist_adapter;
    EditText inputSearch;
    ListView lv_nutri;
    public static ArrayList<NutritionistObject> nutritionists_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_add_nutritionist);
        nutritionists_list = new ArrayList<NutritionistObject>();

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

        lv_nutri = (ListView) findViewById(R.id.lv_free_nutritionists);
        inputSearch = (EditText) findViewById(R.id.et_search_nutritionist);  //prendo logica funzionamento da GymAddTrainerActivity

        getNutritionists();

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                GymAddNutritionistActivity.this.nutritionist_adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }


    private void getNutritionists() {
        GymAddNutritionistActivity.ReceiveNutritionistsConn asyncTaskUser = (GymAddNutritionistActivity.ReceiveNutritionistsConn) new GymAddNutritionistActivity.ReceiveNutritionistsConn(new GymAddNutritionistActivity.ReceiveNutritionistsConn.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<NutritionistObject> nutritionists) {

                nutritionists_list = nutritionists;
                if (nutritionists_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei nutritionist da visualizzare nella recycler view(notificationView)
                            nutritionist_adapter = new CustomGymNutritionistAdapter(GymAddNutritionistActivity.this, nutritionists_list);
                            lv_nutri.setAdapter(nutritionist_adapter);
                            /*
                            notificationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //cancello notifica su db
                                    Toast.makeText(GymMenageWorkerActivity.this, "Elemento: " + i + "; testo: " + nutritionists_list.get(i), Toast.LENGTH_SHORT).show();
                                }
                            });
                             */
                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymAddNutritionistActivity.this, "Nessun nutrizionista come dipendente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //for (int j = 0; j < nutritionists_list.size(); j++) {
                //    Log.e("nutritionist n:" + j, nutritionists_list.get(j).toString());
                //}
            }
        }).execute(String.valueOf(user_id));
    }
    public static ArrayList<NutritionistObject> getAllNutritionist(){
        return nutritionists_list;
    };
    private static class ReceiveNutritionistsConn extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<NutritionistObject> nutritionists);
        }

        public GymAddNutritionistActivity.ReceiveNutritionistsConn.AsyncResponse delegate = null;

        public ReceiveNutritionistsConn(GymAddNutritionistActivity.ReceiveNutritionistsConn.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray _nutritionists = null;
            ArrayList<NutritionistObject> t_objects = new ArrayList<NutritionistObject>();

            try {
                url = new URL("http://10.0.2.2:4000/gym/get_new_nutritionists/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    _nutritionists = JsonParser.parseString(responseString).getAsJsonArray();

                    for (int i = 0; i < _nutritionists.size(); i++) {
                        JsonObject nutritionist = (JsonObject) _nutritionists.get(i);

                        String user_id = nutritionist.get("user_id").getAsString().trim();
                        String name = nutritionist.get("name").getAsString().trim();
                        String lastname = nutritionist.get("lastname").getAsString().trim();
                        String email = nutritionist.get("email").getAsString().trim();
                        String qualification = nutritionist.get("qualification").getAsString().trim();
                        String fiscal_code = nutritionist.get("fiscal_code").getAsString().trim();

                        NutritionistObject t_obj = new NutritionistObject(user_id, name, lastname, email, qualification, fiscal_code);
                        t_objects.add(t_obj);

                    }
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(t_objects);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("GET TRAINER", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<NutritionistObject>());
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
            return _nutritionists;
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
    }

}