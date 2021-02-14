package android_team.gymme_client.trainer.menage_trainig_sheet;

import android_team.gymme_client.support.MyApplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import android_team.gymme_client.R;
import android_team.gymme_client.customer.CustomerSmallObject;
import android_team.gymme_client.gym.DrawerGymListener;
import android_team.gymme_client.gym.menage_customer.CustomGymCustomerAdapter;
import android_team.gymme_client.gym.menage_customer.GymCustomersActivity;
import android_team.gymme_client.gym.menage_worker.CustomGymTrainerAdapter;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.DrawerTrainerListener;
import android_team.gymme_client.trainer.TrainerObject;

public class TrainerMenageTrainingSheet extends AppCompatActivity {

    private static int user_id;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    public static ArrayList<CustomerSmallObject> customer_list;
    static CustomTrainerCustomerAdapter customer_adapter;
    static ListView lv_customer;
    EditText inputSearch;

    public static int getTrainerId() {
        return user_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_new_training_sheet);

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

        lv_customer = (ListView) findViewById(R.id.lv_trainer_customer);
        inputSearch = (EditText) findViewById(R.id.et_search_trainer_customer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("SCHEDE ALLENAMENTI");

        getTrainerCustomer();

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                TrainerMenageTrainingSheet.this.customer_adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    public static ArrayList<CustomerSmallObject> getAllCustomers() {
        return customer_list;
    }

    private void getTrainerCustomer() {
        TrainerMenageTrainingSheet.ReceiveCustomerConnection asyncTaskUser = (TrainerMenageTrainingSheet.ReceiveCustomerConnection) new TrainerMenageTrainingSheet.ReceiveCustomerConnection(new TrainerMenageTrainingSheet.ReceiveCustomerConnection.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<CustomerSmallObject> customers) {
                customer_list = customers;
                if (customer_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            customer_adapter = new CustomTrainerCustomerAdapter(TrainerMenageTrainingSheet.this, customer_list);
                            lv_customer.setAdapter(customer_adapter);

                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TrainerMenageTrainingSheet.this, "Nessun cliente nella tua palestra", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }

    private static class ReceiveCustomerConnection extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<CustomerSmallObject> customers);
        }

        public TrainerMenageTrainingSheet.ReceiveCustomerConnection.AsyncResponse delegate = null;

        public ReceiveCustomerConnection(TrainerMenageTrainingSheet.ReceiveCustomerConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray _customers = null;
            ArrayList<CustomerSmallObject> t_objects = new ArrayList<CustomerSmallObject>();

            try {
                url = new URL("http://10.0.2.2:4000/trainer/get_customers/" + params[0]);
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

                        String user_id = customer.get("user_id").getAsString().trim();
                        String name = customer.get("name").getAsString().trim();
                        String lastname = customer.get("lastname").getAsString().trim();
                        String email = customer.get("email").getAsString().trim();
                        String birthdate = customer.get("birthdate").getAsString().trim();

                        CustomerSmallObject t_obj = new CustomerSmallObject(user_id, name, lastname, email, birthdate);
                        t_objects.add(t_obj);

                    }
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(t_objects);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("GET CUSTOMERS", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<CustomerSmallObject>());
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