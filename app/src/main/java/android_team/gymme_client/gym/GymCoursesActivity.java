package android_team.gymme_client.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;

public class GymCoursesActivity extends AppCompatActivity {

    private int gym_id;
    private int user_id;

    private ListView lv_trainer;
    private TextView tv_trainer_selezionato;
    private EditText et_descrizione, et_titolo, et_categoria, et_data_inizio, et_data_fine, et_numero_massimo;
    private String trainer_selezionato, descrizione, titolo, categoria, data_inizio, data_fine;
    private Integer numero_massimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_courses);

        // INIZIALIZZO VALORI E VIEW
        initialize();

        // PRENDO DATI PER LA ListView
        // Controllo che non siano vuoti altrimenti apro dialog con errore (MANCANO TRAINER CHE POSSANO TENERE IL CORSO)
        // Se non sono vuoti faccio inserire i dati dalla activity

        // Controllo campo campo che ogni cosa sia inserita correttamente
        if(checkInputData()){
            // Mando dati alla connection per inserire il corso

        }else{
            //MESSAGGI DI ERRORE GIA PRESENTI NEI CONTROLLI
        }




    }

    private boolean checkInputData() {
        //Controllo che ci sia un trainer selezionato
        boolean b_trainer = false;
        //Controllo descrizione     MAX 1000
        boolean b_descrizione = false;
        //Controllo titolo          MAX 100
        boolean b_titolo = false;
        //Controllo categoria       MAX 200
        boolean b_categoria = false;
        //Controllo data inizio     date
        boolean b_data_inizio = false;
        //Controllo data fine       date
        boolean b_data_fine = false;
        //Controllo numero massimo  integer
        boolean b_num_max = false;

        return (b_trainer && b_descrizione && b_titolo && b_categoria && b_data_inizio && b_data_fine && b_num_max);
    }

    private void initialize() {
        // Check intent data input (user_id)
        //region CHECK INTENT
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

        // Initialize variables
        gym_id = user_id;

        // Initialize and associates Wies
        lv_trainer = (ListView) findViewById(R.id.lv_course_trainer);
        tv_trainer_selezionato = (TextView) findViewById(R.id.tv_course_trainer_selezionato);
        et_descrizione = (EditText) findViewById(R.id.et_course_descrizione);
        et_titolo= (EditText) findViewById(R.id.et_course_titolo);
        et_categoria= (EditText) findViewById(R.id.et_course_categoria);
        et_data_inizio = (EditText) findViewById(R.id.et_course_data_inizio);
        et_data_fine= (EditText) findViewById(R.id.et_course_data_fine);
        et_numero_massimo = (EditText) findViewById(R.id.et_course_numero_massimo);

    }


}