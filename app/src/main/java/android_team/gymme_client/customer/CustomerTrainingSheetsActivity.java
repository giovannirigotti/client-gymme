package android_team.gymme_client.customer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
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
import android_team.gymme_client.local_database.local_dbmanager.DBManagerUser;
import android_team.gymme_client.support.Drawer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerTrainingSheetsActivity extends AppCompatActivity {

    private int user_id=-1;
    public RecyclerView.Adapter adapter;


    @BindView(R.id.main_toolbar_title)
    TextView main_toolbar_title;

    @BindView(R.id.drawer_trainings_link)
    LinearLayout drawer_trainings_link;

    @BindView(R.id.recycler_training_sheets_customer)
    RecyclerView recyclerView;
    @BindView(R.id.spinner_customer_training_sheets)
    ProgressBar spinner_customer_training_sheets;

    DrawerCustomerListener drawerCustomerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_training_sheets_list);
        ButterKnife.bind(this);

        main_toolbar_title.setText("Allenamenti");



        drawer_trainings_link.setPadding(20, 10, 20, 10);
        drawer_trainings_link.setBackground(getDrawable(R.drawable.rounded_rectangle));
        drawer_trainings_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        DBManagerUser dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();
        Cursor cursor = dbManagerUser.fetch();
        user_id = cursor.getInt(cursor.getColumnIndex("id"));


        if(user_id!=-1) {

            spinner_customer_training_sheets.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            new GetTrainingSheets(this, recyclerView, adapter, spinner_customer_training_sheets).execute(Integer.toString(user_id));


        } else {
            Toast.makeText(this,"Errore nel recuperare l'id utente!", Toast.LENGTH_LONG).show();
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home_activity);
        drawerCustomerListener = new DrawerCustomerListener (this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("I TUOI ALLENAMENTI");
    }



    private static class GetTrainingSheets extends AsyncTask<String, String,Integer> {
        Activity activity;
        RecyclerView.Adapter adapter;
        RecyclerView recyclerView;
        ProgressBar spinner;
        JsonArray trainig_sheets = null;


        public GetTrainingSheets(Activity activity, RecyclerView recyclerView, RecyclerView.Adapter adapter, ProgressBar spinner){
            this.activity=activity;
            this.recyclerView=recyclerView;
            this.adapter=adapter;
            this.spinner= spinner;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            spinner.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://10.0.2.2:4000/trainer/get_training_sheets/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.e("Server tr sheets", responseString);
                    trainig_sheets = JsonParser.parseString(responseString).getAsJsonArray();

                    return 1;
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    return -1;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer i) {

            ///rimuovere spinner e far apparire la lista

            if(trainig_sheets!=null) {
                spinner.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                adapter = new ListTrainingSheetsAdapter(trainig_sheets, activity);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            } else {
                Toast.makeText(activity, "Errore", Toast.LENGTH_LONG).show();
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

    public void customerToNotify(View view){
        drawerCustomerListener.toNotify();
    }
    public void customerToTrainings(View view){
        drawerCustomerListener.toTrainings();
    }
    public void customerToGym(View view){
        drawerCustomerListener.toGym();
    }
    public void customerToCourse(View view){
        drawerCustomerListener.toCourse();
    }
    public void customerToProfile(View view){
        drawerCustomerListener.toProfile();
    }
    public void customerToHome(View view){
        drawerCustomerListener.toHome();
    }
//endregion
}
