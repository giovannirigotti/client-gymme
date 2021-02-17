package android_team.gymme_client.trainer.manage_training_sheet;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.DrawerTrainerListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class TrainerTrainingSheetCustomer extends AppCompatActivity {

    private int user_id, trainer_id;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    public static ArrayList<TrainingSheetObject> sheet_list;
    static CustomTrainerTrainingSheetCustomerAdapter training_sheet_adapter;
    static ListView lv_training_sheets;

    Button btn_aggiungi_corso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_training_sheet_customer);


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

        btn_aggiungi_corso = (Button) findViewById(R.id.btn_create_training_sheet);

        lv_training_sheets = (ListView) findViewById(R.id.lv_trainer_training_sheet_customer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, trainer_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("SCHEDE ALLENAMENTI");

        getSheetInfo();

        btn_aggiungi_corso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Trainer Create Training Sheet Activity");
                Intent i = new Intent(getApplicationContext(), TrainerCreateTrainingSheetActivity.class);
                i.putExtra("user_id", user_id);
                i.putExtra("trainer_id", trainer_id);
                startActivity(i);
            }
        });

    }

    private void getSheetInfo() {
        TrainerTrainingSheetCustomer.ReceiveTrainingSheetConnection asyncTaskUser = (TrainerTrainingSheetCustomer.ReceiveTrainingSheetConnection) new TrainerTrainingSheetCustomer.ReceiveTrainingSheetConnection(new TrainerTrainingSheetCustomer.ReceiveTrainingSheetConnection.AsyncResponse() {
            @Override
            public void processFinish(final ArrayList<TrainingSheetObject> sheets) {
                sheet_list = sheets;
                if (sheet_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            training_sheet_adapter = new CustomTrainerTrainingSheetCustomerAdapter(TrainerTrainingSheetCustomer.this, sheet_list);
                            lv_training_sheets.setAdapter(training_sheet_adapter);

                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerTrainingSheetCustomer.this, "Nessuna scheda di allenamento", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }

    private static class ReceiveTrainingSheetConnection extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<TrainingSheetObject> customers);
        }

        public TrainerTrainingSheetCustomer.ReceiveTrainingSheetConnection.AsyncResponse delegate = null;

        public ReceiveTrainingSheetConnection(TrainerTrainingSheetCustomer.ReceiveTrainingSheetConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray _sheets = null;
            ArrayList<TrainingSheetObject> t_objects = new ArrayList<TrainingSheetObject>();

            try {
                url = new URL("http://10.0.2.2:4000/trainer/get_training_sheets_customer/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    _sheets = JsonParser.parseString(responseString).getAsJsonArray();

                    for (int i = 0; i < _sheets.size(); i++) {
                        JsonObject sheet = (JsonObject) _sheets.get(i);

                        String training_sheet_id = sheet.get("training_sheet_id").getAsString().trim();
                        String customer_id = sheet.get("customer_id").getAsString().trim();
                        String trainer_id = sheet.get("trainer_id").getAsString().trim();
                        String creation_date = sheet.get("creation_date").getAsString().trim();
                        String title = sheet.get("title").getAsString().trim();
                        String description = sheet.get("description").getAsString().trim();
                        String number_of_days = sheet.get("number_of_days").getAsString().trim();
                        String strength = sheet.get("strength").getAsString().trim();
                        String name = sheet.get("name").getAsString().trim();
                        String lastname = sheet.get("lastname").getAsString().trim();


                        TrainingSheetObject t_obj = new TrainingSheetObject(training_sheet_id, customer_id, trainer_id, creation_date, title, description, number_of_days, strength, name, lastname);
                        t_objects.add(t_obj);

                    }
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(t_objects);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("GET CUSTOMERS", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<TrainingSheetObject>());
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
            return _sheets;
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

    public void trainerToTrainingSheet(View view){
        drawerTrainerListener.toTrainingSheet();
    }
    public void trainerToProfile(View view){
        drawerTrainerListener.toProfile();
    }
    public void trainerToHome(View view){
        drawerTrainerListener.toHome();
    }
    //endregion
}