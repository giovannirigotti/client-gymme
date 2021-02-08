package android_team.gymme_client.gym.menage_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.customer.CustomerSmallObject;
import android_team.gymme_client.login.LoginActivity;

public class GymCustomersActivity extends AppCompatActivity {

    private int user_id;

    ListView lv_clienti;
    EditText et_search_client;

    ArrayList<CustomerSmallObject> lista_clienti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_customers);

        initialize();


    }

    private void initialize() {

        // CHECK GET INTENT EXTRAS
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

        lv_clienti = (ListView) findViewById(R.id.lv_clienti_palestra);
        et_search_client = (EditText) findViewById(R.id.et_search_cliente);

        lista_clienti = new ArrayList<>();


    }
}