package android_team.gymme_client.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NutritionistSignupActivity extends AppCompatActivity {

    @BindView(R.id.nutritionist_qualification_signup_text_input)
    TextInputLayout _nutritionist_qualification_signup_text_input;
    @BindView(R.id.nutritionist_qualification_signup_edit_text)
    TextInputEditText _nutritionist_qualification_signup_edit_text;
    @BindView(R.id.nutritionist_fiscal_code_signup_text_input)
    TextInputLayout _nutritionist_fiscal_code_signup_text_input;
    @BindView(R.id.nutritionist_fiscal_code_signup_edit_text)
    TextInputEditText _nutritionist_fiscal_code_signup_edit_text;
    @BindView(R.id.signup_button_final_nutritionist)
    Button _signup_button_final_nutritionist;

    @BindView(R.id.progressBarSignupNutritionist)
    ProgressBar progressBarSignupNutritionist;

    int user_id;
    String email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_nutritionist);
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
        _signup_button_final_nutritionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void validateFields() {
        if (_nutritionist_qualification_signup_edit_text.getText().toString().isEmpty() || _nutritionist_fiscal_code_signup_edit_text.getText().toString().isEmpty()) {
            if (_nutritionist_qualification_signup_edit_text.getText().toString().isEmpty()) {
                _nutritionist_qualification_signup_text_input.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                _nutritionist_qualification_signup_text_input.setHint("Inserisci la tua qualifica!");
                _nutritionist_qualification_signup_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            }
            if (_nutritionist_fiscal_code_signup_edit_text.getText().toString().isEmpty()) {
                _nutritionist_fiscal_code_signup_text_input.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                _nutritionist_fiscal_code_signup_text_input.setHint("Inserisci il tuo codice fiscale!");
                _nutritionist_fiscal_code_signup_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            }
        } else {
            String qualification = _nutritionist_qualification_signup_edit_text.getText().toString();
            String fiscal_code = _nutritionist_fiscal_code_signup_edit_text.getText().toString();
            new NutritionistSignupActivity.RegisterNutritionistConnection().execute(Integer.toString(user_id), qualification, fiscal_code);
        }
    }

    private class RegisterNutritionistConnection extends AsyncTask<String, String, Integer> {
        String toastMessage = null;

        @Override
        protected void onPreExecute() {

            _signup_button_final_nutritionist.setEnabled(false);
            _signup_button_final_nutritionist.setVisibility(View.GONE);
            progressBarSignupNutritionist.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/register/nutritionist");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                JsonObject paramsJson = new JsonObject();
                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("qualification", params[1]);
                paramsJson.addProperty("fiscal_code", params[2]);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(paramsJson.toString());
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.putExtra("email", email);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    urlConnection.disconnect();
                    toastMessage = "Dati registrati correttamente!";
                    finish();
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    //Log.e("Server response", "Error during signup!");
                    toastMessage = "Errore nella registrazione di un nuovo utente!";
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                toastMessage = "Impossibile connettersi!";
                responseCode = 69;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            _signup_button_final_nutritionist.setEnabled(true);
            _signup_button_final_nutritionist.setVisibility(View.VISIBLE);
            progressBarSignupNutritionist.setVisibility(View.GONE);

        }
    }
}
