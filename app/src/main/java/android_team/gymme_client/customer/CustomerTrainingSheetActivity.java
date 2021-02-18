package android_team.gymme_client.customer;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android_team.gymme_client.R;
import android_team.gymme_client.support.BasicActivity;
import android_team.gymme_client.support.Drawer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerTrainingSheetActivity extends BasicActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String training_sheet_id;
    String user_id;
    public static JsonObject trainingSheet = new JsonObject();
    @BindView(R.id.customerTrainingSheetBottomNavigation)
    BottomNavigationView customerTrainingSheetBottomNavigation;
    @BindView(R.id.customerTrainingSheetFragmentContainer)
    FrameLayout customerTrainingSheetFragmentContainer;
    @BindView(R.id.customerTrainingSheetActivitySpinner)
    ProgressBar customerTrainingSheetActivitySpinner;
    @BindView(R.id.drawer_trainings_link)
    LinearLayout drawer_trainings_link;
    DrawerCustomerListener drawerCustomerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_training_sheet);
        ButterKnife.bind(this);
        user_id = getIntent().getStringExtra("user_id");
        training_sheet_id = getIntent().getStringExtra("training_sheet_id");
        BottomNavigationView navigation = customerTrainingSheetBottomNavigation;
        navigation.setOnNavigationItemSelectedListener(this);
        getTrainingSheet(0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home_activity);
        drawerCustomerListener = new DrawerCustomerListener(this, Integer.parseInt(user_id));
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("Allenamento");

        drawer_trainings_link.setPadding(20, 10, 20, 10);
        drawer_trainings_link.setBackground(getDrawable(R.drawable.rounded_rectangle));
        drawer_trainings_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigationTrainingSheetCalendar:
                fragment = new CustomerTrainingSheetCalendarFragment();
                break;
            case R.id.navigationTrainingSheetDetails:
                fragment = new CustomerTrainigSheetDetailsFragment();
                break;
        }
        return loadFragment(fragment);
    }

    public boolean loadFragment(Fragment fragment) {
        Bundle args = new Bundle();
        args.putString("training_sheet", trainingSheet.toString());
        args.putString("training_sheet_id", training_sheet_id);
        args.putString("customer_id", user_id);
        fragment.setArguments(args);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.customerTrainingSheetFragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public void getTrainingSheet(int fragment) {
        CustomerTrainingSheetActivity.GetTrainingSheet asyncTask = (CustomerTrainingSheetActivity.GetTrainingSheet) new CustomerTrainingSheetActivity.GetTrainingSheet(this, customerTrainingSheetBottomNavigation, customerTrainingSheetFragmentContainer, customerTrainingSheetActivitySpinner, fragment, new CustomerTrainingSheetActivity.GetTrainingSheet.AsyncResponse() {

            @Override
            public void processFinish(Integer output) {
                if (output == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //Toast.makeText(CustomerTrainingSheetActivity.this, "SUCCESS, boolan data aggiornati", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else if (output == 404) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerTrainingSheetActivity.this, "Non hai nessuna scheda di allenamento", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerTrainingSheetActivity.this, "Errore sul server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(training_sheet_id);


        //new GetTrainingSheet(this, customerTrainingSheetBottomNavigation, customerTrainingSheetFragmentContainer, customerTrainingSheetActivitySpinner, fragment).execute(training_sheet_id);
    }


    private static class GetTrainingSheet extends AsyncTask<String, String, JsonObject> {

        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public CustomerTrainingSheetActivity.GetTrainingSheet.AsyncResponse delegate = null;

        CustomerTrainingSheetActivity activity;
        BottomNavigationView bnv;
        FrameLayout fl;
        ProgressBar spinner;
        JsonObject training_sheet;
        public int responseCode, fragment;

        public GetTrainingSheet(Activity activity, BottomNavigationView bnv, FrameLayout fl, ProgressBar spinner, int fragment, CustomerTrainingSheetActivity.GetTrainingSheet.AsyncResponse delegate) {
            this.delegate = delegate;
            this.activity = (CustomerTrainingSheetActivity) activity;
            this.bnv = bnv;
            this.fl = fl;
            this.spinner = spinner;
            this.fragment = fragment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bnv.setVisibility(View.GONE);
            fl.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://10.0.2.2:4000/trainer/get_training_sheet/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();

                //Log.e("Codice risposta", Integer.toString(responseCode));

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    //Log.e("Server tr sheet", responseString);
                    training_sheet = JsonParser.parseString(responseString).getAsJsonObject();
                    CustomerTrainingSheetActivity.trainingSheet = new JsonObject();
                    CustomerTrainingSheetActivity.trainingSheet = training_sheet;
                    //Log.e("async tr", CustomerTrainingSheetActivity.trainingSheet.toString());


                    delegate.processFinish(200);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    //Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish(404);
                }

            } catch (IOException e) {
                e.printStackTrace();
                delegate.processFinish(69);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return training_sheet;
        }

        @Override
        protected void onPostExecute(JsonObject training_sheet) {
            //Log.e("final response", Integer.toString(responseCode));
            if (responseCode == 200) {
                spinner.setVisibility(View.GONE);
                bnv.setVisibility(View.VISIBLE);
                fl.setVisibility(View.VISIBLE);
                if (fragment == 0) {
                    activity.loadFragment(new CustomerTrainingSheetCalendarFragment());
                } else if (fragment == 1) {
                    activity.loadFragment(new CustomerTrainigSheetDetailsFragment());
                }
            } else {
                Toast.makeText(activity, "Errore", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
                bnv.setVisibility(View.VISIBLE);
                fl.setVisibility(View.VISIBLE);
                activity.finish();
            }
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

}