package android_team.gymme_client.gym;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.menage_course.GymCourseActivity;
import android_team.gymme_client.gym.menage_customer.GymCustomersActivity;
import android_team.gymme_client.gym.menage_profile.GymProfileActivity;
import android_team.gymme_client.gym.menage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;

public class GymHomeActivity extends AppCompatActivity {


    DrawerListener drawerListener;
    DrawerLayout drawerLayout;
    Button btn_profile, btn_corsi, btn_gestione_dipendenti, btn_gestione_clienti;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_home);
        setTitle("HOME GYM");

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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_gym_home_activity);
        drawerListener = new DrawerListener(this, user_id);

        btn_profile = (Button) findViewById(R.id.btn_gym_home_profile);
        btn_corsi = (Button) findViewById(R.id.btn_gym_home_corsi);
        btn_gestione_dipendenti = (Button) findViewById(R.id.btn_gym_home_gestione_dipendenti);
        btn_gestione_clienti = (Button) findViewById(R.id.btn_gym_home_dipendenti);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Profile Activity");
                Intent i = new Intent(getApplicationContext(), GymProfileActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        btn_corsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Courses Activity");
                Intent i = new Intent(getApplicationContext(), GymCourseActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        btn_gestione_dipendenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Megagment Worker Activity");
                Intent i = new Intent(getApplicationContext(), GymMenageWorkerActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        btn_gestione_clienti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Customers Activity");
                Intent i = new Intent(getApplicationContext(), GymCustomersActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

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

    public void gymToCorsi(View view){
        drawerListener.toCourse();
    }
    public void gymToClienti(View view){
        drawerListener.toCustomer();
    }
    public void gymToDipendenti(View view){
        drawerListener.toEmployees();
    }
    public void gymToProfilo(View view){
        drawerListener.toProfile();
    }
    public void gymToHome(View view){
        drawerListener.toHome();
    }
    //endregion




}
