package android_team.gymme_client.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.trainer.TrainerObject;

public class GymCoursesActivity extends AppCompatActivity {

    private int gym_id;
    private int user_id;
    private int traienr_id;

    private ArrayList<TrainerObject> trainer_list;

    private ListView lv_trainer;
    public Button btn_add_course;
    private TextView tv_trainer_selezionato;
    private EditText et_descrizione, et_titolo, et_categoria, et_data_inizio, et_data_fine, et_numero_massimo;
    private String trainer_selezionato, descrizione, titolo, categoria, data_inizio, data_fine;
    private Integer numero_massimo;

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
        trainer_list = new ArrayList<>();

        // Initialize and associates Wies
        lv_trainer = (ListView) findViewById(R.id.lv_course_trainer);
        tv_trainer_selezionato = (TextView) findViewById(R.id.tv_course_trainer_selezionato);
        et_descrizione = (EditText) findViewById(R.id.et_course_descrizione);
        et_titolo = (EditText) findViewById(R.id.et_course_titolo);
        et_categoria = (EditText) findViewById(R.id.et_course_categoria);
        et_data_inizio = (EditText) findViewById(R.id.et_course_data_inizio);
        et_data_fine = (EditText) findViewById(R.id.et_course_data_fine);
        et_numero_massimo = (EditText) findViewById(R.id.et_course_numero_massimo);
        btn_add_course = (Button) findViewById(R.id.btn_add_course);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_courses);

        // INIZIALIZZO VALORI E VIEW
        initialize();

        // PRENDO DATI PER LA ListView
        // Controllo che non siano vuoti altrimenti apro dialog con errore (MANCANO TRAINER CHE POSSANO TENERE IL CORSO)
        // Se non sono vuoti faccio inserire i dati dalla activity

        btn_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputData()) {
                    // Mando dati alla connection per inserire il corso

                }
            }
        });


    }

    private boolean checkInputData() {

        if (checkTrainer()) {
            if (checkDescrizione()) {
                if (checkTitolo()) {
                    if (checkCategoria()) {
                        if (checkDataInizio()) {
                            if (checkDataFine()) {
                                if (checkNumber()) {
                                    Log.e("CHECK", "ALL OK");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        // Gestione errori nei singoli metodi check...
        return false;
    }

    private boolean checkNumber() {
        String n_max = et_numero_massimo.getText().toString().trim();
        if (n_max.isEmpty()) {
            Toast.makeText(GymCoursesActivity.this, "Inserisci un numero massimo di partecipanti!", Toast.LENGTH_LONG).show();
            return false;
        } else {
            try {
                numero_massimo = Integer.parseInt(n_max);
                Log.e("CONVERSIONE NUMERO", "OK");
                return true;
            } catch (Exception e) {
                Log.e("CONVERSIONE NUMERO", "ERROR");
                Toast.makeText(GymCoursesActivity.this, "Inserisci un numero massimo di sole cifre numeriche!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean checkDataFine() {
        data_fine = et_data_fine.getText().toString().trim();
        if (data_fine.isEmpty()) {
            Toast.makeText(GymCoursesActivity.this, "Inserisci una data di fine!", Toast.LENGTH_LONG).show();
            return false;
        } else if (!validateDate(data_fine)) {
            Toast.makeText(GymCoursesActivity.this, "DATA FINE ERRATA.\n   - FORMATO: <gg/mm/AAAA> senza spazi vuoti e inserendo appositi zeri\n   - DATA: dal 2020 al 2200", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDataInizio() {
        data_inizio = et_data_inizio.getText().toString().trim();
        if (data_inizio.isEmpty()) {
            Toast.makeText(GymCoursesActivity.this, "Inserisci una data di inizio!", Toast.LENGTH_LONG).show();
            return false;
        } else if (!validateDate(data_inizio)) {
            Toast.makeText(GymCoursesActivity.this, "DATA FINE ERRATA.\n   - FORMATO: <gg/mm/AAAA> senza spazi vuoti e inserendo appositi zeri\n   - DATA: dal 2020 al 2200\"", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkCategoria() {
        categoria = et_categoria.getText().toString().trim();
        if (categoria.isEmpty()) {
            Toast.makeText(GymCoursesActivity.this, "Inserisci un categoria!", Toast.LENGTH_LONG).show();
            return false;
        } else if (categoria.length() > 100) {
            Toast.makeText(GymCoursesActivity.this, "Categoria troppo lunga. MAX 200 caratteri.\nUsati: " + categoria.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "categoria OK");
            return true;
        }
    }

    private boolean checkTitolo() {
        titolo = et_titolo.getText().toString().trim();
        if (titolo.isEmpty()) {
            Toast.makeText(GymCoursesActivity.this, "Inserisci un titolo!", Toast.LENGTH_LONG).show();
            return false;
        } else if (titolo.length() > 100) {
            Toast.makeText(GymCoursesActivity.this, "Titolo troppo lunga. MAX 100 caratteri.\nUsati: " + titolo.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "TITOLO OK");
            return true;
        }
    }


    private boolean checkDescrizione() {
        descrizione = et_descrizione.getText().toString().trim();
        if (descrizione.isEmpty()) {
            Toast.makeText(GymCoursesActivity.this, "Inserisci una descrizione!", Toast.LENGTH_LONG).show();
            return false;
        } else if (descrizione.length() > 1000) {
            Toast.makeText(GymCoursesActivity.this, "Descrizione troppo lunga. MAX 1000 caratteri.\nUsati: " + descrizione.length(), Toast.LENGTH_LONG).show();
            return false;
        } else {
            Log.e("CHECK", "DESCRIZIONE OK");
            return true;
        }

    }

    private boolean checkTrainer() {
        trainer_selezionato = tv_trainer_selezionato.getText().toString().trim();
        if (trainer_selezionato.isEmpty()) {
            Toast.makeText(GymCoursesActivity.this, "Seleziona un trainer dalla lista", Toast.LENGTH_LONG).show();
            return false;
        } else {
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


}