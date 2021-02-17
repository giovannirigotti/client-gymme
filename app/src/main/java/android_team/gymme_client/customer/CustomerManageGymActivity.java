package android_team.gymme_client.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android_team.gymme_client.gym.GymObject;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerManageGymActivity extends AppCompatActivity {

    @BindView(R.id.btn_customer_manage_gym_to_disponible_gym)
    Button _btn_customer_manage_gym_to_disponible_gym;


    private int user_id;
    static CustomCustomerGymAdapter gym_adapter;

    ListView lv_gym;

    public static ArrayList<GymObject> gym_list;
    DrawerCustomerListener drawerCustomerListener;
    DrawerLayout drawerLayout;
    TextView tv_title, no_item_cus_list_gym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manage_gym);
        ButterKnife.bind(this);
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
                Toast.makeText(this, "Utente non creato", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home_activity);
        drawerCustomerListener = new DrawerCustomerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        no_item_cus_list_gym = (TextView) findViewById(R.id.no_item_cus_list_gym);
        tv_title.setText("Palestre");
        lv_gym = (ListView) findViewById(R.id.lv_customer_gym);

        getGym();

        _btn_customer_manage_gym_to_disponible_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Customer Manage Gym Activity");
                Intent i = new Intent(getApplicationContext(), CustomerAddGymActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Log.e("REDIRECT", "Customer Home Activity");
        Intent i = new Intent(getApplicationContext(), CustomerHomeActivity.class);
        i.putExtra("user_id", user_id);
        startActivity(i);
    }

    private void getGym() {
        CustomerManageGymActivity.ReceiveGymConnection asyncTaskUser = (CustomerManageGymActivity.ReceiveGymConnection) new CustomerManageGymActivity.ReceiveGymConnection(new CustomerManageGymActivity.ReceiveGymConnection.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<GymObject> gyms) {

                gym_list = gyms;
                if (gym_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista delle gym da visualizzare nella recycler view(notificationView)
                            gym_adapter = new CustomCustomerGymAdapter(CustomerManageGymActivity.this, gym_list);
                            lv_gym.setAdapter(gym_adapter);

                            lv_gym.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                    Intent intent = new Intent(getApplicationContext(), CustomerMyGymActivity.class);
                                    intent.putExtra("gym_id", Integer.parseInt(gym_list.get(i).user_id));
                                    intent.putExtra("user_id", user_id);
                                    //Log.e("gym_id before",  gym_list.get(i).user_id );
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA GYM LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            no_item_cus_list_gym.setVisibility(View.VISIBLE);
                            Toast.makeText(CustomerManageGymActivity.this, "Nessuna palestra", Toast.LENGTH_SHORT).show();
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

        public CustomerManageGymActivity.ReceiveGymConnection.AsyncResponse delegate = null;

        public ReceiveGymConnection(CustomerManageGymActivity.ReceiveGymConnection.AsyncResponse delegate) {
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
                url = new URL("http://10.0.2.2:4000/customer/get_gym_customers/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    //Log.e("Server response", "HTTP_OK");
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
                    //Log.e("GET GYM", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<GymObject>());
                } else {
                    //Log.e("GET GYM", "SERVER ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
                //Log.e("GET GYM", "I/O EXCEPTION ERROR");
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

    public void customerToNotify(View view) {
        drawerCustomerListener.toNotify();
    }

    public void customerToTrainings(View view) {
        drawerCustomerListener.toTrainings();
    }

    public void customerToGym(View view) {
        drawerCustomerListener.toGym();
    }

    public void customerToCourse(View view) {
        drawerCustomerListener.toCourse();
    }

    public void customerToProfile(View view) {
        drawerCustomerListener.toProfile();
    }

    public void customerToHome(View view) {
        drawerCustomerListener.toHome();
    }
//endregion
}




