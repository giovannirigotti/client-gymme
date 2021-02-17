package android_team.gymme_client.signup;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Fx;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity2 extends AppCompatActivity {


    @BindView(R.id.dropdown_button_gym_user)
    ImageButton _dropdown_button_gym_user;
    @BindView(R.id.signup_gym_user_info)
    LinearLayout _signup_gym_user_info;
    @BindView(R.id.signup_button_gym_user)
    Button _signup_button_gym_user;
    @BindView(R.id.progress_bar_gym_user)
    ProgressBar _progress_bar_gym_user;

    @BindView(R.id.dropdown_button_gym_trainer)
    ImageButton _dropdown_button_gym_trainer;
    @BindView(R.id.signup_gym_trainer_info)
    LinearLayout _signup_gym_trainer_info;
    @BindView(R.id.signup_button_gym_trainer)
    Button _signup_button_gym_trainer;
    @BindView(R.id.progress_bar_gym_trainer)
    ProgressBar _progress_bar_gym_trainer;

    @BindView(R.id.dropdown_button_gym_nutritionist)
    ImageButton _dropdown_button_gym_nutritionist;
    @BindView(R.id.signup_gym_nutritionist_info)
    LinearLayout _signup_gym_nutritionist_info;
    @BindView(R.id.signup_button_gym_nutritionist)
    Button _signup_button_gym_nutritionist;
    @BindView(R.id.progress_bar_gym_nutritionist)
    ProgressBar _progress_bar_gym_nutritionist;

    @BindView(R.id.dropdown_button_gym_owner)
    ImageButton _dropdown_button_gym_owner;
    @BindView(R.id.signup_gym_owner_info)
    LinearLayout _signup_gym_owner_info;
    @BindView(R.id.signup_button_gym_owner)
    Button _signup_button_gym_owner;
    @BindView(R.id.progress_bar_gym_owner)
    ProgressBar _progress_bar_gym_owner;

    private static int workload = 12;

    private String email;
    private String name;
    private String lastname;
    private String birthdate;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signup_2);
        ButterKnife.bind(this);


        name = getIntent().getStringExtra("name");
        lastname = getIntent().getStringExtra("lastname");
        email = getIntent().getStringExtra("email");
        birthdate = getIntent().getStringExtra("birthdate");
        password = getIntent().getStringExtra("password");

        _signup_gym_user_info.setVisibility(View.GONE);
        _signup_gym_trainer_info.setVisibility(View.GONE);
        _signup_gym_nutritionist_info.setVisibility(View.GONE);
        _signup_gym_owner_info.setVisibility(View.GONE);


        _dropdown_button_gym_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownGymUser();
            }
        });
        _dropdown_button_gym_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownGymTrainer();
            }
        });
        _dropdown_button_gym_nutritionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownNutritionist();
            }
        });
        _dropdown_button_gym_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownGymOwner();
            }
        });

        /////////////Signup Listeners

        _signup_button_gym_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(SignupActivity2.this, "Utente Palestra", 0);
            }
        });
        _signup_button_gym_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(SignupActivity2.this, "Personal Trainer", 1);
            }
        });

        _signup_button_gym_nutritionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(SignupActivity2.this, "Nutrizionista", 2);
            }
        });

        _signup_button_gym_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(SignupActivity2.this, "Proprietario palestra", 3);
            }
        });
    }

    private void dropdownGymUser() {
        if (_signup_gym_user_info.isShown()) {
            Fx.slide_up(getApplicationContext(), _signup_gym_user_info);
            _signup_gym_user_info.setVisibility(View.GONE);
            _dropdown_button_gym_user.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
        } else {
            if (_signup_gym_trainer_info.isShown())
                hideInfo(_signup_gym_trainer_info, _dropdown_button_gym_trainer);
            if (_signup_gym_nutritionist_info.isShown())
                hideInfo(_signup_gym_nutritionist_info, _dropdown_button_gym_nutritionist);
            if (_signup_gym_owner_info.isShown())
                hideInfo(_signup_gym_owner_info, _dropdown_button_gym_owner);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Fx.slide_down(getApplicationContext(), _signup_gym_user_info);
                    _signup_gym_user_info.setVisibility(View.VISIBLE);
                    _dropdown_button_gym_user.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
                }
            }, 100);
        }
    }

    private void dropdownGymTrainer() {
        if (_signup_gym_trainer_info.isShown()) {
            Fx.slide_up(getApplicationContext(), _signup_gym_trainer_info);
            _signup_gym_trainer_info.setVisibility(View.GONE);
            _dropdown_button_gym_trainer.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
        } else {

            if (_signup_button_gym_user.isShown())
                hideInfo(_signup_gym_user_info, _dropdown_button_gym_user);
            if (_signup_gym_nutritionist_info.isShown())
                hideInfo(_signup_gym_nutritionist_info, _dropdown_button_gym_nutritionist);
            if (_signup_gym_owner_info.isShown())
                hideInfo(_signup_gym_owner_info, _dropdown_button_gym_owner);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Fx.slide_down(getApplicationContext(), _signup_gym_trainer_info);
                    _signup_gym_trainer_info.setVisibility(View.VISIBLE);
                    _dropdown_button_gym_trainer.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
                }
            }, 100);
        }
    }

    private void dropdownNutritionist() {
        if (_signup_gym_nutritionist_info.isShown()) {
            Fx.slide_up(getApplicationContext(), _signup_gym_nutritionist_info);
            _signup_gym_nutritionist_info.setVisibility(View.GONE);
            _dropdown_button_gym_nutritionist.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
        } else {
            if (_signup_gym_trainer_info.isShown())
                hideInfo(_signup_gym_trainer_info, _dropdown_button_gym_trainer);
            if (_signup_button_gym_user.isShown())
                hideInfo(_signup_gym_user_info, _dropdown_button_gym_user);
            if (_signup_gym_owner_info.isShown())
                hideInfo(_signup_gym_owner_info, _dropdown_button_gym_owner);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Fx.slide_down(getApplicationContext(), _signup_gym_nutritionist_info);
                    _signup_gym_nutritionist_info.setVisibility(View.VISIBLE);
                    _dropdown_button_gym_nutritionist.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
                }
            }, 100);

        }
    }

    private void dropdownGymOwner() {
        if (_signup_gym_owner_info.isShown()) {
            Fx.slide_up(getApplicationContext(), _signup_gym_owner_info);
            _signup_gym_owner_info.setVisibility(View.GONE);
            _dropdown_button_gym_owner.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
        } else {
            if (_signup_gym_trainer_info.isShown())
                hideInfo(_signup_gym_trainer_info, _dropdown_button_gym_trainer);
            if (_signup_gym_nutritionist_info.isShown())
                hideInfo(_signup_gym_nutritionist_info, _dropdown_button_gym_nutritionist);
            if (_signup_button_gym_user.isShown())
                hideInfo(_signup_gym_user_info, _dropdown_button_gym_user);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Fx.slide_down(getApplicationContext(), _signup_gym_owner_info);
                    _signup_gym_owner_info.setVisibility(View.VISIBLE);
                    _dropdown_button_gym_owner.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
                }
            }, 100);
        }
    }


    private class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        public TextView _userType;
        String userType;
        int type;

        public CustomDialogClass(Activity a, String userType, int type) {
            super(a);
            this.c = a;
            this.userType = userType;
            this.type = type;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_confirm_user_type);
            yes = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            no = (Button) findViewById(R.id.dialog_confirm_user_type_no);
            _userType = (TextView) findViewById(R.id.dialog_confirm_user_type_text);
            _userType.setText(userType + " ?");
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:
                    dismiss();
                    signUp(name, lastname, email, birthdate, password, type);
                    break;
                case R.id.dialog_confirm_user_type_no:
                    stopSpinner(null);
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private void showDialog(Activity a, String userType, int type) {
        startSpinner(type);
        CustomDialogClass cdd = new CustomDialogClass(a, userType, type);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                stopSpinner(null);
            }
        });
        cdd.show();
    }

    private void hideInfo(LinearLayout layout, ImageButton button) {
        Fx.slide_up_0(getApplicationContext(), layout);
        layout.setVisibility(View.GONE);
        button.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
    }

    private void disableButtons() {

        _signup_button_gym_user.setEnabled(false);
        _signup_button_gym_trainer.setEnabled(false);
        _signup_button_gym_nutritionist.setEnabled(false);
        _signup_button_gym_owner.setEnabled(false);

        _dropdown_button_gym_user.setEnabled(false);
        _dropdown_button_gym_trainer.setEnabled(false);
        _dropdown_button_gym_nutritionist.setEnabled(false);
        _dropdown_button_gym_owner.setEnabled(false);

    }

    private void enableButtons() {

        _signup_button_gym_user.setEnabled(true);
        _signup_button_gym_trainer.setEnabled(true);
        _signup_button_gym_nutritionist.setEnabled(true);
        _signup_button_gym_owner.setEnabled(true);

        _dropdown_button_gym_user.setEnabled(true);
        _dropdown_button_gym_trainer.setEnabled(true);
        _dropdown_button_gym_nutritionist.setEnabled(true);
        _dropdown_button_gym_owner.setEnabled(true);

    }

    private void startSpinner(int type) {

        if (type == 0) {

            _signup_button_gym_user.setVisibility(View.GONE);
            _progress_bar_gym_user.setVisibility(View.VISIBLE);

        } else if (type == 1) {

            _signup_button_gym_trainer.setVisibility(View.GONE);
            _progress_bar_gym_trainer.setVisibility(View.VISIBLE);

        } else if (type == 2) {

            _signup_button_gym_nutritionist.setVisibility(View.GONE);
            _progress_bar_gym_nutritionist.setVisibility(View.VISIBLE);

        } else if (type == 3) {

            _signup_button_gym_owner.setVisibility(View.GONE);
            _progress_bar_gym_owner.setVisibility(View.VISIBLE);

        }
    }

    private void stopSpinner(String toastMessage) {

        _signup_button_gym_user.setVisibility(View.VISIBLE);
        _progress_bar_gym_user.setVisibility(View.GONE);
        _signup_button_gym_user.setEnabled(true);

        _signup_button_gym_trainer.setVisibility(View.VISIBLE);
        _progress_bar_gym_trainer.setVisibility(View.GONE);
        _signup_button_gym_trainer.setEnabled(true);

        _signup_button_gym_nutritionist.setVisibility(View.VISIBLE);
        _progress_bar_gym_nutritionist.setVisibility(View.GONE);
        _signup_button_gym_nutritionist.setEnabled(true);


        _signup_button_gym_owner.setVisibility(View.VISIBLE);
        _progress_bar_gym_owner.setVisibility(View.GONE);
        _signup_button_gym_owner.setEnabled(true);

        if (toastMessage != null) {
            Toast responseToast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);
            responseToast.show();

        }
    }


    private void signUp(String name, String lastname, String email, String birthdate, String password, int type) {
        disableButtons();
        startSpinner(type);
        new RegisterBaseUserConnection().execute(name, lastname, email, birthdate, password, Integer.toString(type));

    }

    private class RegisterBaseUserConnection extends AsyncTask<String, String, Integer> {

        String toastMessage = null;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/register/user");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("name", params[0]);
                paramsJson.addProperty("lastname", params[1]);
                paramsJson.addProperty("email", params[2]);
                paramsJson.addProperty("birthdate", params[3]);
                paramsJson.addProperty("password", params[4]);
                paramsJson.addProperty("user_type", params[5]);


                //Log.e("json", paramsJson.toString());

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
                    //Log.e("ok", "ok");
                    new CheckUserDataConnection().execute(email);
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
            enableButtons();
            stopSpinner(toastMessage);

        }
    }


    private class CheckUserDataConnection extends AsyncTask<String, String, JsonObject> {

        String toastMessage = null;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;

            try {
                url = new URL("http://10.0.2.2:4000/get_user_data/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    String responseString = readStream(urlConnection.getInputStream());
                    user = JsonParser.parseString(responseString).getAsJsonObject();

                    int user_type = user.get("user_type").getAsInt();

                    ////aggiungere i reindirizzamenti alle pagine di signup dati specifici in base agli altir tipi di utente

                    switch (user_type) {
                        case 0: //customer
                            Intent intentCustomer = new Intent(getApplicationContext(), CustomerSignupActivity.class);
                            intentCustomer.putExtra("name", name);
                            intentCustomer.putExtra("lastname", lastname);
                            intentCustomer.putExtra("email", email);
                            intentCustomer.putExtra("birthdate", birthdate);
                            intentCustomer.putExtra("password", password);
                            intentCustomer.putExtra("user_id", user.get("user_id").getAsInt());
                            startActivity(intentCustomer);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 1: //trainer
                            Intent intentTrainer = new Intent(getApplicationContext(), TrainerSignupActivity.class);
                            intentTrainer.putExtra("name", name);
                            intentTrainer.putExtra("lastname", lastname);
                            intentTrainer.putExtra("email", email);
                            intentTrainer.putExtra("birthdate", birthdate);
                            intentTrainer.putExtra("password", password);
                            intentTrainer.putExtra("user_id", user.get("user_id").getAsInt());
                            startActivity(intentTrainer);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 2: //nutrituionist
                            Intent intentNutritionist = new Intent(getApplicationContext(), NutritionistSignupActivity.class);
                            intentNutritionist.putExtra("name", name);
                            intentNutritionist.putExtra("lastname", lastname);
                            intentNutritionist.putExtra("email", email);
                            intentNutritionist.putExtra("birthdate", birthdate);
                            intentNutritionist.putExtra("password", password);
                            intentNutritionist.putExtra("user_id", user.get("user_id").getAsInt());
                            startActivity(intentNutritionist);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 3: //gym
                            Intent intentGym = new Intent(getApplicationContext(), GymSignupActivity.class);
                            intentGym.putExtra("name", name);
                            intentGym.putExtra("lastname", lastname);
                            intentGym.putExtra("email", email);
                            intentGym.putExtra("birthdate", birthdate);
                            intentGym.putExtra("password", password);
                            intentGym.putExtra("user_id", user.get("user_id").getAsInt());
                            startActivity(intentGym);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        default:
                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            Toast.makeText(getApplicationContext(), "Errore generico!", Toast.LENGTH_LONG).show();
                    }

                    if (user_type == 0) {
                        Intent intent = new Intent(getApplicationContext(), CustomerSignupActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("lastname", lastname);
                        intent.putExtra("email", email);
                        intent.putExtra("birthdate", birthdate);
                        intent.putExtra("password", password);
                        intent.putExtra("user_id", user.get("user_id").getAsInt());

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }


                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    toastMessage = "Errore del server!";
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

            Toast responseToast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);
            if (user == null) {
                responseToast.show();
            }
            enableButtons();

        }


        private String readStream(InputStream in) throws UnsupportedEncodingException {

            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();

            try {

                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                if (reader != null) {


                    try {

                        reader.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            return response.toString();
        }

    }

}







