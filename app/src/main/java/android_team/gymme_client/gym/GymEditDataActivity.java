package android_team.gymme_client.gym;

import androidx.appcompat.app.AppCompatActivity;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.nutritionist.NutritionistProfileActivity;
import android_team.gymme_client.signup.GymSignupActivity3;
import android_team.gymme_client.trainer.TrainerProfileActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
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

public class GymEditDataActivity extends AppCompatActivity {

    private int user_id;
    private int pool, box_ring, aerobics, spa, wifi, parking_area, personal_trainer, nutritionist, impedance_balance, courses, showers;

    //region BINDING ELEMENTI VIEW
    @BindView(R.id.gymSwitchSignUpPool)
    Switch _switchSignUpPool;
    @BindView(R.id.gymSwitchSignupBoxRing)
    Switch _switchSignupBoxRing;
    @BindView(R.id.gymSwitchSignupAerobics)
    Switch _switchSignupAerobics;
    @BindView(R.id.gymSwitchSignUpSpa)
    Switch _switchSignUpSpa;
    @BindView(R.id.gymSwitchSignUpWifi)
    Switch _switchSignUpWifi;
    @BindView(R.id.gymSwitchSignUpParking)
    Switch _switchSignUpParking;
    @BindView(R.id.gymSwitchSignUpPersonalTrainer)
    Switch _switchSignUpPersonalTrainer;
    @BindView(R.id.gymSwitchSignUpNutritionist)
    Switch _switchSignUpNutritionist;
    @BindView(R.id.gymSwitchSignUpImpedenceBalance)
    Switch _switchSignUpImpedenceBalance;
    @BindView(R.id.gymSwitchSignUpCourses)
    Switch _switchSignUpCourses;
    @BindView(R.id.gymSwitchSignUpShowers)
    Switch _switchSignUpShowers;
    @BindView(R.id.btn_gym_confirm_data)
    Button _btn_gym_confirm_data;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_edit_data);
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


        //  GetGymData(): caricare la configurazione dei tasti in base ai campi salvati nel DB per la palestra in questione:
        //  -   SCARICO DATI mediante query sul db e lo user_id
        //  -   SETTO VIEW grazie ai dati ricevuti
        GetGymData();

        //  BUTTON AVANTI MENAGEMENT
        _btn_gym_confirm_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  UpdateBooleanData(): update dei dati booleani della palestra.
                UpdateBooleanData();
            }
        });


    }


    //region GYM DATA MANAGMNET

    //  Get data & Set View
    private void GetGymData() {
        //Gym Data
        GymEditDataActivity.GetGymDataConnection asyncTaskTrainer = (GymEditDataActivity.GetGymDataConnection) new GymEditDataActivity.GetGymDataConnection(new GymEditDataActivity.GetGymDataConnection.AsyncResponse() {
            @Override
            public void processFinish(int _pool, int _box_ring, int _aerobics, int _spa, int _wifi, int _parking_area, int _personal_traienr, int _nutritionist, int _impedance_balance, int _courses, int _showers) {

                if (_pool == -1) {
                    Toast.makeText(GymEditDataActivity.this, "ERRORE CARICAMENTO DATI", Toast.LENGTH_SHORT).show();
                    Intent new_i = new Intent(GymEditDataActivity.this, LoginActivity.class);
                    startActivity(new_i);
                    finish();
                } else if (_pool == 1 || _pool == 0) {
                    //  -   SETTO VIEW grazie ai dati ricevuti
                    if (_pool == 1) {
                        pool = _pool;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpPool.setChecked(true);
                            }
                        });
                    }
                    if (_box_ring == 1) {
                        box_ring = _box_ring;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignupBoxRing.setChecked(true);
                            }
                        });
                    }
                    if (_aerobics == 1) {
                        aerobics = _aerobics;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignupAerobics.setChecked(true);
                            }
                        });
                    }
                    if (_spa == 1) {
                        spa = _spa;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpSpa.setChecked(true);
                            }
                        });
                    }
                    if (_wifi == 1) {
                        wifi = _wifi;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpWifi.setChecked(true);
                            }
                        });
                    }
                    if (_parking_area == 1) {
                        parking_area = _parking_area;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpParking.setChecked(true);
                            }
                        });
                    }
                    if (_personal_traienr == 1) {
                        personal_trainer = _personal_traienr;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpPersonalTrainer.setChecked(true);
                            }
                        });
                    }
                    if (_nutritionist == 1) {
                        nutritionist = _nutritionist;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpNutritionist.setChecked(true);
                            }
                        });
                    }
                    if (_impedance_balance == 1) {
                        impedance_balance = _impedance_balance;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpImpedenceBalance.setChecked(true);
                            }
                        });
                    }
                    if (_courses == 1) {
                        courses = _courses;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpCourses.setChecked(true);
                            }
                        });
                    }
                    if (_showers == 1) {
                        showers = _showers;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _switchSignUpShowers.setChecked(true);
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymEditDataActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).execute(String.valueOf(user_id));
    }

    private static class GetGymDataConnection extends AsyncTask<String, String, JsonObject> {
        public interface AsyncResponse {
            void processFinish(int _pool, int _box_ring, int _aerobics, int _spa, int _wifi, int _parking_area, int _personal_traienr, int _nutritionist, int _impedance_balance, int _courses, int _showers);
        }

        public GymEditDataActivity.GetGymDataConnection.AsyncResponse delegate = null;

        public GetGymDataConnection(GymEditDataActivity.GetGymDataConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;

            try {
                url = new URL("http://10.0.2.2:4000/gym/get_boolean_data/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.e("Server customer", responseString);
                    user = JsonParser.parseString(responseString).getAsJsonObject();
                    delegate.processFinish(user.get("pool").getAsInt(), user.get("box_ring").getAsInt(), user.get("aerobics").getAsInt(), user.get("spa").getAsInt(),
                            user.get("wifi").getAsInt(), user.get("parking_area").getAsInt(), user.get("personal_trainer_service").getAsInt(),
                            user.get("nutritionist_service").getAsInt(), user.get("impedance_balance").getAsInt(), user.get("courses").getAsInt(), user.get("showers").getAsInt());

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return user;
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

    // Updating datas
    private void UpdateBooleanData() {
        // AGGIORNO STATO ATTUALE DEGLI SWITCH
        updateFields();

        // METODO-CLASSE PER CHIAMARE LA POST CORRETTA SUL SERVER
        GymEditDataActivity.UpdateBooleanDataConnection asyncTask = (GymEditDataActivity.UpdateBooleanDataConnection) new GymEditDataActivity.UpdateBooleanDataConnection(new GymEditDataActivity.UpdateBooleanDataConnection.AsyncResponse() {

            @Override
            public void processFinish(Integer output) {
                if (output == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymEditDataActivity.this, "SUCCESS, boolan data aggiornati", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GymEditDataActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id), String.valueOf(pool), String.valueOf(box_ring), String.valueOf(aerobics), String.valueOf(spa), String.valueOf(wifi), String.valueOf(parking_area), String.valueOf(personal_trainer),
                String.valueOf(nutritionist), String.valueOf(impedance_balance), String.valueOf(courses), String.valueOf(showers));

    }

    private void updateFields() {
        pool = switchToInt(_switchSignUpPool.isChecked());
        box_ring = switchToInt(_switchSignupBoxRing.isChecked());
        aerobics = switchToInt(_switchSignupAerobics.isChecked());
        spa = switchToInt(_switchSignUpSpa.isChecked());
        wifi = switchToInt(_switchSignUpWifi.isChecked());
        parking_area = switchToInt(_switchSignUpParking.isChecked());
        personal_trainer = switchToInt(_switchSignUpPersonalTrainer.isChecked());
        nutritionist = switchToInt(_switchSignUpNutritionist.isChecked());
        impedance_balance = switchToInt(_switchSignUpImpedenceBalance.isChecked());
        courses = switchToInt(_switchSignUpCourses.isChecked());
        showers = switchToInt(_switchSignUpShowers.isChecked());
    }

    private int switchToInt(boolean checked) {
        return (checked == true) ? 1 : 0;
    }


    public static class UpdateBooleanDataConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public GymEditDataActivity.UpdateBooleanDataConnection.AsyncResponse delegate = null;

        public UpdateBooleanDataConnection(GymEditDataActivity.UpdateBooleanDataConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/gym/update_boolean_data/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("pool", params[1]);
                paramsJson.addProperty("box_ring", params[2]);
                paramsJson.addProperty("aerobics", params[3]);
                paramsJson.addProperty("spa", params[4]);
                paramsJson.addProperty("wifi", params[5]);
                paramsJson.addProperty("parking_area", params[6]);
                paramsJson.addProperty("personal_trainer_service", params[7]);
                paramsJson.addProperty("nutritionist_service", params[8]);
                paramsJson.addProperty("impedance_balance", params[9]);
                paramsJson.addProperty("courses", params[10]);
                paramsJson.addProperty("showers", params[11]);

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
                    Log.e("BOOLEAN DATA", "CAMBIATI SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("BOOLEAN DATA", "Error");
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
    //endregion
}