package android_team.gymme_client.trainer.menage_trainig_sheet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;

public class TrainerCreateDaysActivity extends AppCompatActivity {

    private int user_id, days, sheet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_create_days);

        //region CHET INTENT EXTRAS
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
        if (!i.hasExtra("days")) {
            Toast.makeText(this, "days mancanti", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            days = i.getIntExtra("days", -1);
            Log.w("days ricevuti:", String.valueOf(days));
            if (days == -1) {
                Toast.makeText(this, "days passato male.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }
        if (!i.hasExtra("sheet_id")) {
            Toast.makeText(this, "sheet_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            sheet_id = i.getIntExtra("sheet_id", -1);
            Log.w("sheet_id ricevuto:", String.valueOf(sheet_id));
            if (sheet_id == -1) {
                Toast.makeText(this, "sheet_id passato male.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }

        //endregion

        Log.e("user_id", String.valueOf(user_id));
        Log.e("days", String.valueOf(days));
        Log.e("sheet_id", String.valueOf(sheet_id));

    }
}