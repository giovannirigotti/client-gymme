package android_team.gymme_client.customer;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android_team.gymme_client.support.Drawer;

public class CustomerHomeActivity extends AppCompatActivity {

    private int user_id;


    @BindView(R.id.btn_customer_home_profile)
    Button _btn_customer_home_profile;

    @BindView(R.id.btn_customer_home_notification)
    Button _btn_customer_home_notification;

    @BindView(R.id.btn_customer_home_gym)
    Button _btn_customer_home_gym;

    @BindView(R.id.drawer_layout_home_activity)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_toolbar_title)
    TextView main_toolbar_title;

    @BindView(R.id.drawer_home_link)
    LinearLayout drawer_home_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        ButterKnife.bind(this);
        setTitle("HOME CUSTOMER");


        main_toolbar_title.setText("Home");

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

        _btn_customer_home_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Customer Profile Activity");
                Intent i = new Intent(getApplicationContext(), CustomerProfileActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        _btn_customer_home_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Customer Manage Gym Activity");
                Intent i = new Intent(getApplicationContext(), CustomerManageGymActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

          _btn_customer_home_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Customer Manage Gym Activity");
                Intent i = new Intent(getApplicationContext(), CustomerNotificationActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });


        drawer_home_link.setPadding(20, 10, 20, 10);
        drawer_home_link.setBackground(getDrawable(R.drawable.rounded_rectangle));
        drawer_home_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
    }



    ////Gestione Drawer

    public void ClickMenu(View view) {
        Drawer.openDrawer(drawerLayout);
    }

    public void ClickDrawer(View view) {
        Drawer.closeDrawer(drawerLayout);
    }

    public void ToTrainings(View view) {
        redirectActivity(this, CustomerTrainingSheetsActivity.class);
    }

    public void redirectActivity(Activity a, Class c){

        Intent i = new Intent(a,c);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
        a.startActivity(i);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Drawer.closeDrawer(drawerLayout);
    }
}
