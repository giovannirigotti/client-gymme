package android_team.gymme_client.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


public class GymSignupActivity3 extends AppCompatActivity {

    @BindView(R.id.monday_opening_text_input)
    TextInputLayout monday_opening_text_input;
    @BindView(R.id.monday_opening_edit_text)
    EditText monday_opening_edit_text;
    @BindView(R.id.monday_closing_text_input)
    TextInputLayout monday_closing_text_input;
    @BindView(R.id.monday_closing_edit_text)
    EditText monday_closing_edit_text;
    @BindView(R.id.tuesday_opening_text_input)
    TextInputLayout tuesday_opening_text_input;
    @BindView(R.id.tuesday_opening_edit_text)
    EditText tuesday_opening_edit_text;
    @BindView(R.id.tuesday_closing_text_input)
    TextInputLayout tuesday_closing_text_input;
    @BindView(R.id.tuesday_closing_edit_text)
    EditText tuesday_closing_edit_text;
    @BindView(R.id.wednesday_opening_text_input)
    TextInputLayout wednesday_opening_text_input;
    @BindView(R.id.wednesday_opening_edit_text)
    EditText wednesday_opening_edit_text;
    @BindView(R.id.wednesday_closing_text_input)
    TextInputLayout wednesday_closing_text_input;
    @BindView(R.id.wednesday_closing_edit_text)
    EditText wednesday_closing_edit_text;
    @BindView(R.id.thursday_opening_text_input)
    TextInputLayout thursday_opening_text_input;
    @BindView(R.id.thursday_opening_edit_text)
    EditText thursday_opening_edit_text;
    @BindView(R.id.thursday_closing_text_input)
    TextInputLayout thursday_closing_text_input;
    @BindView(R.id.thursday_closing_edit_text)
    EditText thursday_closing_edit_text;
    @BindView(R.id.friday_opening_text_input)
    TextInputLayout friday_opening_text_input;
    @BindView(R.id.friday_opening_edit_text)
    EditText friday_opening_edit_text;
    @BindView(R.id.friday_closing_text_input)
    TextInputLayout friday_closing_text_input;
    @BindView(R.id.friday_closing_edit_text)
    EditText friday_closing_edit_text;
    @BindView(R.id.saturday_opening_text_input)
    TextInputLayout saturday_opening_text_input;
    @BindView(R.id.saturday_opening_edit_text)
    EditText saturday_opening_edit_text;
    @BindView(R.id.saturday_closing_text_input)
    TextInputLayout saturday_closing_text_input;
    @BindView(R.id.saturday_closing_edit_text)
    EditText saturday_closing_edit_text;
    @BindView(R.id.sunday_opening_text_input)
    TextInputLayout sunday_opening_text_input;
    @BindView(R.id.sunday_opening_edit_text)
    EditText sunday_opening_edit_text;
    @BindView(R.id.sunday_closing_text_input)
    TextInputLayout sunday_closing_text_input;
    @BindView(R.id.sunday_closing_edit_text)
    EditText sunday_closing_edit_text;

    @BindView(R.id.signup_button_final_gym)
    Button signup_button_final_gym;

    @BindView(R.id.progressBarSignupGym3)
    ProgressBar progressBarSignup3Gym;

    int user_id;
    String email;
    String vat_number;
    String gym_name;
    String gym_address;
    String zip_code;

    int opening_monday = -1;
    int closing_monday = -1;
    int opening_tuesday = -1;
    int closing_tuesday = -1;
    int opening_wednesday = -1;
    int closing_wednesday = -1;
    int opening_thursday = -1;
    int closing_thursday = -1;
    int opening_friday = -1;
    int closing_friday = -1;
    int opening_saturday = -1;
    int closing_saturday = -1;
    int opening_sunday = -1;
    int closing_sunday = -1;


    int[] hours = new int[14];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_gym_3);
        ButterKnife.bind(this);

        user_id = getIntent().getIntExtra("user_id", -1);
        email = getIntent().getStringExtra("email");
        vat_number = getIntent().getStringExtra("vat_number");
        gym_name = getIntent().getStringExtra("gym_name");
        gym_address = getIntent().getStringExtra("gym_address");
        zip_code = getIntent().getStringExtra("zip_code");
        final String pool = Integer.toString(getIntent().getBooleanExtra("pool", false) ? 1 : 0);
        final String boxRing = Integer.toString(getIntent().getBooleanExtra("box_ring", false) ? 1 : 0);
        final String aerobics = Integer.toString(getIntent().getBooleanExtra("aerobics", false) ? 1 : 0);
        final String spa = Integer.toString(getIntent().getBooleanExtra("spa", false) ? 1 : 0);
        final String wifi = Integer.toString(getIntent().getBooleanExtra("wifi", false) ? 1 : 0);
        final String parkingArea = Integer.toString(getIntent().getBooleanExtra("parking_area", false) ? 1 : 0);
        final String personalTrainer = Integer.toString(getIntent().getBooleanExtra("personal_trainer", false) ? 1 : 0);
        final String nutritionist = Integer.toString(getIntent().getBooleanExtra("nutritionist", false) ? 1 : 0);
        final String impedanceBalance = Integer.toString(getIntent().getBooleanExtra("impedance_balance", false) ? 1 : 0);
        final String courses = Integer.toString(getIntent().getBooleanExtra("courses", false) ? 1 : 0);
        final String showers = Integer.toString(getIntent().getBooleanExtra("showers", false) ? 1 : 0);


        signup_button_final_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("checkformat", Boolean.toString(checkFormat()));

                if (checkFormat()) {
                    getHours();

                    new RegisterGymDataConnection().execute(Integer.toString(user_id), vat_number, gym_name, gym_address, zip_code,
                            pool, boxRing, aerobics, spa, wifi, parkingArea, personalTrainer, nutritionist, impedanceBalance, courses, showers, Integer.toString(opening_monday),
                            Integer.toString(closing_monday),
                            Integer.toString(opening_tuesday),
                            Integer.toString(closing_tuesday),
                            Integer.toString(opening_wednesday),
                            Integer.toString(closing_wednesday),
                            Integer.toString(opening_thursday),
                            Integer.toString(closing_thursday),
                            Integer.toString(opening_friday),
                            Integer.toString(closing_friday),
                            Integer.toString(opening_saturday),
                            Integer.toString(closing_saturday),
                            Integer.toString(opening_sunday),
                            Integer.toString(closing_sunday)
                    );
                }
            }
        });
    }

    private class RegisterGymDataConnection extends AsyncTask<String, String, Integer> {
        String toastMessage = null;

        @Override
        protected void onPreExecute() {
            signup_button_final_gym.setEnabled(false);
            signup_button_final_gym.setVisibility(View.GONE);
            progressBarSignup3Gym.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            //Log.e("DEBUG", "1");
            try {
                url = new URL("http://10.0.2.2:4000/register/gym/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                JsonObject paramsJson = new JsonObject();
                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("vat_number", params[1]);
                paramsJson.addProperty("gym_name", params[2]);
                paramsJson.addProperty("gym_address", params[3]);
                paramsJson.addProperty("zip_code", params[4]);
                paramsJson.addProperty("pool", params[5]);
                paramsJson.addProperty("box_ring", params[6]);
                paramsJson.addProperty("aerobics", params[7]);
                paramsJson.addProperty("spa", params[8]);
                paramsJson.addProperty("wifi", params[9]);
                paramsJson.addProperty("parking_area", params[10]);
                paramsJson.addProperty("personal_trainer_service", params[11]);
                paramsJson.addProperty("nutritionist_service", params[12]);
                paramsJson.addProperty("impedance_balance", params[13]);
                paramsJson.addProperty("courses", params[14]);
                paramsJson.addProperty("showers", params[15]);
                paramsJson.addProperty("opening_monday", params[16]);
                paramsJson.addProperty("closing_monday", params[17]);
                paramsJson.addProperty("opening_tuesday", params[18]);
                paramsJson.addProperty("closing_tuesday", params[19]);
                paramsJson.addProperty("opening_wednesday", params[20]);
                paramsJson.addProperty("closing_wednesday", params[21]);
                paramsJson.addProperty("opening_thursday", params[22]);
                paramsJson.addProperty("closing_thursday", params[23]);
                paramsJson.addProperty("opening_friday", params[24]);
                paramsJson.addProperty("closing_friday", params[25]);
                paramsJson.addProperty("opening_saturday", params[26]);
                paramsJson.addProperty("closing_saturday", params[27]);
                paramsJson.addProperty("opening_sunday", params[28]);
                paramsJson.addProperty("closing_sunday", params[29]);


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
                //Log.e("DEBUG", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    //Log.e("DEBUG", "3");
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.putExtra("email", email);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    urlConnection.disconnect();
                    toastMessage = "Dati registrati correttamente!";
                    finish();
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {

                    //Log.e("DEBUG", "4");
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
            if (toastMessage != null) {
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            }

            signup_button_final_gym.setEnabled(true);
            signup_button_final_gym.setVisibility(View.VISIBLE);
            progressBarSignup3Gym.setVisibility(View.GONE);
        }
    }

    private void getHours() {
        if (!monday_opening_edit_text.getText().toString().trim().isEmpty()) {
            opening_monday = toMins(monday_opening_edit_text.getText().toString().trim());
        } else opening_monday = -1;
        if (!monday_closing_edit_text.getText().toString().trim().isEmpty()) {
            closing_monday = toMins(monday_closing_edit_text.getText().toString().trim());
        } else closing_monday = -1;
        if (!tuesday_opening_edit_text.getText().toString().trim().isEmpty()) {
            opening_tuesday = toMins(tuesday_opening_edit_text.getText().toString().trim());
        } else opening_tuesday = -1;
        if (!tuesday_closing_edit_text.getText().toString().trim().isEmpty()) {
            closing_tuesday = toMins(tuesday_closing_edit_text.getText().toString().trim());
            opening_tuesday = toMins(tuesday_opening_edit_text.getText().toString().trim());
        } else closing_tuesday = -1;
        if (!wednesday_opening_edit_text.getText().toString().trim().isEmpty()) {
            opening_wednesday = toMins(wednesday_opening_edit_text.getText().toString().trim());
        } else opening_wednesday = -1;
        if (!wednesday_closing_edit_text.getText().toString().trim().isEmpty()) {
            closing_wednesday = toMins(wednesday_closing_edit_text.getText().toString().trim());
        } else closing_wednesday = -1;
        if (!thursday_opening_edit_text.getText().toString().trim().isEmpty()) {
            opening_thursday = toMins(thursday_opening_edit_text.getText().toString().trim());
        } else opening_thursday = -1;
        if (!thursday_closing_edit_text.getText().toString().trim().isEmpty()) {
            closing_thursday = toMins(thursday_closing_edit_text.getText().toString().trim());
        } else closing_thursday = -1;
        if (!friday_opening_edit_text.getText().toString().trim().isEmpty()) {
            opening_friday = toMins(friday_opening_edit_text.getText().toString().trim());
        } else opening_friday = -1;
        if (!friday_closing_edit_text.getText().toString().trim().isEmpty()) {
            closing_friday = toMins(friday_closing_edit_text.getText().toString().trim());
        } else closing_friday = -1;
        if (!saturday_opening_edit_text.getText().toString().trim().isEmpty()) {
            opening_saturday = toMins(saturday_opening_edit_text.getText().toString().trim());
        } else opening_saturday = -1;
        if (!saturday_closing_edit_text.getText().toString().trim().isEmpty()) {
            closing_saturday = toMins(saturday_closing_edit_text.getText().toString().trim());
        } else closing_saturday = -1;
        if (!sunday_opening_edit_text.getText().toString().trim().isEmpty()) {
            opening_sunday = toMins(sunday_opening_edit_text.getText().toString().trim());
        } else opening_sunday = -1;
        if (!sunday_closing_edit_text.getText().toString().trim().isEmpty()) {
            closing_sunday = toMins(sunday_closing_edit_text.getText().toString().trim());
        } else closing_sunday = -1;

        hours[0] = opening_monday;
        hours[1] = closing_monday;
        hours[2] = opening_tuesday;
        hours[3] = closing_tuesday;
        hours[4] = opening_wednesday;
        hours[5] = closing_wednesday;
        hours[6] = opening_thursday;
        hours[7] = closing_thursday;
        hours[8] = opening_friday;
        hours[9] = closing_friday;
        hours[10] = opening_saturday;
        hours[11] = closing_saturday;
        hours[12] = opening_sunday;
        hours[13] = closing_sunday;

    }

    private int toMins(String s) {

        int hours = -1;
        int minutes = 0;

        if (s.substring(1, 1).compareTo("0") == 0) {
            hours = Integer.parseInt(s.substring(1, 2)) * 60;
        } else {
            hours = Integer.parseInt(s.substring(0, 2)) * 60;
        }

        if (s.substring(3, 4).compareTo("0") == 0) {
            minutes = Integer.parseInt(s.substring(4, 5));
        } else {

            minutes = Integer.parseInt(s.substring(3, 5));

        }

        return hours + minutes;
    }

    private boolean checkFormat() {
        boolean result = false;

        if ((validateHour(monday_opening_edit_text.getText().toString()) || monday_opening_edit_text.getText().toString().isEmpty()) &&
                (validateHour(monday_closing_edit_text.getText().toString()) || monday_closing_edit_text.getText().toString().isEmpty()) &&
                (validateHour(tuesday_opening_edit_text.getText().toString()) || tuesday_opening_edit_text.getText().toString().isEmpty()) &&
                (validateHour(tuesday_closing_edit_text.getText().toString()) || tuesday_closing_edit_text.getText().toString().isEmpty()) &&
                (validateHour(wednesday_opening_edit_text.getText().toString()) || wednesday_opening_edit_text.getText().toString().isEmpty()) &&
                (validateHour(wednesday_closing_edit_text.getText().toString()) || wednesday_closing_edit_text.getText().toString().isEmpty()) &&
                (validateHour(thursday_opening_edit_text.getText().toString()) || thursday_opening_edit_text.getText().toString().isEmpty()) &&
                (validateHour(thursday_closing_edit_text.getText().toString()) || thursday_closing_edit_text.getText().toString().isEmpty()) &&
                (validateHour(friday_opening_edit_text.getText().toString()) || friday_opening_edit_text.getText().toString().isEmpty()) &&
                (validateHour(friday_closing_edit_text.getText().toString()) || friday_closing_edit_text.getText().toString().isEmpty()) &&
                (validateHour(saturday_opening_edit_text.getText().toString()) || saturday_opening_edit_text.getText().toString().isEmpty()) &&
                (validateHour(saturday_closing_edit_text.getText().toString()) || saturday_closing_edit_text.getText().toString().isEmpty()) &&
                (validateHour(sunday_opening_edit_text.getText().toString()) || sunday_opening_edit_text.getText().toString().isEmpty()) &&
                (validateHour(sunday_closing_edit_text.getText().toString()) || sunday_closing_edit_text.getText().toString().isEmpty())) {
            result = true;
        } else {
            if (!validateHour(monday_opening_edit_text.getText().toString()) && !monday_opening_edit_text.getText().toString().isEmpty()) {
                monday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                monday_opening_text_input.setHint("Formato invalido!");
            }

            if (!validateHour(monday_closing_edit_text.getText().toString()) && !monday_closing_edit_text.getText().toString().isEmpty()) {
                monday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                monday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(tuesday_opening_edit_text.getText().toString()) && !tuesday_opening_edit_text.getText().toString().isEmpty()) {
                tuesday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                tuesday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(tuesday_closing_edit_text.getText().toString()) && !tuesday_closing_edit_text.getText().toString().isEmpty()) {
                tuesday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                tuesday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(wednesday_opening_edit_text.getText().toString()) && !wednesday_opening_edit_text.getText().toString().isEmpty()) {
                wednesday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                wednesday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(wednesday_closing_edit_text.getText().toString()) && !wednesday_closing_edit_text.getText().toString().isEmpty()) {
                wednesday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                wednesday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(tuesday_opening_edit_text.getText().toString()) && !tuesday_opening_edit_text.getText().toString().isEmpty()) {
                tuesday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                tuesday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(tuesday_closing_edit_text.getText().toString()) && !tuesday_closing_edit_text.getText().toString().isEmpty()) {
                tuesday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                tuesday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(wednesday_opening_edit_text.getText().toString()) && !wednesday_opening_edit_text.getText().toString().isEmpty()) {
                wednesday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                wednesday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(wednesday_closing_edit_text.getText().toString()) && !wednesday_closing_edit_text.getText().toString().isEmpty()) {
                wednesday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                wednesday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(thursday_opening_edit_text.getText().toString()) && !thursday_opening_edit_text.getText().toString().isEmpty()) {
                thursday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                thursday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(thursday_closing_edit_text.getText().toString()) && !thursday_closing_edit_text.getText().toString().isEmpty()) {
                thursday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                thursday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(friday_opening_edit_text.getText().toString()) && !friday_opening_edit_text.getText().toString().isEmpty()) {
                friday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                friday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(friday_closing_edit_text.getText().toString()) && !friday_closing_edit_text.getText().toString().isEmpty()) {
                friday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                friday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(saturday_opening_edit_text.getText().toString()) && !saturday_opening_edit_text.getText().toString().isEmpty()) {
                saturday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                saturday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(saturday_closing_edit_text.getText().toString()) && !saturday_closing_edit_text.getText().toString().isEmpty()) {
                saturday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                saturday_closing_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(sunday_opening_edit_text.getText().toString()) && !sunday_opening_edit_text.getText().toString().isEmpty()) {
                sunday_opening_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                sunday_opening_text_input.setHint("Formato invalido!");
            }
            if (!validateHour(sunday_closing_edit_text.getText().toString()) && !sunday_closing_edit_text.getText().toString().isEmpty()) {
                sunday_closing_text_input.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#fa8282")));
                sunday_closing_text_input.setHint("Formato invalido!");
            }

            result = false;
        }
        return result;
    }

    private boolean validateHour(String time) {
        String regex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

        if (!time.isEmpty() && time.length() == 5) {
            int hours = Integer.parseInt(time.substring(0, 2));
            int minutes = Integer.parseInt(time.substring(3, 5));

            //Log.e("res", Boolean.toString(time.matches(regex)));
            return time.matches(regex) && 0 <= hours && hours <= 23 && 0 <= minutes && minutes <= 59;

        } else return false;
    }

}
