package android_team.gymme_client.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GymSignupActivity extends AppCompatActivity {

    @BindView(R.id.gym_vat_signup_text_input)
    TextInputLayout _gym_vat_signup_text_input;
    @BindView(R.id.gym_vat_signup_edit_text)
    TextInputEditText _gym_vat_signup_edit_text;
    @BindView(R.id.gym_name_signup_text_input)
    TextInputLayout _gym_name_signup_text_input;
    @BindView(R.id.gym_name_signup_edit_text)
    TextInputEditText _gym_name_signup_edit_text;
    @BindView(R.id.gym_address_signup_text_input)
    TextInputLayout _gym_address_signup_text_input;
    @BindView(R.id.gym_address_signup_edit_text)
    TextInputEditText _gym_address_signup_edit_text;
    @BindView(R.id.zip_code_signup_text_input)
    TextInputLayout _zip_code_signup_text_input;
    @BindView(R.id.gym_zip_code_signup_edit_text)
    TextInputEditText _gym_zip_code_signup_edit_text;
    @BindView(R.id.signup_button_next_gym)
    Button _signup_button_next_gym;
    @BindView(R.id.signupGymProgressBar)
    ProgressBar signupGymProgressBar;


    int user_id;
    String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_gym);

        ButterKnife.bind(this);

        Intent i = getIntent();
        if (!i.hasExtra("user_id")) {
            Toast.makeText(this, "Utente non creato", Toast.LENGTH_LONG).show();
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

        email = i.getStringExtra("email");

        _signup_button_next_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _signup_button_next_gym.setEnabled(false);
                _signup_button_next_gym.setVisibility(View.GONE);
                signupGymProgressBar.setVisibility(View.VISIBLE);
                validateFields();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void validateFields() {
        if (_gym_vat_signup_edit_text.getText().toString().trim().isEmpty() || _gym_name_signup_edit_text.getText().toString().trim().isEmpty()
                || _gym_address_signup_edit_text.getText().toString().trim().isEmpty() || _gym_zip_code_signup_edit_text.getText().toString().trim().isEmpty()) {

            if (_gym_vat_signup_edit_text.getText().toString().trim().isEmpty()) {
                _gym_vat_signup_text_input.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                _gym_vat_signup_text_input.setHint("Inserisci la partita IVA!");
                _gym_vat_signup_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            }

            if (_gym_name_signup_edit_text.getText().toString().trim().isEmpty()) {
                _gym_name_signup_text_input.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                _gym_name_signup_text_input.setHint("Inserisci il nome!");
                _gym_name_signup_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            }

            if (_gym_address_signup_edit_text.getText().toString().trim().isEmpty()) {
                _gym_address_signup_text_input.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                _gym_address_signup_text_input.setHint("Inserisci l'indirizzo!");
                _gym_address_signup_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            }

            if (_gym_zip_code_signup_edit_text.getText().toString().trim().isEmpty()) {
                _zip_code_signup_text_input.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                _zip_code_signup_text_input.setHint("Inserisci il CAP!");
                _zip_code_signup_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            }
        } else {
            String vat_number = _gym_vat_signup_edit_text.getText().toString();
            String name = _gym_name_signup_edit_text.getText().toString();
            String address = _gym_address_signup_edit_text.getText().toString();
            String zip = _gym_zip_code_signup_edit_text.getText().toString();

            Intent i = new Intent(getApplicationContext(), GymSignupActivity2.class);
            i.putExtra("user_id", user_id);
            i.putExtra("email", email);
            i.putExtra("vat_number", vat_number);
            i.putExtra("gym_name", name);
            i.putExtra("gym_address", address);
            i.putExtra("zip_code", zip);
            startActivity(i);
            _signup_button_next_gym.setEnabled(true);
            _signup_button_next_gym.setVisibility(View.VISIBLE);
            signupGymProgressBar.setVisibility(View.GONE);
        }
    }
}

