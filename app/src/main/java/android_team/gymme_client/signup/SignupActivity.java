package android_team.gymme_client.signup;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.NoNetworkActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String birthdate;
    private boolean name_bool = false;
    private boolean lastname_bool = false;
    private boolean email_bool = false;
    private boolean password_bool = false;
    private boolean password_confirm_bool = false;
    private boolean birthdate_bool = false;

    @BindView(R.id.name_signup_textinput_layout)
    TextInputLayout _nameTextInputLayout;
    @BindView(R.id.lastname_signup_textinput_layout)
    TextInputLayout _lastnameTextInputLayout;
    @BindView(R.id.email_signup_textinput_layout)
    TextInputLayout _emailTextInputLayout;
    @BindView(R.id.birthdate_signup_textinput_layout)
    TextInputLayout _birthDateTextInputLayout;
    @BindView(R.id.password_signup_textinput_layout)
    TextInputLayout _passwordTextInputLayout;
    @BindView(R.id.password_confirm_signup_textinput_layout)
    TextInputLayout _passwordConfirmTextInputLayout;

    @BindView(R.id.name_signup)
    TextInputEditText _nameText;
    @BindView(R.id.lastname_signup)
    TextInputEditText _lastameText;
    @BindView(R.id.email_signup)
    TextInputEditText _emailText;
    @BindView(R.id.birthdate_signup)
    TextInputEditText _birthdateText;
    @BindView(R.id.password_signup)
    TextInputEditText _passwordText;
    @BindView(R.id.password_confirm_signup)
    TextInputEditText _passwordConfirmText;

    @BindView(R.id.back_to_login_button)
    ImageButton _backButton;
    @BindView(R.id.next_signup_button)
    Button _nextButton;
    @BindView(R.id.progress_bar_signup)
    ProgressBar _progress_bar_signup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


        _nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }

        });

    }


    private void validate() {

        Log.d(TAG, "Validating");

        if (_nameText.getText().toString().isEmpty()) {
            _nameTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            _nameTextInputLayout.setHint("Inserisci il tuo nome!");
        } else if (!validateName(_nameText.getText().toString()) && !_nameText.getText().toString().isEmpty()) {

            _nameTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            _nameTextInputLayout.setHint("Inserisci un nome valido!");

        } else {
            _nameTextInputLayout.setHintTextColor(_nameTextInputLayout.getDefaultHintTextColor());
            _nameTextInputLayout.setHint("Nome");
            name = _nameText.getText().toString();
            name_bool = true;
        }

        if (_lastameText.getText().toString().isEmpty()) {
            _lastnameTextInputLayout.setHint("Inserisci il tuo cognome!");
            _lastnameTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
        } else if (!validateName(_lastameText.getText().toString()) && !_lastameText.getText().toString().isEmpty()) {

            _lastnameTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            _lastnameTextInputLayout.setHint("Inserisci un nome valido!");

        } else {
            _lastnameTextInputLayout.setHintTextColor(_birthDateTextInputLayout.getDefaultHintTextColor());
            _lastnameTextInputLayout.setHint("Cognome");
            lastname = _lastameText.getText().toString();
            lastname_bool = true;
        }

        if (_emailText.getText().toString().isEmpty()) {
            _emailTextInputLayout.setHint("Inserisci la tua mail!");
            _emailTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
        } else if (!validateMail(_emailText.getText().toString()) && !_emailText.getText().toString().isEmpty()) {
            _emailTextInputLayout.setHint("Email non valida!");
            _emailTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
        } else if (validateMail(_emailText.getText().toString()) && !_emailText.getText().toString().isEmpty()) {
            _emailTextInputLayout.setHintTextColor(_emailTextInputLayout.getDefaultHintTextColor());
            _emailTextInputLayout.setHint("Email");
            email = _emailText.getText().toString();
            email_bool = true;
        }


        if (_birthdateText.getText().toString().isEmpty()) {
            _birthDateTextInputLayout.setHint("Inserisci la tua data di nascita!");
            _birthDateTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
        } else if (!_birthdateText.getText().toString().isEmpty() && !validateBirthDate(_birthdateText.getText().toString())) {
            _birthDateTextInputLayout.setHint("Data non valida!");
            _birthDateTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            _birthDateTextInputLayout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            _birthDateTextInputLayout.setHelperText("La data deve essere in formato gg/mm/aaaa");
        } else if (!_birthdateText.getText().toString().isEmpty() && validateBirthDate(_birthdateText.getText().toString())) {
            _birthDateTextInputLayout.setHintTextColor(_birthDateTextInputLayout.getDefaultHintTextColor());
            _birthDateTextInputLayout.setHint("Data di nascita");
            _birthDateTextInputLayout.setHelperTextColor(_birthDateTextInputLayout.getDefaultHintTextColor());
            _birthDateTextInputLayout.setHelperText("gg/mm/aaaa");
            birthdate = _birthdateText.getText().toString();
            birthdate_bool = true;

        }


        if (_passwordText.getText().toString().isEmpty()) {
            _passwordTextInputLayout.setHint("Inserisci la tua password!");
            _passwordTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
        } else if (!_passwordText.getText().toString().isEmpty() && !validatePassword(_passwordText.getText().toString())) {
            _passwordTextInputLayout.setHint("Password non valida!");
            _passwordTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            _passwordTextInputLayout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
            _passwordTextInputLayout.setHelperText("La password deve contenere:\n" + "\u2022 Un carattere maiusciolo e uno minuscolo\n" +
                    "\u2022 Un simbolo\n" + "\u2022 Almeno 8 caratteri\n" + "\u2022 Un numero\n" + "\u2022 Nessuno spazio");
        } else if (!_passwordText.getText().toString().isEmpty() && validatePassword(_passwordText.getText().toString())) {
            _passwordConfirmTextInputLayout.setHintTextColor(_passwordConfirmTextInputLayout.getDefaultHintTextColor());
            _passwordConfirmTextInputLayout.setHint("Password");
            _passwordConfirmTextInputLayout.setHelperTextEnabled(false);
            password_bool = true;
            password = _passwordText.getText().toString();
        }

        Log.e("Data", _passwordText.getText().toString() + " " + _passwordConfirmText.getText().toString());
        String str1 = _passwordText.getText().toString();
        String str2 = _passwordConfirmText.getText().toString();


        if (str2.isEmpty()) {
            _passwordConfirmTextInputLayout.setHint("Inserisci di nuovo la tua password!");
            _passwordConfirmTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
        } else if (!(str1.equals(str2))) {
            _passwordConfirmTextInputLayout.setHint("Le due password sono diverse!");
            _passwordConfirmTextInputLayout.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
        } else if (str1.equals(str2) && !str2.isEmpty()) {
            _passwordConfirmTextInputLayout.setHintTextColor(_passwordConfirmTextInputLayout.getDefaultHintTextColor());
            _passwordConfirmTextInputLayout.setHint("Ripeti password");
            _passwordConfirmTextInputLayout.setHelperTextEnabled(false);
            password_confirm_bool = true;
        }


        Log.e("Debug", name_bool + " " + lastname_bool + " " + email_bool + " " + birthdate_bool + " " + password_bool + " " + password_confirm_bool);

        if (name_bool && lastname_bool && email_bool && birthdate_bool && password_bool && password_confirm_bool) {


            new CheckUserDataConnection().execute(email);

        }


    }


    private boolean validateMail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
    }

    private boolean validateBirthDate(String birthdate) {
        boolean checkFormat;
        boolean checkNumber;

        if (birthdate.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
            checkFormat = true;
        else
            checkFormat = false;

        String[] t = birthdate.split("/");
        checkNumber = (Integer.valueOf(t[0]) <= 31 && Integer.valueOf(t[1]) <= 12 && Integer.valueOf(t[2]) >= 1900 && Integer.valueOf(t[2]) <= 2100);

        return (checkNumber && checkFormat);
    }

    private boolean validateName(String name) {

        String regex = "^[A-Za-z]+$";
        return name.matches(regex);


    }



    private class CheckUserDataConnection extends AsyncTask<String, String, JsonObject> {

        String toastMessage = null;

        @Override
        protected void onPreExecute() {

            _nextButton.setEnabled(false);
            _nextButton.setVisibility(View.GONE);
            _progress_bar_signup.setVisibility(View.VISIBLE);
            _emailText.setEnabled(false);
            _passwordText.setEnabled(false);
            _nameText.setEnabled(false);
            _lastameText.setEnabled(false);
            _birthdateText.setEnabled(false);
        }


        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;

            try {
                url = new URL("http://10.0.2.2:4000/get_user_data/"+params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "Duplicate user!");
                    toastMessage = "Esiste già un utente registrato con questa email!";

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {

                    Intent intent = new Intent(getApplicationContext(), SignupActivity2.class);
                    intent.putExtra("name", name);
                    intent.putExtra("lastname", lastname);
                    intent.putExtra("email", email);
                    intent.putExtra("birthdate", birthdate);
                    intent.putExtra("password", password);
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            } catch (IOException e) {
                e.printStackTrace();
                toastMessage = "Impossibile connettersi!";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return user;
        }

        @Override
        protected void onPostExecute(JsonObject user) {
            _nextButton.setEnabled(true);
            _nextButton.setVisibility(View.VISIBLE);
            _progress_bar_signup.setVisibility(View.GONE);
            _emailText.setEnabled(true);
            _passwordText.setEnabled(true);
            _nameText.setEnabled(true);
            _lastameText.setEnabled(true);
            _birthdateText.setEnabled(true);

            Toast responseToast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);
            if (toastMessage != null)
                responseToast.show();
        }
    }




}