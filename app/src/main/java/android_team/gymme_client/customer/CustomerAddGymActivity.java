package android_team.gymme_client.customer;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.GymObject;
import android_team.gymme_client.login.LoginActivity;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
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

public class CustomerAddGymActivity extends AppCompatActivity {

    private int user_id;
    static CustomCustomerGymAdapter gym_adapter;

    ListView lv_gym;

    public static ArrayList<GymObject> gym_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_gym);

        gym_list = new ArrayList<GymObject>();

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
        lv_gym = (ListView) findViewById(R.id.lv_customer_disponible_gym);
        getGym();

    }


    private void getGym() {
        CustomerAddGymActivity.ReceiveGymConnection asyncTaskUser = (CustomerAddGymActivity.ReceiveGymConnection) new CustomerAddGymActivity.ReceiveGymConnection(new CustomerAddGymActivity.ReceiveGymConnection.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<GymObject> gyms) {

                gym_list = gyms;
                if (gym_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista delle gym da visualizzare nella recycler view(notificationView)
                            gym_adapter = new CustomCustomerGymAdapter(CustomerAddGymActivity.this, gym_list);
                            lv_gym.setAdapter(gym_adapter);

                            lv_gym.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //cancello notifica su db
                                    ViewGym(CustomerAddGymActivity.this, user_id, gym_list.get(i).user_id, gym_list.get(i));
                                    //Toast.makeText(CustomerAddGymActivity.this, "Elemento: " + i + "; testo: " + gym_list.get(i).gym_name, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NON C'è NESSUNA GYM DISPONIBILE
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerAddGymActivity.this, "Nessuna gym", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }

    private static class ReceiveGymConnection extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<GymObject> gyms);
        }

        public CustomerAddGymActivity.ReceiveGymConnection.AsyncResponse delegate = null;

        public ReceiveGymConnection(CustomerAddGymActivity.ReceiveGymConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray _gyms = null;
            ArrayList<GymObject> g_objects = new ArrayList<GymObject>();

            try {
                url = new URL("http://10.0.2.2:4000/customer/get_disponible_gym_customers/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    _gyms = JsonParser.parseString(responseString).getAsJsonArray();

                    for (int i = 0; i < _gyms.size(); i++) {
                        JsonObject gym = (JsonObject) _gyms.get(i);

                        String user_id = gym.get("user_id").getAsString().trim();
                        String vat_number = gym.get("vat_number").getAsString().trim();
                        String gym_name = gym.get("gym_name").getAsString().trim();
                        String gym_address = gym.get("gym_address").getAsString().trim();
                        String zip_code = gym.get("zip_code").getAsString().trim();
                        Integer pool = gym.get("pool").getAsInt();
                        Integer box_ring = gym.get("box_ring").getAsInt();
                        Integer aerobics = gym.get("aerobics").getAsInt();
                        Integer spa = gym.get("spa").getAsInt();
                        Integer wifi = gym.get("wifi").getAsInt();
                        Integer parking_area = gym.get("parking_area").getAsInt();
                        Integer personal_trainer_service = gym.get("personal_trainer_service").getAsInt();
                        Integer nutritionist_service = gym.get("nutritionist_service").getAsInt();
                        Integer impedance_balance = gym.get("impedance_balance").getAsInt();
                        Integer courses = gym.get("courses").getAsInt();
                        Integer showers = gym.get("showers").getAsInt();



                        GymObject g_obj = new GymObject(user_id, vat_number, gym_name, gym_address, zip_code, pool, box_ring, aerobics, spa, wifi, parking_area, personal_trainer_service, nutritionist_service, impedance_balance, courses, showers);
                        g_objects.add(g_obj);

                    }
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(g_objects);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("GET GYM", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<GymObject>());
                } else {
                    Log.e("GET GYM", "SERVER ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("GET GYM", "I/O EXCEPTION ERROR");
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return _gyms;
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

    private void ViewGym(Activity a, int user_id, String gym_id, GymObject gym ) {
        Log.e("VIEW GYM", user_id+" "+gym_id);
        CustomerAddGymActivity.CustomDialogViewGymClass cdd = new CustomerAddGymActivity.CustomDialogViewGymClass(a, user_id, gym_id, gym);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogViewGymClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Button Inscription, Esc;
        public TextView name, address;
        public String gym_id;
        public Integer user_id;
        public GymObject gym;

        public CustomDialogViewGymClass(Activity a, Integer user_id, String gym_id, GymObject gym) {
            super(a);
            this.c = a;
            this.user_id = user_id;
            this.gym_id = gym_id;
            this.gym = gym;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_customer_gym_iscription);
            Inscription = (Button) findViewById(R.id.btn_dialog_customer_gym_inscription_inscription);
            Esc = (Button) findViewById(R.id.btn_dialog_customer_gym_inscription_type_no);
            name = (TextView) findViewById(R.id.tv_dialog_customer_gym_inscription_name);
            address = (TextView) findViewById(R.id.tv_dialog_customer_gym_inscription_address);
            Inscription.setOnClickListener(this);
            Esc.setOnClickListener(this);

            name.setText(gym.gym_name);
            address.setText(gym.gym_address);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_dialog_customer_gym_inscription_inscription:
                    dismiss();
                    iscriptionGym(user_id, gym_id);

                    break;
                case R.id.btn_dialog_customer_gym_inscription_type_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }




    }

    private void iscriptionGym(final Integer user_id, String gym_id) {
        CustomerAddGymActivity.InsertGymConnection asyncTaskUser = (CustomerAddGymActivity.InsertGymConnection) new CustomerAddGymActivity.InsertGymConnection(new CustomerAddGymActivity.InsertGymConnection.AsyncResponse() {
            @Override
            public void processFinish(Integer output) {

                if (output==200) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerAddGymActivity.this, "Iscrizione avvenuta", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CustomerAddGymActivity.this, CustomerManageGymActivity.class);
                            intent.putExtra("user_id",user_id);
                            startActivity(intent);
                            finish();

                        }
                    });

                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NON C'è NESSUNA GYM DISPONIBILE
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerAddGymActivity.this, "Errore iscrizione", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id), gym_id);
    }

    private static class InsertGymConnection extends AsyncTask<String, String, Integer> {

        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public CustomerAddGymActivity.InsertGymConnection.AsyncResponse delegate = null;

        public InsertGymConnection(CustomerAddGymActivity.InsertGymConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            int responseCode = 500;

            try {
                url = new URL("http://10.0.2.2:4000/customer/inscription");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("gym_id", params[1]);

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

                    Log.e("Server response", "HTTP_OK");


                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(HttpURLConnection.HTTP_OK);
                } else {
                    Log.e("ISCRIPTION GYM", "SERVER ERROR");
                    delegate.processFinish(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ISCRIPTION GYM", "I/O EXCEPTION ERROR");
                delegate.processFinish(500);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }


    }
    //endregion
}




