package android_team.gymme_client.gym.menage_course;

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
import android_team.gymme_client.gym.DrawerListener;
import android_team.gymme_client.gym.GymHomeActivity;
import android_team.gymme_client.gym.menage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.support.MyApplication;
import android_team.gymme_client.trainer.TrainerObject;

public class GymAddCoursesActivity extends AppCompatActivity {

    private int gym_id;
    private int user_id;
    DrawerListener drawerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    static String traienr_id;
    static CustomCourseTrainerAdapter trainer_adapter;
    static ArrayList<TrainerObject> trainer_list;

    static TextView tv_trainer_selezionato;
    static String trainer_selezionato;

    static ListView lv_trainer;
    public Button btn_add_course;
    private EditText et_descrizione, et_titolo, et_categoria, et_data_inizio, et_data_fine, et_numero_massimo;
    private String descrizione, titolo, categoria, data_inizio, data_fine;
    private Integer numero_massimo;

    private void initialize() {
        // Check intent data input (user_id)
        //region CHECK INTENT
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

        // Initialize variables
        gym_id = user_id;
        trainer_list = new ArrayList<>();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_gym_activity);
        drawerListener = new DrawerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("GYM COURSE");

        // Initialize and associates Wies
        lv_trainer = (ListView) findViewById(R.id.lv_course_trainer);
        tv_trainer_selezionato = (TextView) findViewById(R.id.tv_course_trainer_selezionato);
        et_descrizione = (EditText) findViewById(R.id.et_course_descrizione);
        et_titolo = (EditText) findViewById(R.id.et_course_titolo);
        et_categoria = (EditText) findViewById(R.id.et_course_categoria);
        et_data_inizio = (EditText) findViewById(R.id.et_course_data_inizio);
        et_data_fine = (EditText) findViewById(R.id.et_course_data_fine);
        et_numero_massimo = (EditText) findViewById(R.id.et_course_numero_massimo);
        btn_add_course = (Button) findViewById(R.id.btn_add_course);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_add_course);

        // INIZIALIZZO VALORI E VIEW
        initialize();

        // PRENDO DATI PER LA ListView
        // Controllo che non siano vuoti altrimenti apro dialog con errore (MANCANO TRAINER CHE POSSANO TENERE IL CORSO)
        // Se non sono vuoti faccio inserire i dati dalla activity
        getTrainers();

        btn_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputData()) {
                    // Mando dati alla connection per inserire il corso
                    insertCourse();
                }
            }
        });


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
        drawerListener.toCourse();
    }
    public void gymToClienti(View view){
        drawerListener.toCustomer();
    }
    public void gymToDipendenti(View view){
        drawerListener.toEmployees();
    }
    public void gymToProfilo(View view){
        drawerListener.toProfile();
    }
    public void gymToHome(View view){
        drawerListener.toHome();
    }
    //endregion

    //CARIMENTO DATI
    private void getTrainers() {
        GymAddCoursesActivity.ReceiveTrainersConn asyncTaskUser = (GymAddCoursesActivity.ReceiveTrainersConn) new GymAddCoursesActivity.ReceiveTrainersConn(new GymAddCoursesActivity.ReceiveTrainersConn.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<TrainerObject> trainers) {
                trainer_list = trainers;
                if (trainer_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            trainer_adapter = new CustomCourseTrainerAdapter(GymAddCoursesActivity.this, trainer_list);
                            lv_trainer.setAdapter(trainer_adapter);

                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //vToast.makeText(GymCoursesActivity.this, "Nessun personal trainer disponibile", Toast.LENGTH_SHORT).show();
                            showErrorDialog(GymAddCoursesActivity.this, user_id);
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

        public GymAddCoursesActivity.ReceiveTrainersConn.AsyncResponse delegate = null;

        public ReceiveTrainersConn(GymAddCoursesActivity.ReceiveTrainersConn.AsyncResponse delegate) {
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
                    delegate.processFinish(new ArrayList<TrainerObject>());
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("GET TRAINER", "I/O EXCEPTION ERROR");
                delegate.processFinish(new ArrayList<TrainerObject>());
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

    //DIALOG ERRORE
    private void showErrorDialog(Activity a, int user_id) {
        String errorMessage = "Stai provando a creare un corso ma non hai Personal Trainer qualificati che possano tenerlo.\n" +
                "Vai alla pagina GESTIONE DIPENDENTI per assumere un Personal Trainer qualificato e riprova!";
        GymAddCoursesActivity.CustomErrorDialog cdd = new GymAddCoursesActivity.CustomErrorDialog(a, user_id, errorMessage, GymMenageWorkerActivity.class);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomErrorDialog extends Dialog implements View.OnClickListener {

        public Class destination;
        public Activity c;
        public Button Esci;
        String errorMessage;
        TextView tv_error_message;
        public Integer user_id;

        public CustomErrorDialog(Activity a, Integer user_id, String error_message, Class destination) {
            super(a);
            this.c = a;
            this.user_id = user_id;
            this.errorMessage = error_message;
            this.destination = destination;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_error);
            Esci = (Button) findViewById(R.id.dialog_btn_esci);
            tv_error_message = (TextView) findViewById(R.id.tv_message_error);

            tv_error_message.setText(errorMessage);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_btn_esci:
                    Log.e("REDIRECT", "Gym Menage Trainer Activity");
                    Intent i = new Intent(getApplicationContext(), destination);
                    i.putExtra("user_id", user_id);
                    startActivity(i);
                    finish();
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    //SELEZIONE TRAINER
    public static void selectTrainer(Integer position) {
        String nominativo = trainer_list.get(position).name + " " + trainer_list.get(position).lastname;

        traienr_id = trainer_list.get(position).user_id;
        trainer_selezionato = nominativo;

        tv_trainer_selezionato.setText(trainer_selezionato);
        Log.e("Trainer selezionato", traienr_id + " " + trainer_selezionato);
        Toast.makeText(MyApplication.getContext(), "Selezionato trainer: "+trainer_selezionato, Toast.LENGTH_LONG).show();

    }

    //CONTROLLI INPUT
    private boolean checkInputData() {

        if (checkTrainer()) {
            if (checkDescrizione()) {
                if (checkTitolo()) {
                    if (checkCategoria()) {
                        if (checkDataInizio()) {
                            if (checkDataFine()) {
                                if (checkNumber()) {
                                    Log.e("CHECK", "ALL OK");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        // Gestione errori nei singoli metodi check...
        return false;
    }

    private boolean checkNumber() {
        String n_max = et_numero_massimo.getText().toString().trim();
        if (n_max.isEmpty()) {
            Toast.makeText(GymAddCoursesActivity.this, "Inserisci un numero massimo di partecipanti!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            try {
                numero_massimo = Integer.parseInt(n_max);
                Log.e("CONVERSIONE NUMERO", "OK");
                return true;
            } catch (Exception e) {
                Log.e("CONVERSIONE NUMERO", "ERROR");
                Toast.makeText(GymAddCoursesActivity.this, "Inserisci un numero massimo di sole cifre numeriche!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean checkDataFine() {
        data_fine = et_data_fine.getText().toString().trim();
        if (data_fine.isEmpty()) {
            Toast.makeText(GymAddCoursesActivity.this, "Inserisci una data di fine!", Toast.LENGTH_LONG).show();
            return false;
        } else if (!validateDate(data_fine)) {
            Toast.makeText(GymAddCoursesActivity.this, "DATA FINE ERRATA.\n   - FORMATO: <gg/mm/AAAA> senza spazi vuoti e inserendo appositi zeri\n   - DATA: dal 2020 al 2200", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDataInizio() {
        data_inizio = et_data_inizio.getText().toString().trim();
        if (data_inizio.isEmpty()) {
            Toast.makeText(GymAddCoursesActivity.this, "Inserisci una data di inizio!", Toast.LENGTH_LONG).show();
            return false;
        } else if (!validateDate(data_inizio)) {
            Toast.makeText(GymAddCoursesActivity.this, "DATA FINE ERRATA.\n   - FORMATO: <gg/mm/AAAA> senza spazi vuoti e inserendo appositi zeri\n   - DATA: dal 2020 al 2200\"", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkCategoria() {
        categoria = et_categoria.getText().toString().trim();
        if (categoria.isEmpty()) {
            Toast.makeText(GymAddCoursesActivity.this, "Inserisci un categoria!", Toast.LENGTH_LONG).show();
            return false;
        } else if (categoria.length() > 100) {
            Toast.makeText(GymAddCoursesActivity.this, "Categoria troppo lunga. MAX 200 caratteri.\nUsati: " + categoria.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "categoria OK");
            return true;
        }
    }

    private boolean checkTitolo() {
        titolo = et_titolo.getText().toString().trim();
        if (titolo.isEmpty()) {
            Toast.makeText(GymAddCoursesActivity.this, "Inserisci un titolo!", Toast.LENGTH_LONG).show();
            return false;
        } else if (titolo.length() > 100) {
            Toast.makeText(GymAddCoursesActivity.this, "Titolo troppo lunga. MAX 100 caratteri.\nUsati: " + titolo.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "TITOLO OK");
            return true;
        }
    }

    private boolean checkDescrizione() {
        descrizione = et_descrizione.getText().toString().trim();
        if (descrizione.isEmpty()) {
            Toast.makeText(GymAddCoursesActivity.this, "Inserisci una descrizione!", Toast.LENGTH_LONG).show();
            return false;
        } else if (descrizione.length() > 1000) {
            Toast.makeText(GymAddCoursesActivity.this, "Descrizione troppo lunga. MAX 1000 caratteri.\nUsati: " + descrizione.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "DESCRIZIONE OK");
            return true;
        }

    }

    private boolean checkTrainer() {
        trainer_selezionato = tv_trainer_selezionato.getText().toString().trim();
        if (trainer_selezionato.isEmpty()) {
            Toast.makeText(GymAddCoursesActivity.this, "Seleziona un trainer dalla lista", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateDate(String birthdate) {
        boolean checkFormat;
        boolean checkNumber;

        if (birthdate.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
            checkFormat = true;
        else
            checkFormat = false;

        String[] t = birthdate.split("/");
        checkNumber = (Integer.valueOf(t[0]) <= 31 && Integer.valueOf(t[1]) <= 12 && Integer.valueOf(t[2]) >= 2020 && Integer.valueOf(t[2]) <= 2200);

        return (checkNumber && checkFormat);
    }

    // CREAZIONE CORSO SUL DB
    // POST --> /gym/insert_course/

    private void insertCourse(){
        GymAddCoursesActivity.AddCourseConnection asyncTask = (GymAddCoursesActivity.AddCourseConnection) new GymAddCoursesActivity.AddCourseConnection(new GymAddCoursesActivity.AddCourseConnection.AsyncResponse() {
            @Override
            public void processFinish(Integer output) {
                if (output == 200) {
                    GymMenageWorkerActivity.runOnUI(new Runnable() {
                        public void run() {
                            Toast.makeText(GymAddCoursesActivity.this, "SUCCESS, corso aggiunto", Toast.LENGTH_SHORT).show();
                            Log.e("REDIRECT", "Gym Profile Activity");
                            Intent i = new Intent(getApplicationContext(), GymHomeActivity.class);
                            i.putExtra("user_id", user_id);
                            startActivity(i);
                            finish();
                        }
                    });
                } else {
                    GymMenageWorkerActivity.runOnUI(new Runnable() {
                        public void run() {
                            Toast.makeText(GymAddCoursesActivity.this, "ERRORE AGGIUNTA CORSO, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).execute(String.valueOf(gym_id), String.valueOf(traienr_id), descrizione, titolo, categoria, data_inizio, data_fine, String.valueOf(numero_massimo));
    }


    public static class AddCourseConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public GymAddCoursesActivity.AddCourseConnection.AsyncResponse delegate = null;

        public AddCourseConnection(GymAddCoursesActivity.AddCourseConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/gym/insert_course/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("gym_id", params[0]);
                paramsJson.addProperty("trainer_id", params[1]);
                paramsJson.addProperty("description", params[2]);
                paramsJson.addProperty("title", params[3]);
                paramsJson.addProperty("category", params[4]);
                paramsJson.addProperty("start_date", params[5]);
                paramsJson.addProperty("end_date", params[6]);
                paramsJson.addProperty("max_persons", params[7]);

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
                    Log.e("GYM COURSE", "AGGIUNTO OK");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("GYM COURSE", "Error CREAZIONE CORSO");
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