package android_team.gymme_client.nutritionist;

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
import android_team.gymme_client.trainer.TrainerProfileActivity;

public class NutritionistHomeActivity extends AppCompatActivity {

    Button btn_profile;
    private int user_id;

    DrawerNutriListener drawerNutriListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritionist_home);
        setTitle("HOME NUTRITIONIST");

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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_nutri_activity);
        drawerNutriListener = new DrawerNutriListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("HOME");

        btn_profile = (Button) findViewById(R.id.btn_nutri_profile);

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Trainer Profile Activity");
                Intent i = new Intent(getApplicationContext(), NutritionistProfileActivity.class);
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

    public void nutriToProfile(View view){
        drawerNutriListener.toProfile();
    }
    public void nutriToHome(View view){
        drawerNutriListener.toHome();
    }
    //endregion

}
