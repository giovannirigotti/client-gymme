package android_team.gymme_client.gym.menage_profile;

import androidx.appcompat.app.AppCompatActivity;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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

public class GymEditHoursActivity extends AppCompatActivity {

    //region BINDINGS
    @BindView(R.id.monday_opening_gym_ti)
    TextInputLayout monday_opening_gym_ti;
    @BindView(R.id.monday_opening_gym_et)
    EditText monday_opening_gym_et;
    @BindView(R.id.monday_closing_gym_ti)
    TextInputLayout monday_closing_gym_ti;
    @BindView(R.id.monday_closing_gym_et)
    EditText monday_closing_gym_et;
    @BindView(R.id.tuesday_opening_gym_ti)
    TextInputLayout tuesday_opening_gym_ti;
    @BindView(R.id.tuesday_opening_gym_et)
    EditText tuesday_opening_gym_et;
    @BindView(R.id.tuesday_closing_gym_ti)
    TextInputLayout tuesday_closing_gym_ti;
    @BindView(R.id.tuesday_closing_gym_et)
    EditText tuesday_closing_gym_et;
    @BindView(R.id.wednesday_opening_gym_ti)
    TextInputLayout wednesday_opening_gym_ti;
    @BindView(R.id.wednesday_opening_gym_et)
    EditText wednesday_opening_gym_et;
    @BindView(R.id.wednesday_closing_gym_ti)
    TextInputLayout wednesday_closing_gym_ti;
    @BindView(R.id.wednesday_closing_gym_et)
    EditText wednesday_closing_gym_et;
    @BindView(R.id.thursday_opening_gym_ti)
    TextInputLayout thursday_opening_gym_ti;
    @BindView(R.id.thursday_opening_gym_et)
    EditText thursday_opening_gym_et;
    @BindView(R.id.thursday_closing_gym_ti)
    TextInputLayout thursday_closing_gym_ti;
    @BindView(R.id.thursday_closing_gym_et)
    EditText thursday_closing_gym_et;
    @BindView(R.id.friday_opening_gym_ti)
    TextInputLayout friday_opening_gym_ti;
    @BindView(R.id.friday_opening_gym_et)
    EditText friday_opening_gym_et;
    @BindView(R.id.friday_closing_gym_ti)
    TextInputLayout friday_closing_gym_ti;
    @BindView(R.id.friday_closing_gym_et)
    EditText friday_closing_gym_et;
    @BindView(R.id.saturday_opening_gym_ti)
    TextInputLayout saturday_opening_gym_ti;
    @BindView(R.id.saturday_opening_gym_et)
    EditText saturday_opening_gym_et;
    @BindView(R.id.saturday_closing_gym_ti)
    TextInputLayout saturday_closing_gym_ti;
    @BindView(R.id.saturday_closing_gym_et)
    EditText saturday_closing_gym_et;
    @BindView(R.id.sunday_opening_gym_ti)
    TextInputLayout sunday_opening_gym_ti;
    @BindView(R.id.sunday_opening_gym_et)
    EditText sunday_opening_gym_et;
    @BindView(R.id.sunday_closing_gym_ti)
    TextInputLayout sunday_closing_gym_ti;
    @BindView(R.id.sunday_closing_gym_et)
    EditText sunday_closing_gym_et;

    @BindView(R.id.btn_gym_update_hours)
    Button btn_gym_update_hours;
    //endregion

    private int user_id;

    int opening_monday = -1;
    int closing_monday = -1;
    int opening_tuesday = -1;
    int closing_tuesday = -1;
    int opening_wednesday = -1;
    int closing_wednesday = -1;
    int opening_thursday = -1;
    int closing_thursday = -1;
    int opening_friday = -1;
    int closing_friday = -1;
    int opening_saturday = -1;
    int closing_saturday = -1;
    int opening_sunday = -1;
    int closing_sunday = -1;


    String o_monday;
    String c_monday;
    String o_tuesday;
    String c_tuesday;
    String o_wednesday;
    String c_wednesday;
    String o_thursday;
    String c_thursday;
    String o_friday;
    String c_friday;
    String o_saturday;
    String c_saturday;
    String o_sunday;
    String c_sunday;

    Integer reset_o_monday, reset_c_monday, reset_o_tuesday, reset_c_tuesday,
            reset_o_wednesday, reset_c_wednesday, reset_o_thursday, reset_c_thursday,
            reset_o_friday, reset_c_friday, reset_o_saturday, reset_c_saturday,
            reset_o_sunday, reset_c_sunday;


    // opening_monday, closing_monday, opening_tuesday, closing_tuesday, opening_wednesday, closing_wednesday,
    // opening_thursday, closing_thursday, opening_friday, closing_friday, opening_saturday, closing_saturday, opening_sunday, closing_sunday


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_edit_hours);
        ButterKnife.bind(this);

        //region CHECK INTENT.EXTRAS RECEIVED
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

        //CARICO ORARI SALVATI NEL DB
        GetHoursData();

        //  BUTTON AVANTI MENAGEMENT
        btn_gym_update_hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHours();
            }
        });

    }

    private void UpdateHours() {
        updateViewVal();
        boolean is_ok = checkAndAssignViewVal();
        if (is_ok) {
            GymEditHoursActivity.UpdateHoursConnection asyncTask = (GymEditHoursActivity.UpdateHoursConnection) new GymEditHoursActivity.UpdateHoursConnection(new GymEditHoursActivity.UpdateHoursConnection.AsyncResponse() {

                @Override
                public void processFinish(Integer output) {
                    if (output == 200) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(GymEditHoursActivity.this, "SUCCESS, orari aggiornati", Toast.LENGTH_SHORT).show();
                                // REDIRECT TO PROFILE GYM ACTIVITY
                                Log.e("REDIRECT", "Gym Profile Activity");
                                Intent i = new Intent(getApplicationContext(), GymProfileActivity.class);
                                i.putExtra("user_id", user_id);
                                startActivity(i);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(GymEditHoursActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


            }).execute(String.valueOf(user_id), String.valueOf(opening_monday), String.valueOf(closing_monday), String.valueOf(opening_tuesday), String.valueOf(closing_tuesday), String.valueOf(opening_wednesday),
                    String.valueOf(closing_wednesday), String.valueOf(opening_thursday), String.valueOf(closing_thursday), String.valueOf(opening_friday), String.valueOf(closing_friday), String.valueOf(opening_saturday),
                    String.valueOf(closing_saturday), String.valueOf(opening_sunday), String.valueOf(closing_sunday));
            

        } else {
            //SE ERRORE NEGLI INSERIMENTI
            Toast.makeText(getApplicationContext(), "Errore immissione dati:\n" +
                    " - FORMATO: <hh:mm>\n" +
                    " - INSERISCI COPPIA: sia apertura che chiusura per ogni giorno\n" +
                    " - ALTRO ERRORE", Toast.LENGTH_LONG).show();
        }


    }

    public static class UpdateHoursConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public GymEditHoursActivity.UpdateHoursConnection.AsyncResponse delegate = null;

        public UpdateHoursConnection(GymEditHoursActivity.UpdateHoursConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/gym/update_hours/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("gym_id", params[0]);
                paramsJson.addProperty("opening_monday", params[1]);
                paramsJson.addProperty("closing_monday", params[2]);
                paramsJson.addProperty("opening_tuesday", params[3]);
                paramsJson.addProperty("closing_tuesday", params[4]);
                paramsJson.addProperty("opening_wednesday", params[5]);
                paramsJson.addProperty("closing_wednesday", params[6]);
                paramsJson.addProperty("opening_thursday", params[7]);
                paramsJson.addProperty("closing_thursday", params[8]);
                paramsJson.addProperty("opening_friday", params[9]);
                paramsJson.addProperty("closing_friday", params[10]);
                paramsJson.addProperty("opening_saturday", params[11]);
                paramsJson.addProperty("closing_saturday", params[12]);
                paramsJson.addProperty("opening_sunday", params[13]);
                paramsJson.addProperty("closing_sunday", params[14]);

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
                    Log.e("GYM HOURS", "CAMBIATI SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("GYM HOURS", "Error");
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


    private boolean checkAndAssignViewVal() {
        boolean res = true;
        reset_o_monday = opening_monday;
        reset_c_monday = closing_monday;
        reset_o_tuesday = opening_tuesday;
        reset_c_tuesday = closing_tuesday;
        reset_o_wednesday = opening_wednesday;
        reset_c_wednesday = closing_wednesday;
        reset_o_thursday = opening_thursday;
        reset_c_thursday = closing_thursday;
        reset_o_friday = opening_friday;
        reset_c_friday = closing_friday;
        reset_o_saturday = opening_saturday;
        reset_c_saturday = closing_saturday;
        reset_o_sunday = opening_sunday;
        reset_c_sunday = closing_sunday;

        if (o_monday.isEmpty()) {
            opening_monday = -1;
        } else if (validateHour(o_monday)) {
            opening_monday = toMins(o_monday);
        } else {
            res = false;
            reset();
        }
        if (c_monday.isEmpty()) {
            closing_monday = -1;
        } else if (validateHour(c_monday)) {
            closing_monday = toMins(c_monday);
        } else {
            res = false;
            reset();
        }

        if (o_tuesday.isEmpty()) {
            opening_tuesday = -1;
        } else if (validateHour(o_tuesday)) {
            opening_tuesday = toMins(o_tuesday);
        } else {
            res = false;
            reset();
        }
        if (c_tuesday.isEmpty()) {
            closing_tuesday = -1;
        } else if (validateHour(c_tuesday)) {
            closing_tuesday = toMins(c_tuesday);
        } else {
            res = false;
            reset();
        }

        if (o_wednesday.isEmpty()) {
            opening_wednesday = -1;
        } else if (validateHour(o_wednesday)) {
            opening_wednesday = toMins(o_wednesday);
        } else {
            res = false;
            reset();
        }
        if (c_wednesday.isEmpty()) {
            closing_wednesday = -1;
        } else if (validateHour(c_wednesday)) {
            closing_wednesday = toMins(c_wednesday);
        } else {
            res = false;
            reset();
        }

        if (o_thursday.isEmpty()) {
            opening_thursday = -1;
        } else if (validateHour(o_thursday)) {
            opening_thursday = toMins(o_thursday);
        } else {
            res = false;
            reset();
        }
        if (c_thursday.isEmpty()) {
            closing_thursday = -1;
        } else if (validateHour(c_thursday)) {
            closing_thursday = toMins(c_thursday);
        } else {
            res = false;
            reset();
        }

        if (o_friday.isEmpty()) {
            opening_friday = -1;
        } else if (validateHour(o_friday)) {
            opening_friday = toMins(o_friday);
        } else {
            res = false;
            reset();
        }
        if (c_friday.isEmpty()) {
            closing_friday = -1;
        } else if (validateHour(c_friday)) {
            closing_friday = toMins(c_friday);
        } else {
            res = false;
            reset();
        }

        if (o_saturday.isEmpty()) {
            opening_saturday = -1;
        } else if (validateHour(o_saturday)) {
            opening_saturday = toMins(o_saturday);
        } else {
            res = false;
            reset();
        }
        if (c_saturday.isEmpty()) {
            closing_saturday = -1;
        } else if (validateHour(c_saturday)) {
            closing_saturday = toMins(c_saturday);
        } else {
            res = false;
            reset();
        }

        if (o_sunday.isEmpty()) {
            opening_sunday = -1;
        } else if (validateHour(o_sunday)) {
            opening_sunday = toMins(o_sunday);
        } else {
            res = false;
            reset();
        }
        if (c_sunday.isEmpty()) {
            closing_sunday = -1;
        } else if (validateHour(c_sunday)) {
            closing_sunday = toMins(c_sunday);
        } else {
            res = false;
            reset();
        }

        Log.e("checkViewVal", opening_monday + " - " + closing_monday + " - " + opening_tuesday + " - " + closing_tuesday + " - " + opening_wednesday + " - " + closing_wednesday + " - " + opening_thursday + " - " + closing_thursday + " - " + opening_friday + " - " + closing_friday + " - " + opening_saturday + " - " + closing_saturday + " - " + opening_sunday + " - " + closing_sunday);
        Log.e("checkViewVal", "END, res: " + res);
        return res;
    }

    private void reset() {
        opening_monday = reset_o_monday;
        closing_monday = reset_c_monday;

        opening_tuesday = reset_o_tuesday;
        closing_tuesday = reset_c_tuesday;

        opening_wednesday = reset_o_wednesday;
        closing_wednesday = reset_c_wednesday;

        opening_thursday = reset_o_thursday;
        closing_thursday = reset_c_thursday;

        opening_friday = reset_o_friday;
        closing_friday = reset_c_friday;

        opening_saturday = reset_o_saturday;
        closing_saturday = reset_c_saturday;

        opening_sunday = reset_o_sunday;
        closing_sunday = reset_c_sunday;
    }

    private int toMins(String s) {

        int hours = -1;
        int minutes = 0;

        if (s.substring(1, 1).compareTo("0") == 0) {
            hours = Integer.parseInt(s.substring(1, 2)) * 60;
        } else {
            hours = Integer.parseInt(s.substring(0, 2)) * 60;
        }

        if (s.substring(3, 4).compareTo("0") == 0) {
            minutes = Integer.parseInt(s.substring(4, 5));
        } else {

            minutes = Integer.parseInt(s.substring(3, 5));

        }

        return hours + minutes;
    }

    private boolean validateHour(String time) {
        String regex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

        if (!time.isEmpty() && time.length() == 5) {
            int hours = Integer.parseInt(time.substring(0, 2));
            int minutes = Integer.parseInt(time.substring(3, 5));

            //Log.e("res", Boolean.toString(time.matches(regex)));
            return time.matches(regex) && 0 <= hours && hours <= 23 && 0 <= minutes && minutes <= 59;

        } else return false;
    }

    private void updateViewVal() {
        o_monday = monday_opening_gym_et.getText().toString().trim();
        c_monday = monday_closing_gym_et.getText().toString().trim();
        o_tuesday = tuesday_opening_gym_et.getText().toString().trim();
        c_tuesday = tuesday_closing_gym_et.getText().toString().trim();
        o_wednesday = wednesday_opening_gym_et.getText().toString().trim();
        c_wednesday = wednesday_closing_gym_et.getText().toString().trim();
        o_thursday = thursday_opening_gym_et.getText().toString().trim();
        c_thursday = thursday_closing_gym_et.getText().toString().trim();
        o_friday = friday_opening_gym_et.getText().toString().trim();
        c_friday = friday_opening_gym_et.getText().toString().trim();
        o_saturday = saturday_opening_gym_et.getText().toString().trim();
        c_saturday = saturday_closing_gym_et.getText().toString().trim();
        o_sunday = sunday_opening_gym_et.getText().toString().trim();
        c_sunday = sunday_opening_gym_et.getText().toString().trim();

        Log.e("updateViewVal", "END");
        //Log.e("updateViewVal", o_monday + " - " + c_monday + " - " + o_tuesday + " - " + c_tuesday + " - " + o_wednesday + " - " + c_wednesday + " - " + o_thursday + " - " + c_thursday + " - " + o_friday + " - " + c_friday + " - " + o_saturday + " - " + c_saturday + " - " + o_sunday + " - " + c_sunday);
    }


    //  Get data & Set View
    private void GetHoursData() {
        GymEditHoursActivity.GetGymHoursConnection asyncTaskTrainer = (GymEditHoursActivity.GetGymHoursConnection) new GymEditHoursActivity.GetGymHoursConnection(new GymEditHoursActivity.GetGymHoursConnection.AsyncResponse() {
            @Override
            public void processFinish(final int _opening_monday, int _closing_monday, int _opening_tuesday, int _closing_tuesday, int _opening_wednesday, int _closing_wednesday, int _opening_thursday, int _closing_thursday, int _opening_friday, int _closing_friday, int _opening_saturday, int _closing_saturday, int _opening_sunday, int _closing_sunday) {

                if (_opening_monday == -2 && _opening_tuesday == -2 && _opening_wednesday == -2 && _opening_thursday == -2 && _opening_friday == -2 && _opening_saturday == -2 && _opening_sunday == -2) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymEditHoursActivity.this, "ERRORE CARICAMENTO DATI", Toast.LENGTH_SHORT).show();
                            Intent new_i = new Intent(GymEditHoursActivity.this, LoginActivity.class);
                            startActivity(new_i);
                            finish();
                        }
                    });
                } else if (_opening_monday != -2 || _opening_tuesday != -2 || _opening_wednesday != -2 || _opening_thursday != -2 || _opening_friday != -2 || _opening_saturday != -2 || _opening_sunday != -2) {
                    //  -   SETTO VIEW grazie ai dati ricevuti
                    if (_opening_monday != -2) {
                        opening_monday = _opening_monday;
                        closing_monday = _closing_monday;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                String _op_h = minToString(opening_monday);
                                String _cl_h = minToString(closing_monday);
                                monday_opening_gym_et.setText(_op_h);
                                monday_closing_gym_et.setText(_cl_h);
                            }
                        });
                    }
                    if (_opening_tuesday != -2) {
                        opening_tuesday = _opening_tuesday;
                        closing_tuesday = _closing_tuesday;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                String _op_h = minToString(opening_tuesday);
                                String _cl_h = minToString(closing_tuesday);
                                tuesday_opening_gym_et.setText(_op_h);
                                tuesday_closing_gym_et.setText(_cl_h);
                            }
                        });
                    }
                    if (_opening_wednesday != -2) {
                        opening_wednesday = _opening_wednesday;
                        closing_wednesday = _closing_wednesday;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                String _op_h = minToString(opening_wednesday);
                                String _cl_h = minToString(closing_wednesday);
                                wednesday_opening_gym_et.setText(_op_h);
                                wednesday_closing_gym_et.setText(_cl_h);
                            }
                        });
                    }
                    if (_opening_thursday != -2) {
                        opening_thursday = _opening_thursday;
                        closing_thursday = _closing_thursday;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                String _op_h = minToString(opening_thursday);
                                String _cl_h = minToString(closing_thursday);
                                thursday_opening_gym_et.setText(_op_h);
                                thursday_closing_gym_et.setText(_cl_h);
                            }
                        });
                    }
                    if (_opening_friday != -2) {
                        opening_friday = _opening_friday;
                        closing_friday = _closing_friday;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                String _op_h = minToString(opening_friday);
                                String _cl_h = minToString(closing_friday);
                                friday_opening_gym_et.setText(_op_h);
                                friday_closing_gym_et.setText(_cl_h);
                            }
                        });
                    }
                    if (_opening_saturday != -2) {
                        opening_saturday = _opening_saturday;
                        closing_saturday = _closing_saturday;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                String _op_h = minToString(opening_saturday);
                                String _cl_h = minToString(closing_saturday);
                                saturday_opening_gym_et.setText(_op_h);
                                saturday_closing_gym_et.setText(_cl_h);
                            }
                        });
                    }
                    if (_opening_sunday != -2) {
                        opening_sunday = _opening_sunday;
                        closing_sunday = _closing_sunday;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                String _op_h = minToString(opening_sunday);
                                String _cl_h = minToString(closing_sunday);
                                sunday_opening_gym_et.setText(_op_h);
                                sunday_closing_gym_et.setText(_cl_h);
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymEditHoursActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).execute(String.valueOf(user_id));
    }

    private static class GetGymHoursConnection extends AsyncTask<String, String, JsonArray> {
        public interface AsyncResponse {
            void processFinish(int _opening_monday, int _closing_monday, int _opening_tuesday, int _closing_tuesday, int _opening_wednesday, int _closing_wednesday, int _opening_thursday, int _closing_thursday, int _opening_friday, int _closing_friday, int _opening_saturday, int _closing_saturday, int _opening_sunday, int _closing_sunday);
        }

        public GymEditHoursActivity.GetGymHoursConnection.AsyncResponse delegate = null;

        public GetGymHoursConnection(GymEditHoursActivity.GetGymHoursConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected JsonArray doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray orari = null;
            int _opening_monday = -2, _closing_monday = -2, _opening_tuesday = -2, _closing_tuesday = -2;
            int _opening_wednesday = -2, _closing_wednesday = -2, _opening_thursday = -2, _closing_thursday = -2;
            int _opening_friday = -2, _closing_friday = -2, _opening_saturday = -2, _closing_saturday = -2, _opening_sunday = -2, _closing_sunday = -2;

            try {
                url = new URL("http://10.0.2.2:4000/gym/get_hours/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    //Log.e("Server customer", responseString);
                    orari = JsonParser.parseString(responseString).getAsJsonArray();
                    //Log.e("ARRAY", orari.toString());
                    for (int i = 0; i < orari.size(); i++) {
                        JsonObject giorno = (JsonObject) orari.get(i);
                        //Log.e("Giorno " + i, giorno.toString());
                        Integer day_id = giorno.get("day").getAsInt();
                        Integer open_h, close_h;
                        open_h = giorno.get("open").getAsInt();
                        close_h = giorno.get("close").getAsInt();
                        if (open_h != -1) {
                            //aggiorno valore
                            switch (day_id) {
                                case 1:
                                    _opening_monday = open_h;
                                    _closing_monday = close_h;
                                    break;
                                case 2:
                                    _opening_tuesday = open_h;
                                    _closing_tuesday = close_h;
                                    break;
                                case 3:
                                    _opening_wednesday = open_h;
                                    _closing_wednesday = close_h;
                                    break;
                                case 4:
                                    _opening_thursday = open_h;
                                    _closing_thursday = close_h;
                                    break;
                                case 5:
                                    _opening_friday = open_h;
                                    _closing_friday = close_h;
                                    break;
                                case 6:
                                    _opening_saturday = open_h;
                                    _closing_saturday = close_h;
                                    break;
                                case 7:
                                    _opening_sunday = open_h;
                                    _closing_sunday = close_h;
                                    break;
                                default:
                                    delegate.processFinish(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2);
                            }
                        }
                    }
                    delegate.processFinish(_opening_monday, _closing_monday, _opening_tuesday, _closing_tuesday, _opening_wednesday, _closing_wednesday, _opening_thursday, _closing_thursday, _opening_friday, _closing_friday, _opening_saturday, _closing_saturday, _opening_sunday, _closing_sunday);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return orari;
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

    public String minToString(int mins) {
        int hours = mins / 60; //since both are ints, you get an int
        int minutes = mins % 60;
        String s_h, m_h;

        if (String.valueOf(hours).length() == 1) {
            s_h = "0" + hours;
        } else {
            s_h = String.valueOf(hours);
        }

        if (String.valueOf(minutes).length() == 1) {
            m_h = "0" + minutes;
        } else {
            m_h = String.valueOf(minutes);
        }
        String to_res = s_h + ":" + m_h;
        return to_res;
    }
}