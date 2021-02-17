package android_team.gymme_client.trainer.manage_training_sheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.DrawerTrainerListener;

public class TrainerCreateDaysActivity extends AppCompatActivity {

    private int days, sheet_id, customer_id;
    private static int user_id;

    private static int DAY_POSITION;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    private ArrayList<Integer> day_list;
    static CustomCreateDayAdapter day_adapter;
    static ListView lv_day;

    private static ArrayList<Boolean> day_check;

    private Button btn_end_create_day;

    public static Integer getUserId() {
        return user_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_create_days);

        btn_end_create_day = (Button) findViewById(R.id.btn_end_create_day);
        lv_day = (ListView) findViewById(R.id.lv_create_days);

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
        if (!i.hasExtra("days")) {
            Toast.makeText(this, "days mancanti", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            days = i.getIntExtra("days", -1);
            Log.w("days ricevuti:", String.valueOf(days));
            if (days == -1) {
                Toast.makeText(this, "days passato male.", Toast.LENGTH_LONG).show();
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
        if (!i.hasExtra("customer_id")) {
            Toast.makeText(this, "customer_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            customer_id = i.getIntExtra("customer_id", -1);
            Log.w("customer_id ricevuto:", String.valueOf(customer_id));
            if (customer_id == -1) {
                Toast.makeText(this, "customer_id passato male.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }

        //endregion

        day_list = new ArrayList<>();
        for(int k = 1; k <= days; k++){
            day_list.add(k);
        }
        day_check = new ArrayList<>();
        for(int k = 1; k <= days; k++){
            day_check.add(false);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("GIORNI SCHEDA");

        day_adapter = new CustomCreateDayAdapter(TrainerCreateDaysActivity.this, day_list, sheet_id);
        lv_day.setAdapter(day_adapter);



        btn_end_create_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllDays()){
                    //TODO VADO ALLA ACTIVITY DELLE SCHEDE e INVIO NOTIFICA
                    sendNotify();

                }
                else{
                    Toast.makeText(TrainerCreateDaysActivity.this, "Completa l'inserimento di tutti i giorni", Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    private void sendNotify() {
        TrainerCreateDaysActivity.InsertNotifyConnection asyncTask = (TrainerCreateDaysActivity.InsertNotifyConnection) new TrainerCreateDaysActivity.InsertNotifyConnection(new TrainerCreateDaysActivity.InsertNotifyConnection.AsyncResponse() {

            @Override
            public void processFinish(Integer output) {
                if (output == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerCreateDaysActivity.this, "SCHEDA INVIATA", Toast.LENGTH_SHORT).show();
                            Log.e("REDIRECT", "Trainer Menage Training Sheet");
                            Intent i = new Intent(getApplicationContext(), TrainerMenageTrainingSheet.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtra("user_id", user_id);
                            startActivity(i);
                            finish();
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerCreateDaysActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }).execute(String.valueOf(customer_id));
    }


    public static class InsertNotifyConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public TrainerCreateDaysActivity.InsertNotifyConnection.AsyncResponse delegate = null;

        public InsertNotifyConnection(TrainerCreateDaysActivity.InsertNotifyConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/insert_notifications/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("text", "Nuova scheda di allenamento disponibile");
                paramsJson.addProperty("notification_type", 20);

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
                    Log.e("NOTIFICA", "INSERITA SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("NOTIFICA", "Error");
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

    public static void updatePosition(Integer position) {
        DAY_POSITION = position;
    }

    private boolean checkAllDays() {
        boolean res = true;
        for (int i = 0; i < days; i++){
            if(day_check.get(i) == false){
                res = false;
            }
        }
        return res;
    }

    public static void blockButton() {
        lv_day.getChildAt(DAY_POSITION).findViewById(R.id.btn_create_day_plus).setEnabled(false);
        TextView to_edit = (TextView) lv_day.getChildAt(DAY_POSITION).findViewById(R.id.tv_create_day_text);
        to_edit.setText("COMPLETATO");
    }

    public static void checkDayOK(){
        day_check.set(DAY_POSITION, true);
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