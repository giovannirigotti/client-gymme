
package android_team.gymme_client.gym.menage_worker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import android_team.gymme_client.gym.DrawerGymListener;
import android_team.gymme_client.gym.GymHomeActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.nutritionist.NutritionistObject;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.TrainerObject;

public class GymMenageWorkerActivity extends AppCompatActivity {

    static Activity myContext;

    private static int user_id;

    DrawerGymListener drawerGymListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    static CustomGymTrainerAssumedAdapter trainer_adapter;
    static CustomGymNutritionistAssumedAdapter nutritionist_adapter;

    static ListView lv_trainer, lv_nutri;
    Button btn_add_trainer, btn_add_nutri;

    public static ArrayList<TrainerObject> trainers_list;
    public static ArrayList<NutritionistObject> nutritionists_list;

    //RunOnUi in static context
    public static Handler UIHandler;

    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_menage_worker);

        trainers_list = new ArrayList<TrainerObject>();
        nutritionists_list = new ArrayList<NutritionistObject>();
        myContext = this.getParent();

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

        lv_trainer = (ListView) findViewById(R.id.lv_menage_trainer);
        lv_nutri = (ListView) findViewById(R.id.lv_menage_nutritionist);
        btn_add_trainer = (Button) findViewById(R.id.btn_menage_trainer);
        btn_add_nutri = (Button) findViewById(R.id.btn_menage_nutritionist);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_gym_activity);
        drawerGymListener = new DrawerGymListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("DIPENDENTI");

        getWorkerData();

        btn_add_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Add Trainer");
                Intent i = new Intent(getApplicationContext(), GymAddTrainerActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });


        btn_add_nutri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Add Nutri");
                Intent i = new Intent(getApplicationContext(), GymAddNutritionistActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.e("REDIRECT", "Gym Home Activity");
        Intent i = new Intent(getApplicationContext(), GymHomeActivity.class);
        i.putExtra("user_id", user_id);
        startActivity(i);
    }

    //region DRAWER
    @Override
    protected void onPause() {
        super.onPause();
        Drawer.closeDrawer(drawerLayout);
    }
    public void ClickMenu(View view) {
        Drawer.openDrawer(drawerLayout);
    }

    public void ClickDrawer(View view) {
        Drawer.closeDrawer(drawerLayout);
    }

    public void gymToCorsi(View view){
        drawerGymListener.toCourse();
    }
    public void gymToClienti(View view){
        drawerGymListener.toCustomer();
    }
    public void gymToDipendenti(View view){
        drawerGymListener.toEmployees();
    }
    public void gymToProfilo(View view){
        drawerGymListener.toProfile();
    }
    public void gymToHome(View view){
        drawerGymListener.toHome();
    }
    //endregion

    //region GET DATA REGION
    private void getWorkerData() {
        getTrainers();
        getNutritionists();
    }

    public static String getGymId() {
        return String.valueOf(user_id);
    }

    private void getTrainers() {
        GymMenageWorkerActivity.ReceiveTrainersConn asyncTaskUser = (GymMenageWorkerActivity.ReceiveTrainersConn) new GymMenageWorkerActivity.ReceiveTrainersConn(new GymMenageWorkerActivity.ReceiveTrainersConn.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<TrainerObject> trainers) {

                trainers_list = trainers;
                if (trainers_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            trainer_adapter = new CustomGymTrainerAssumedAdapter(GymMenageWorkerActivity.this, trainers_list);
                            lv_trainer.setAdapter(trainer_adapter);
                            /*
                            notificationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //cancello notifica su db
                                    Toast.makeText(GymMenageWorkerActivity.this, "Elemento: " + i + "; testo: " + trainers_list.get(i), Toast.LENGTH_SHORT).show();
                                }
                            });
                             */
                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymMenageWorkerActivity.this, "Nessun personal trainer come dipendente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //for (int j = 0; j < trainers_list.size(); j++) {
                //    Log.e("trainer n:" + j, trainers_list.get(j).toString());
                //}
            }

        }).execute(String.valueOf(user_id));
    }

    private static class ReceiveTrainersConn extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<TrainerObject> trainers);
        }

        public GymMenageWorkerActivity.ReceiveTrainersConn.AsyncResponse delegate = null;

        public ReceiveTrainersConn(GymMenageWorkerActivity.ReceiveTrainersConn.AsyncResponse delegate) {
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
                url = new URL("http://10.0.2.2:4000/gym/get_gym_trainers/" + params[0]);
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

    private void getNutritionists() {
        GymMenageWorkerActivity.ReceiveNutritionistsConn asyncTaskUser = (GymMenageWorkerActivity.ReceiveNutritionistsConn) new GymMenageWorkerActivity.ReceiveNutritionistsConn(new GymMenageWorkerActivity.ReceiveNutritionistsConn.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<NutritionistObject> nutritionists) {

                nutritionists_list = nutritionists;
                if (nutritionists_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei nutritionist da visualizzare nella recycler view(notificationView)
                            nutritionist_adapter = new CustomGymNutritionistAssumedAdapter(GymMenageWorkerActivity.this, nutritionists_list);
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
                            Toast.makeText(GymMenageWorkerActivity.this, "Nessun nutrizionista come dipendente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //for (int j = 0; j < nutritionists_list.size(); j++) {
                //    Log.e("nutritionist n:" + j, nutritionists_list.get(j).toString());
                //}
            }
        }).execute(String.valueOf(user_id));
    }

    private static class ReceiveNutritionistsConn extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<NutritionistObject> nutritionists);
        }

        public GymMenageWorkerActivity.ReceiveNutritionistsConn.AsyncResponse delegate = null;

        public ReceiveNutritionistsConn(GymMenageWorkerActivity.ReceiveNutritionistsConn.AsyncResponse delegate) {
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
                url = new URL("http://10.0.2.2:4000/gym/get_gym_nutritionists/" + params[0]);
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


    //endregion

    //region DISMISS PEOPLE


    public static void redoAdapterTrainer(Activity context, ArrayList<TrainerObject> trainers, Integer position) {
        ArrayList<TrainerObject> new_t = new ArrayList<>();
        for(int i = 0; i < trainers.size(); i++){
            if(i != position){
                new_t.add(trainers.get(i));
            }
        }
        trainer_adapter = new CustomGymTrainerAssumedAdapter(context, new_t);
        lv_trainer.setAdapter(trainer_adapter);
    }

    public static void redoAdapterNutri(Activity context, ArrayList<NutritionistObject> nutritionists, Integer position) {
        ArrayList<NutritionistObject> new_n = new ArrayList<>();
        for(int i = 0; i < nutritionists.size(); i++){
            if(i != position){
                new_n.add(nutritionists.get(i));
            }
        }
        nutritionist_adapter = new CustomGymNutritionistAssumedAdapter(context, new_n);
        lv_nutri.setAdapter(nutritionist_adapter);
    }


    public static void addToAdapterNutritionist(Activity context, ArrayList<NutritionistObject> nutritionists, NutritionistObject t_toAdd) {
        ArrayList<NutritionistObject> new_n = new ArrayList<>();
        for(int i = 0; i < nutritionists.size(); i++){
            new_n.add(nutritionists.get(i));
        }
        new_n.add(t_toAdd);
        nutritionist_adapter = new CustomGymNutritionistAssumedAdapter(context, new_n);
        lv_nutri.setAdapter(nutritionist_adapter);
    }


    //endregion
}

























