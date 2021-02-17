package android_team.gymme_client.trainer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.manage_training_sheet.TrainerMenageTrainingSheet;

public class TrainerHomeActivity extends AppCompatActivity {

    Button btn_profile, btn_training_sheet;

    private int user_id;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_home);
        setTitle("HOME TRAINER");

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

        btn_profile = (Button) findViewById(R.id.btn_trainer_home_profile);
        btn_training_sheet = (Button) findViewById(R.id.btn_trainer_training_sheet);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("Home");

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Trainer Profile Activity");
                Intent i = new Intent(getApplicationContext(), TrainerProfileActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        btn_training_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Trainer Training Sheet Activity");
                Intent i = new Intent(getApplicationContext(), TrainerMenageTrainingSheet.class);
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

    public void trainerToTrainingSheet(View view) {
        drawerTrainerListener.toTrainingSheet();
    }

    public void trainerToProfile(View view) {
        drawerTrainerListener.toProfile();
    }

    public void trainerToHome(View view) {
        drawerTrainerListener.toHome();
    }
    //endregion

}
