
package android_team.gymme_client.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;

public class GymMenageWorkerActivity extends AppCompatActivity {

    private int user_id;

    ListView lv_trainer, lv_nutri;
    Button btn_add_trainer, btn_add_nutri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_menage_worker);

        //region CHECK INTENT EXTRAS
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
        //endregion

        lv_trainer = (ListView) findViewById(R.id.lv_menage_trainer);
        lv_nutri = (ListView) findViewById(R.id.lv_menage_nutritionist);
        btn_add_trainer = (Button) findViewById(R.id.btn_menage_trainer);
        btn_add_nutri = (Button) findViewById(R.id.btn_menage_nutritionist);

        /*
        btn_add_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Add Trainer");
                Intent i = new Intent(getApplicationContext(), GymProfileActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        btn_add_nutri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Add Nutri");
                Intent i = new Intent(getApplicationContext(), GymCoursesActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });
         */
    }
}