package android_team.gymme_client.trainer.manage_training_sheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import android_team.gymme_client.gym.manage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.DrawerTrainerListener;

public class TrainerCreateTrainingSheetActivity extends AppCompatActivity {

    private int user_id, trainer_id;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    EditText et_title, et_description, et_date, et_days;
    String str_title, str_description, str_date;
    Integer str_days;
    Button btn_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_create_training_sheet);

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

        if (!i.hasExtra("trainer_id")) {
            Toast.makeText(this, "trainer_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            trainer_id = i.getIntExtra("trainer_id", -1);
            Log.w("trainer_id ricevuto:", String.valueOf(trainer_id));
            if (trainer_id == -1) {
                Toast.makeText(this, "Trainer non creato.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, trainer_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("CREA SCHEDA");

        et_title = (EditText) findViewById(R.id.et_create_sheet_title);
        et_description = (EditText) findViewById(R.id.et_create_sheet_description);
        et_date = (EditText) findViewById(R.id.et_create_sheet_date);
        et_days = (EditText) findViewById(R.id.et_create_sheet_days);

        btn_go = (Button) findViewById(R.id.btn_create_sheet_go);

        //MANDO INSERT AL SERVER PER TABELLA trainig_sheets e dati utili per la activity succesiva
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTrainingSheet();
            }
        });


    }

    private void createTrainingSheet() {
        if (checkData()) {
            insertTrainigSheet();
        }
        //ERRORI GESTITI NEI SINGOLI CHECK DI OGNI DATO
    }

    //region CHECK DATA
    private boolean checkData() {

        if(checkTitolo()){
            if(checkDescrizione()){
                if(checkDataCreazione()){
                    if(checkNumber()){
                        Log.e("CHECK", "ALL OK");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkNumber() {
        String n_days = et_days.getText().toString().trim();
        if (n_days.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un numero di giorni!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            try {
                str_days = Integer.parseInt(n_days);
                Log.e("CONVERSIONE NUMERO", "OK");
                if(str_days < 1 || str_days > 7 ){
                    Log.e("CONVERSIONE NUMERO", "ERRORE DIMENSIONE NUMERO");
                    Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un numero di giorni compreso tra 1 e 7!", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            } catch (Exception e) {
                Log.e("CONVERSIONE NUMERO", "ERROR");
                Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un numero di sole cifre numeriche!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean checkDataCreazione() {
        str_date = et_date.getText().toString().trim();
        if (str_date.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci una data di creazione!", Toast.LENGTH_LONG).show();
            return false;
        } else if (!validateDate(str_date)) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "DATA ERRATA.\n   - FORMATO: <gg/mm/AAAA> senza spazi vuoti e inserendo appositi zeri\n   - DATA: dal 2020 al 2200", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTitolo() {
        str_title = et_title.getText().toString().trim();
        if (str_title.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un titolo!", Toast.LENGTH_LONG).show();
            return false;
        } else if (str_title.length() > 200) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Titolo troppo lunga. MAX 200 caratteri.\nUsati: " + str_title.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "TITOLO OK");
            return true;
        }
    }

    private boolean checkDescrizione() {
        str_description = et_description.getText().toString().trim();
        if (str_description.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci una descrizione!", Toast.LENGTH_LONG).show();
            return false;
        } else if (str_description.length() > 2000) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Descrizione troppo lunga. MAX 2000 caratteri.\nUsati: " + str_description.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "DESCRIZIONE OK");
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
    //endregion

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

    private void insertTrainigSheet(){
        TrainerCreateTrainingSheetActivity.AddSheetConnection asyncTask = (TrainerCreateTrainingSheetActivity.AddSheetConnection) new TrainerCreateTrainingSheetActivity.AddSheetConnection(new TrainerCreateTrainingSheetActivity.AddSheetConnection.AsyncResponse() {
            @Override
            public void processFinish(Integer output, final Integer sheet_id) {
                if (output == 200) {
                    GymMenageWorkerActivity.runOnUI(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "SUCCESS, scheda creata", Toast.LENGTH_SHORT).show();
                            Log.e("REDIRECT", "Trainer Create Days Activity");
                            Intent i = new Intent(getApplicationContext(), TrainerCreateDaysActivity.class);
                            // Prima user_id era diventato quello del customer e trainer_id quello che globalmente
                            // viene chiamato user_id. Con questo passaggio torno nello standard
                            i.putExtra("user_id", trainer_id);
                            i.putExtra("days", str_days);
                            i.putExtra("sheet_id", sheet_id);
                            i.putExtra("customer_id", user_id);
                            startActivity(i);
                            finish();
                        }
                    });
                } else {
                    GymMenageWorkerActivity.runOnUI(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "ERRORE AGGIUNTA CORSO, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).execute(String.valueOf(user_id), String.valueOf(trainer_id), str_title, str_description, str_date, String.valueOf(str_days));
    }


    public static class AddSheetConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output, Integer sheet_id);
        }

        public TrainerCreateTrainingSheetActivity.AddSheetConnection.AsyncResponse delegate = null;

        public AddSheetConnection(TrainerCreateTrainingSheetActivity.AddSheetConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/trainer/create_training_sheet/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("customer_id", params[0]);
                paramsJson.addProperty("trainer_id", params[1]);
                paramsJson.addProperty("title", params[2]);
                paramsJson.addProperty("description", params[3]);
                paramsJson.addProperty("creation_date", params[4]);
                paramsJson.addProperty("number_of_days", params[5]);

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
                    Log.e("TRAINING SHEET", "AGGIUNTO OK");
                    responseCode = 200;
                    String responseString = readStream(urlConnection.getInputStream());
                    JsonObject sheet = JsonParser.parseString(responseString).getAsJsonObject();
                    Integer sheet_id = sheet.get("training_sheet_id").getAsInt();
                    Log.e("sheet_id ricevuto", ""+sheet_id);
                    delegate.processFinish(responseCode, sheet_id);
                } else {
                    Log.e("TRAINING SHEET", "Error INSERIMENTO TRAINING SHEET");
                    responseCode = 500;
                    delegate.processFinish(responseCode, 0);
                    urlConnection.disconnect();
                }
            } catch (IOException e){
                Log.e("TRAINING SHEET", "I/O EXCEPTION ERROR");
                e.printStackTrace();
                responseCode = 69;
                delegate.processFinish(responseCode, 0);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
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