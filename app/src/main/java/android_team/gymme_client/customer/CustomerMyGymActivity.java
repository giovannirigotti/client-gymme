package android_team.gymme_client.customer;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.GymEditDataActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.signup.SignupActivity;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomerMyGymActivity extends AppCompatActivity {
    @BindView(R.id.tv_customer_my_gym_name)
    TextView _tv_customer_my_gym_name;
    @BindView(R.id.tv_customer_my_gym_address)
    TextView _tv_customer_my_gym_address;
    @BindView(R.id.tv_customer_my_gym_vat_number)
    TextView _tv_customer_my_gym_vat_number;
    @BindView(R.id.tv_customer_my_gym_zip_code)
    TextView _tv_customer_my_gym_zip_code;
    @BindView(R.id.tv_customer_my_gym_pool)
    TextView _tv_customer_my_gym_pool;
    @BindView(R.id.tv_customer_my_gym_box_ring)
    TextView _tv_customer_my_gym_box_ring;
    @BindView(R.id.tv_customer_my_gym_aerobics)
    TextView _tv_customer_my_gym_aerobics;
    @BindView(R.id.tv_customer_my_gym_spa)
    TextView _tv_customer_my_gym_spa;
    @BindView(R.id.tv_customer_my_gym_wifi)
    TextView _tv_customer_my_gym_wifi;
    @BindView(R.id.tv_customer_my_gym_parkin_area)
    TextView _tv_customer_my_gym_parkin_area;
    @BindView(R.id.tv_customer_my_gym_personal_trainer_service)
    TextView _tv_customer_my_gym_personal_trainer_service;
    @BindView(R.id.tv_customer_my_gym_nutritionist_service)
    TextView _tv_customer_my_gym_nutritionist_service;
    @BindView(R.id.tv_customer_my_gym_impedance_balance)
    TextView _tv_customer_my_gym_impedance_balance;
    @BindView(R.id.tv_customer_my_gym_courses)
    TextView _tv_customer_my_gym_courses;
    @BindView(R.id.tv_customer_my_gym_showers)
    TextView _tv_customer_my_gym_showers;

    @BindView(R.id.check1)
    ImageView _check1;
    @BindView(R.id.check2)
    ImageView _check2;
    @BindView(R.id.check3)
    ImageView _check3;
    @BindView(R.id.check4)
    ImageView _check4;
    @BindView(R.id.check5)
    ImageView _check5;
    @BindView(R.id.check6)
    ImageView _check6;
    @BindView(R.id.check7)
    ImageView _check7;
    @BindView(R.id.check8)
    ImageView _check8;
    @BindView(R.id.check9)
    ImageView _check9;
    @BindView(R.id.check10)
    ImageView _check10;
    @BindView(R.id.check11)
    ImageView _check11;

    Integer gym_id;
    private int user_id, pool, box_ring, aerobics, spa, wifi, parking_area, personal_trainer, nutritionist, impedance_balance, courses, showers;
    private String vat_number, gym_name, gym_address, gym_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_my_gym);
        ButterKnife.bind(this);

        Intent i = getIntent();
        if (!i.hasExtra("user_id")) {
            Toast.makeText(this, "user_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            gym_id = i.getIntExtra("user_id", -1);
            if (gym_id == -1) {
                Toast.makeText(this, "Gym non creata.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }




        GetGymData();
    }

    private void GetGymData() {
        CustomerMyGymActivity.GetGymDataConnection asyncTaskTrainer = (CustomerMyGymActivity.GetGymDataConnection) new CustomerMyGymActivity.GetGymDataConnection(new CustomerMyGymActivity.GetGymDataConnection.AsyncResponse() {
            @Override
            public void processFinish(int _user_id, String _vat_number,  String _gym_name, String _gym_address, String _zip_code, int _pool, int _box_ring, int _aerobics, int _spa, int _wifi, int _parking_area, int _personal_trainer, int _nutritionist, int _impedance_balance, int _courses, int _showers) {

                if (_pool == -1) {
                    //Toast.makeText(CustomerMyGymActivity.this, "ERRORE CARICAMENTO DATI", Toast.LENGTH_SHORT).show();
                    Intent new_i = new Intent(CustomerMyGymActivity.this, LoginActivity.class);
                    startActivity(new_i);
                    finish();
                } else if (_pool == 1 || _pool == 0) {
                    //  -   SETTO VIEW grazie ai dati ricevuti
                    _tv_customer_my_gym_name.setText(_gym_name);
                    _tv_customer_my_gym_address.setText(_gym_address);
                    _tv_customer_my_gym_zip_code.setText(_zip_code);
                    _tv_customer_my_gym_vat_number.setText(_vat_number);
                    if (_pool == 1) {
                        pool = _pool;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check1.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_box_ring == 1) {
                        box_ring = _box_ring;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check2.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_aerobics == 1) {
                        aerobics = _aerobics;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check3.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_spa == 1) {
                        spa = _spa;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check4.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_wifi == 1) {
                        wifi = _wifi;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check5.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_parking_area == 1) {
                        parking_area = _parking_area;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check6.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_personal_trainer == 1) {
                        personal_trainer = _personal_trainer;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check7.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_nutritionist == 1) {
                        nutritionist = _nutritionist;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check8.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_impedance_balance == 1) {
                        impedance_balance = _impedance_balance;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check9.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_courses == 1) {
                        courses = _courses;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check10.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                    if (_showers == 1) {
                        showers = _showers;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //SETTO VIEW grazie ai dati ricevuti
                                _check11.setImageResource(R.drawable.ic_check);
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerMyGymActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).execute(String.valueOf(gym_id));
    }


    private static class GetGymDataConnection extends AsyncTask<String, String, JsonObject> {
        public interface AsyncResponse {
            void processFinish(int _user_id, String _vat_number, String _gym_name, String _gym_address, String _zip_code, int _pool, int _box_ring, int _aerobics, int _spa, int _wifi, int _parking_area, int _personal_trainer, int _nutritionist, int _impedance_balance, int _courses, int _showers);
        }

        public CustomerMyGymActivity.GetGymDataConnection.AsyncResponse delegate = null;

        public GetGymDataConnection(CustomerMyGymActivity.GetGymDataConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;

            try {
                url = new URL("http://10.0.2.2:4000/gym/get_all_data/" + params[0]);
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
                    delegate.processFinish(user.get("user_id").getAsInt(), user.get("vat_number").getAsString(), user.get("gym_name").getAsString(), user.get("gym_address").getAsString(), user.get("zip_code").getAsString(), user.get("pool").getAsInt(), user.get("box_ring").getAsInt(), user.get("aerobics").getAsInt(), user.get("spa").getAsInt(),
                            user.get("wifi").getAsInt(), user.get("parking_area").getAsInt(), user.get("personal_trainer_service").getAsInt(),
                            user.get("nutritionist_service").getAsInt(), user.get("impedance_balance").getAsInt(), user.get("courses").getAsInt(), user.get("showers").getAsInt());

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish(-1, "", "","","",-1,-1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
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
}