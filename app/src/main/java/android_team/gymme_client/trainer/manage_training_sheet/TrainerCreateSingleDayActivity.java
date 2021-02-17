package android_team.gymme_client.trainer.manage_training_sheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.DrawerTrainerListener;

public class TrainerCreateSingleDayActivity extends AppCompatActivity {

    private int user_id, seq, sheet_id;
    private static boolean INSERT_ERROR;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;
    Button btn_add_exercise, btn_end;

    //DIALOG
    ArrayList<ExerciseObject> exercise_list;
    static TextView tv_dialog;
    static CustomExerciseAdapter exercise_adapter;
    static ListView lv_exercise;
    static Integer exercise_id;

    //LISTVIEW ESERCIZI E RIPETIZIONI
    static ArrayList<CompleteExerciseObject> complete_list;
    static CustomCompleteExerciseAdapter complete_adapter;
    static ListView lv_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_create_single_day);

        complete_list = new ArrayList<>();
        lv_complete = (ListView) findViewById(R.id.lv_create_days);

        //region CHET INTENT EXTRAS
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
        if (!i.hasExtra("seq")) {
            Toast.makeText(this, "seq mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            seq = i.getIntExtra("seq", -1);
            Log.w("seq ricevuto:", String.valueOf(seq));
            if (seq == -1) {
                Toast.makeText(this, "seq passato male.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }
        if (!i.hasExtra("sheet_id")) {
            Toast.makeText(this, "sheet_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            sheet_id = i.getIntExtra("sheet_id", -1);
            Log.w("sheet_id ricevuto:", String.valueOf(sheet_id));
            if (sheet_id == -1) {
                Toast.makeText(this, "sheet_id passato male.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }

        //endregion
        INSERT_ERROR = true;
        btn_add_exercise = (Button) findViewById(R.id.btn_add_exercise);
        btn_end = (Button) findViewById(R.id.btn_end_create_day);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("ESERCIZI SCHEDA");

        btn_add_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("OPEN DIALOG", "Oper dialog add exercise");
                InsertExercise(TrainerCreateSingleDayActivity.this, user_id);
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exercise_list.size() < 1){
                    Toast.makeText(TrainerCreateSingleDayActivity.this, "Nessun esercizio selezionato", Toast.LENGTH_LONG).show();
                } else {
                    insertCompletedExercises();
                    //BLOCCO BOTTONE
                    TrainerCreateDaysActivity.blockButton();
                    //DO PER VERO CHE CRO IL GIORNO
                    TrainerCreateDaysActivity.checkDayOK();
                    finish();
                }
            }
        });
    }


    public static void selectExercise(ExerciseObject exerciseObject) {
        tv_dialog.setText(exerciseObject.getName());
        exercise_id = Integer.parseInt(exerciseObject.getExercise_id());
    }

    private void InsertExercise(Activity a, int user_id) {
        TrainerCreateSingleDayActivity.CustomDialogoSelectExercise cdd = new TrainerCreateSingleDayActivity.CustomDialogoSelectExercise(a, user_id);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogoSelectExercise extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Button Inserisci, Esci;
        public EditText repetitions;
        public Integer user_id;

        public CustomDialogoSelectExercise(Activity a, Integer user_id) {
            super(a);
            this.c = a;
            this.user_id = user_id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_insert_exercise);
            Inserisci = (Button) findViewById(R.id.dialog_confirm_exercise_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_exercise_no);
            repetitions = (EditText) findViewById(R.id.et_dialog_insert_repetitions);
            lv_exercise = (ListView) findViewById(R.id.lv_select_exercise);
            tv_dialog = (TextView) findViewById(R.id.tv_selected_exercise);

            getExercises();

            Inserisci.setOnClickListener(this);
            Esci.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_confirm_exercise_yes:
                    if (checkData()) {
                        //CREO NUOVO OGGETTO DA INSERIRE NEGLI ESERCIZI DI UNA GIORNATA
                        CompleteExerciseObject to_add = new CompleteExerciseObject(String.valueOf(sheet_id), String.valueOf(seq), repetitions.getText().toString(), String.valueOf(exercise_id), tv_dialog.getText().toString());
                        complete_list.add(to_add);
                        complete_adapter = new CustomCompleteExerciseAdapter(c, complete_list);
                        lv_complete.setAdapter(complete_adapter);
                        dismiss();
                    }
                    break;
                case R.id.dialog_confirm_exercise_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }

        private boolean checkData() {
            boolean res = true;
            if (tv_dialog.getText().toString().equals("Esercizio selezionato")) {
                res = false;
                Toast.makeText(c, "Nessun esercizio selezionato", Toast.LENGTH_LONG).show();
            }
            if (repetitions.getText().toString().equals("")) {
                res = false;
                Toast.makeText(c, "Inserisci il numero di ripetizioni", Toast.LENGTH_LONG).show();
            }
            return res;
        }
    }


    public static void removeFromCompleteAdapter(Activity context, Integer position) {
        ArrayList<CompleteExerciseObject> new_arr = new ArrayList<>();
        for (int i = 0; i < complete_list.size(); i++) {
            if (position != i) {
                new_arr.add(complete_list.get(i));
            }
        }
        complete_list = new_arr;

        complete_adapter = new CustomCompleteExerciseAdapter(context, complete_list);
        lv_complete.setAdapter(complete_adapter);
    }


    private void getExercises() {
        TrainerCreateSingleDayActivity.ReceiveExerciseConnection asyncTaskUser = (TrainerCreateSingleDayActivity.ReceiveExerciseConnection) new TrainerCreateSingleDayActivity.ReceiveExerciseConnection(new TrainerCreateSingleDayActivity.ReceiveExerciseConnection.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<ExerciseObject> exercises) {
                exercise_list = exercises;
                if (exercise_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            exercise_adapter = new CustomExerciseAdapter(TrainerCreateSingleDayActivity.this, exercise_list);
                            lv_exercise.setAdapter(exercise_adapter);

                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerCreateSingleDayActivity.this, "Nessun cliente nella tua palestra", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }


    private void insertCompletedExercises() {

        for (int i = 0; i < complete_list.size(); i++) {
            InsertExercise(complete_list.get(i));
        }
    }

    private void InsertExercise(CompleteExerciseObject object_to_add) {
        TrainerCreateSingleDayActivity.InsertExerciseConnection asyncTask = (TrainerCreateSingleDayActivity.InsertExerciseConnection) new TrainerCreateSingleDayActivity.InsertExerciseConnection(new TrainerCreateSingleDayActivity.InsertExerciseConnection.AsyncResponse() {

            @Override
            public void processFinish(Integer output) {
                if (output != 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerCreateSingleDayActivity.this, "ERRORE inserimento", Toast.LENGTH_SHORT).show();
                            INSERT_ERROR = false;
                        }
                    });
                }
            }
        }).execute(object_to_add.getTraining_sheet_id(), object_to_add.getSeq(), object_to_add.getRepetitions(), object_to_add.getExercise_id());
    }


    public static class InsertExerciseConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public TrainerCreateSingleDayActivity.InsertExerciseConnection.AsyncResponse delegate = null;

        public InsertExerciseConnection(TrainerCreateSingleDayActivity.InsertExerciseConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/trainer/insert_day_exercises/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("training_sheet_id", params[0]);
                paramsJson.addProperty("seq", params[1]);
                paramsJson.addProperty("repetitions", params[2]);
                paramsJson.addProperty("exercise_id", params[3]);


                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(paramsJson.toString());
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("EXERCISE SHEET", "CAMBIATI SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("EXERCISE SHEET", "Error");
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


    private static class ReceiveExerciseConnection extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<ExerciseObject> exercises);
        }

        public TrainerCreateSingleDayActivity.ReceiveExerciseConnection.AsyncResponse delegate = null;

        public ReceiveExerciseConnection(TrainerCreateSingleDayActivity.ReceiveExerciseConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray _customers = null;
            ArrayList<ExerciseObject> t_objects = new ArrayList<ExerciseObject>();

            try {
                url = new URL("http://10.0.2.2:4000/trainer/get_exercises/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    _customers = JsonParser.parseString(responseString).getAsJsonArray();

                    for (int i = 0; i < _customers.size(); i++) {
                        JsonObject customer = (JsonObject) _customers.get(i);

                        String exercise_id = customer.get("exercise_id").getAsString().trim();
                        String name = customer.get("name").getAsString().trim();


                        ExerciseObject t_obj = new ExerciseObject(exercise_id, name);
                        t_objects.add(t_obj);

                    }
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(t_objects);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("GET CUSTOMERS", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<ExerciseObject>());
                } else {
                    Log.e("GET CUSTOMERS", "SERVER ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("GET CUSTOMERS", "I/O EXCEPTION ERROR");
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return _customers;
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

    public void trainerToTrainingSheet(View view) {
        drawerTrainerListener.toTrainingSheet();
    }

    public void trainerToProfile(View view) {
        drawerTrainerListener.toProfile();
    }

    public void trainerToHome(View view) {
        drawerTrainerListener.toHome();
    }
    //endregion
}