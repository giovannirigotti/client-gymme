package android_team.gymme_client.trainer.menage_trainig_sheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.menage_course.GymAddCoursesActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.DrawerTrainerListener;
import android_team.gymme_client.trainer.TrainerProfileActivity;

public class TrainerCreateTrainingSheetActivity extends AppCompatActivity {

    private int user_id, trainer_id;

    DrawerTrainerListener drawerTrainerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    EditText et_title, et_description, et_date, et_days;
    String str_title, str_description, str_date;
    Integer str_days;
    Button btn_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_create_training_sheet);

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

        if (!i.hasExtra("trainer_id")) {
            Toast.makeText(this, "trainer_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            trainer_id = i.getIntExtra("trainer_id", -1);
            Log.w("trainer_id ricevuto:", String.valueOf(trainer_id));
            if (trainer_id == -1) {
                Toast.makeText(this, "Trainer non creato.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_trainer_activity);
        drawerTrainerListener = new DrawerTrainerListener(this, trainer_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("CREA SCHEDA");

        et_title = (EditText) findViewById(R.id.et_create_sheet_title);
        et_description = (EditText) findViewById(R.id.et_create_sheet_description);
        et_date = (EditText) findViewById(R.id.et_create_sheet_date);
        et_days = (EditText) findViewById(R.id.et_create_sheet_days);

        btn_go = (Button) findViewById(R.id.btn_create_sheet_go);

        //MANDO INSERT AL SERVER PER TABELLA trainig_sheets e dati utili per la activity succesiva
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTrainingSheet();
            }
        });


    }

    private void createTrainingSheet() {
        if (checkData()) {

        }
        //ERRORI GESTITI NEI SINGOLI CHECK DI OGNI DATO
    }

    //region CHECK DATA
    private boolean checkData() {

        if(checkTitolo()){
            if(checkDescrizione()){
                if(checkDataCreazione()){
                    if(checkNumber()){
                        Log.e("CHECK", "ALL OK");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkNumber() {
        String n_days = et_days.getText().toString().trim();
        if (n_days.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un numero di giorni!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            try {
                str_days = Integer.parseInt(n_days);
                Log.e("CONVERSIONE NUMERO", "OK");
                if(str_days < 1 || str_days > 7 ){
                    Log.e("CONVERSIONE NUMERO", "ERRORE DIMENSIONE NUMERO");
                    Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un numero di giorni compreso tra 1 e 7!", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            } catch (Exception e) {
                Log.e("CONVERSIONE NUMERO", "ERROR");
                Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un numero di sole cifre numeriche!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean checkDataCreazione() {
        str_date = et_date.getText().toString().trim();
        if (str_date.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci una data di creazione!", Toast.LENGTH_LONG).show();
            return false;
        } else if (!validateDate(str_date)) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "DATA ERRATA.\n   - FORMATO: <gg/mm/AAAA> senza spazi vuoti e inserendo appositi zeri\n   - DATA: dal 2020 al 2200", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTitolo() {
        str_title = et_title.getText().toString().trim();
        if (str_title.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci un titolo!", Toast.LENGTH_LONG).show();
            return false;
        } else if (str_title.length() > 200) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Titolo troppo lunga. MAX 200 caratteri.\nUsati: " + str_title.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "TITOLO OK");
            return true;
        }
    }

    private boolean checkDescrizione() {
        str_description = et_description.getText().toString().trim();
        if (str_description.isEmpty()) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Inserisci una descrizione!", Toast.LENGTH_LONG).show();
            return false;
        } else if (str_description.length() > 2000) {
            Toast.makeText(TrainerCreateTrainingSheetActivity.this, "Descrizione troppo lunga. MAX 2000 caratteri.\nUsati: " + str_description.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "DESCRIZIONE OK");
            return true;
        }

    }

    private boolean validateDate(String birthdate) {
        boolean checkFormat;
        boolean checkNumber;

        if (birthdate.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
            checkFormat = true;
        else
            checkFormat = false;

        String[] t = birthdate.split("/");
        checkNumber = (Integer.valueOf(t[0]) <= 31 && Integer.valueOf(t[1]) <= 12 && Integer.valueOf(t[2]) >= 2020 && Integer.valueOf(t[2]) <= 2200);

        return (checkNumber && checkFormat);
    }
    //endregion

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