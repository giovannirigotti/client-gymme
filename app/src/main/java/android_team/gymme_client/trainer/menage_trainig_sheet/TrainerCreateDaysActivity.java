package android_team.gymme_client.trainer.menage_trainig_sheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.DrawerTrainerListener;

public class TrainerCreateDaysActivity extends AppCompatActivity {

    private int days, sheet_id;
    private static int user_id;

    private static int DAY_POSITION;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    private ArrayList<Integer> day_list;
    static CustomCreateDayAdapter day_adapter;
    static ListView lv_day;

    private static ArrayList<Boolean> day_check;

    private Button btn_end_create_day;

    public static Integer getUserId() {
        return user_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_create_days);

        btn_end_create_day = (Button) findViewById(R.id.btn_end_create_day);
        lv_day = (ListView) findViewById(R.id.lv_create_days);

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

        day_list = new ArrayList<>();
        for(int k = 1; k <= days; k++){
            day_list.add(k);
        }
        day_check = new ArrayList<>();
        for(int k = 1; k <= days; k++){
            day_check.add(false);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("GIORNI SCHEDA");

        day_adapter = new CustomCreateDayAdapter(TrainerCreateDaysActivity.this, day_list, sheet_id);
        lv_day.setAdapter(day_adapter);



        btn_end_create_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllDays()){
                    //TODO VADO ALLA ACTIVITY DELLE SCHEDE e INVIO NOTIFICA
                }
                else{
                    Toast.makeText(TrainerCreateDaysActivity.this, "Completa l'inserimento di tutti i giorni", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static void updatePosition(Integer position) {
        DAY_POSITION = position;
    }

    private boolean checkAllDays() {
        boolean res = true;
        for (int i = 0; i < days; i++){
            if(day_check.get(i) == false){
                res = false;
            }
        }
        return res;
    }

    public static void blockButton() {
        lv_day.getChildAt(DAY_POSITION).findViewById(R.id.btn_create_day_plus).setEnabled(false);
        TextView to_edit = (TextView) lv_day.getChildAt(DAY_POSITION).findViewById(R.id.tv_create_day_text);
        to_edit.setText("COMPLETATO");
    }

    public static void checkDayOK(){
        day_check.set(DAY_POSITION, true);
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

    public void trainerToTrainingSheet(View view){
        drawerTrainerListener.toTrainingSheet();
    }
    public void trainerToProfile(View view){
        drawerTrainerListener.toProfile();
    }
    public void trainerToHome(View view){
        drawerTrainerListener.toHome();
    }
    //endregion
}