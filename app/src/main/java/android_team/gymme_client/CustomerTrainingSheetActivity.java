package android_team.gymme_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android_team.gymme_client.support.BasicActivity;
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


    private static class GetTrainingSheet extends AsyncTask<String, String, JsonObject> {
        CustomerTrainingSheetActivity activity;
        BottomNavigationView bnv;
        FrameLayout fl;
        ProgressBar spinner;
        JsonObject training_sheet;
        public int responseCode, fragment;

        public GetTrainingSheet(Activity activity, BottomNavigationView bnv, FrameLayout fl, ProgressBar spinner, int fragment) {
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
                urlConnection.disconnect();

                Log.e("Codice risposta", Integer.toString(responseCode));

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.e("Server tr sheet", responseString);
                    training_sheet = JsonParser.parseString(responseString).getAsJsonObject();
                    CustomerTrainingSheetActivity.trainingSheet = new JsonObject();
                    CustomerTrainingSheetActivity.trainingSheet = training_sheet;
                    Log.e("async tr", CustomerTrainingSheetActivity.trainingSheet.toString());

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return training_sheet;
        }

        @Override
        protected void onPostExecute(JsonObject training_sheet) {
            Log.e("final response", Integer.toString(responseCode));
            if (responseCode == 200) {
                spinner.setVisibility(View.GONE);
                bnv.setVisibility(View.VISIBLE);
                fl.setVisibility(View.VISIBLE);
                if (fragment == 0) {
                    activity.loadFragment(new CustomerTrainingSheetCalendarFragment());
                } else if(fragment==1){
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

    public void getTrainingSheet(int fragment) {
        new GetTrainingSheet(this, customerTrainingSheetBottomNavigation, customerTrainingSheetFragmentContainer, customerTrainingSheetActivitySpinner, fragment).execute(training_sheet_id);
    }
}