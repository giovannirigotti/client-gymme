package android_team.gymme_client.gym;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

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
        boolean is_ok = checkViewVal();
        if(is_ok){
            //UpdateDataOnDBWW
        }
        else{
            //SE ERRORE NEGLI INSERIMENTI
            Toast.makeText(getApplicationContext(), "Errore immissione dati:\n" +
                    " - FORMATO: <hh:mm>\n" +
                    " - INSERISCI COPPIA: sia apertura che chiusura per ogni giorno\n" +
                    " - ALTRO ERRORE", Toast.LENGTH_LONG).show();
        }


    }

    private boolean checkViewVal() {
        boolean res = true;
        
        if (!o_monday.isEmpty() && !(validateHour(o_monday)))  res = false;
        if (!c_monday.isEmpty() && !(validateHour(c_monday)))  res = false;

        if (!o_tuesday.isEmpty() && !(validateHour(o_tuesday)))  res = false;
        if (!c_tuesday.isEmpty() && !(validateHour(c_tuesday)))  res = false;

        if (!o_wednesday.isEmpty() && !(validateHour(o_wednesday)))  res = false;
        if (!c_wednesday.isEmpty() && !(validateHour(c_wednesday)))  res = false;

        if (!o_thursday.isEmpty() && !(validateHour(o_thursday)))  res = false;
        if (!c_thursday.isEmpty() && !(validateHour(c_thursday)))  res = false;

        if (!o_friday.isEmpty() && !(validateHour(o_friday)))  res = false;
        if (!c_friday.isEmpty() && !(validateHour(c_friday)))  res = false;

        if (!o_saturday.isEmpty() && !(validateHour(o_saturday)))  res = false;
        if (!c_saturday.isEmpty() && !(validateHour(c_saturday)))  res = false;

        if (!o_sunday.isEmpty() && !(validateHour(o_sunday)))  res = false;
        if (!c_sunday.isEmpty() && !(validateHour(c_sunday)))  res = false;

        Log.e("checkViewVal", "END, res: "+ res);
        return res;
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