package android_team.gymme_client.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import android_team.gymme_client.local_database.local_dbmanager.DBManagerUser;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Utili;
import android_team.gymme_client.trainer.TrainerProfileActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerProfileActivity extends AppCompatActivity {

    private int user_id;
    private DBManagerUser dbManagerUser = null;

    private String nome;
    private String cognome;
    private String email;
    private String nascita;
    private Integer altezza;
    private String disturbi;
    private String allergie;

    @BindView(R.id.tv_customer_profile_nome)
    TextView _tv_customer_profile_nome;
    @BindView(R.id.tv_customer_profile_cognome)
    TextView _tv_customer_profile_cognome;
    @BindView(R.id.tv_customer_profile_nascita)
    TextView _tv_customer_profile_nascita;
    @BindView(R.id.tv_customer_profile_altezza)
    TextView _tv_customer_profile_altezza;
    @BindView(R.id.tv_customer_profile_email)
    TextView _tv_customer_profile_email;
    @BindView(R.id.tv_customer_profile_disturbi)
    TextView _tv_customer_profile_disturbi;
    @BindView(R.id.tv_customer_profile_allergie)
    TextView _tv_customer_profile_allergie;

    @BindView(R.id.btn_customer_profile_password)
    Button _btn_customer_profile_password;
    @BindView(R.id.btn_customer_profile_email)
    Button _btn_customer_profile_email;
    @BindView(R.id.btn_customer_profile_disturbi)
    Button _btn_customer_profile_disturbi;
    @BindView(R.id.btn_customer_profile_allergie)
    Button _btn_customer_profile_allergie;
    @BindView(R.id.btn_customer_profile_logout)
    Button _btn_customer_profile_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        ButterKnife.bind(this);
        dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();

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


        //Get User Data & Customer Data from DB
        GetDataSetView(user_id);

        //Update password menegment
        _btn_customer_profile_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword(CustomerProfileActivity.this, user_id);
            }
        });

        //Update email menegment
        _btn_customer_profile_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEmail(CustomerProfileActivity.this, user_id);
            }
        });

        //Update diseases menegment
        _btn_customer_profile_disturbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeDiseases(CustomerProfileActivity.this, user_id);
            }
        });

        //Update email menegment
        _btn_customer_profile_allergie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAllergies(CustomerProfileActivity.this, user_id);
            }
        });

        //Update email menegment
        _btn_customer_profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogout();
            }
        });


    }


    //LOGOUT
    private void DoLogout() {
        Utili.logout(CustomerProfileActivity.this);
        Intent intent = new Intent(CustomerProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //GET DATA CLASSES
    private void GetDataSetView(int user_id) {
        //User Data
        CustomerProfileActivity.GetUserDataConnection asyncTaskUser = (CustomerProfileActivity.GetUserDataConnection) new CustomerProfileActivity.GetUserDataConnection(new CustomerProfileActivity.GetUserDataConnection.AsyncResponse() {
            @Override
            public void processFinish(String _name, String _cognome, String _email, String _nascita) {

                if (_name.equals("error")) {
                    Toast.makeText(CustomerProfileActivity.this, "ERRORE CARICAMENTO DATI", Toast.LENGTH_SHORT).show();
                    Intent new_i = new Intent(CustomerProfileActivity.this, LoginActivity.class);
                    startActivity(new_i);
                    finish();
                } else if (!_name.equals("error")) {
                    nome = _name;
                    cognome = _cognome;
                    email = _email;
                    nascita = _nascita;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerProfileActivity.this, "Ben tornato", Toast.LENGTH_SHORT).show();
                            _tv_customer_profile_nome.setText(nome);
                            _tv_customer_profile_cognome.setText(cognome);
                            _tv_customer_profile_email.setText(email);
                            _tv_customer_profile_nascita.setText(nascita.split("T")[0]);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
        //Customer Data
        CustomerProfileActivity.GetCustomerDataConnection asyncTaskTrainer = (CustomerProfileActivity.GetCustomerDataConnection) new GetCustomerDataConnection(new GetCustomerDataConnection.AsyncResponse() {
            @Override
            public void processFinish(String _altezza, String _disturbi, String _allergie) {

                if (_altezza.equals("error")) {
                    Toast.makeText(CustomerProfileActivity.this, "ERRORE CARICAMENTO DATI", Toast.LENGTH_SHORT).show();
                    Intent new_i = new Intent(CustomerProfileActivity.this, LoginActivity.class);
                    startActivity(new_i);
                    finish();
                } else if (!_altezza.equals("error")) {
                    altezza = Integer.valueOf(_altezza);
                    disturbi = _disturbi;
                    allergie = _allergie;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            _tv_customer_profile_altezza.setText(String.valueOf(altezza) + "cm");
                            _tv_customer_profile_disturbi.setText(disturbi);
                            _tv_customer_profile_allergie.setText(allergie);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).execute(String.valueOf(user_id));
    }

    private static class GetUserDataConnection extends AsyncTask<String, String, JsonObject> {

        public interface AsyncResponse {
            void processFinish(String _name, String _cognome, String _email, String _nascita);
        }

        public CustomerProfileActivity.GetUserDataConnection.AsyncResponse delegate = null;

        public GetUserDataConnection(CustomerProfileActivity.GetUserDataConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;

            try {
                url = new URL("http://10.0.2.2:4000/user/get_all_data/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    //Log.e("Server user", responseString);
                    user = JsonParser.parseString(responseString).getAsJsonObject();
                    delegate.processFinish(user.get("name").getAsString(), user.get("lastname").getAsString(), user.get("email").getAsString(), user.get("birthdate").getAsString());

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish("error", "error", "error", "error");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return user;
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

    private static class GetCustomerDataConnection extends AsyncTask<String, String, JsonObject> {
        public interface AsyncResponse {
            void processFinish(String _altezza, String _disturbi, String _allergie);
        }

        public CustomerProfileActivity.GetCustomerDataConnection.AsyncResponse delegate = null;

        public GetCustomerDataConnection(CustomerProfileActivity.GetCustomerDataConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected JsonObject doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            String diseases, allergies;

            try {
                url = new URL("http://10.0.2.2:4000/customer/get_all_data/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.e("Server customer", responseString);
                    user = JsonParser.parseString(responseString).getAsJsonObject();

                    if (user.get("diseases").isJsonNull()) {
                        diseases = "";
                    } else {
                        diseases = user.get("diseases").getAsString();
                    }
                    if (user.get("allergies").isJsonNull()) {
                        allergies = "";
                    } else {
                        allergies = user.get("allergies").getAsString();
                    }

                    delegate.processFinish(user.get("height").getAsString(), diseases, allergies);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Server response", "HTTP_NOT_FOUND");
                    delegate.processFinish("error", "error", "error");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return user;
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

    //EMAIL
    private void ChangeEmail(Activity a, int user_id) {
        CustomerProfileActivity.CustomDialogEmailClass cdd = new CustomerProfileActivity.CustomDialogEmailClass(a, user_id);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogEmailClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Button Aggiorna, Esci;
        public EditText new_email, confirm_email;
        public Integer user_id;

        public CustomDialogEmailClass(Activity a, Integer user_id) {
            super(a);
            this.c = a;
            this.user_id = user_id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_update_email);
            Aggiorna = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_user_type_no);
            new_email = (EditText) findViewById(R.id.et_dialog_nuova_email);
            confirm_email = (EditText) findViewById(R.id.et_dialog_conferma_email);

            Aggiorna.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:
                    if (checkEmails()) {
                        UpdateEmailConnection asyncTask = (UpdateEmailConnection) new UpdateEmailConnection(new UpdateEmailConnection.AsyncResponse() {

                            @Override
                            public void processFinish(Integer output) {
                                if (output == 403) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(CustomerProfileActivity.this, "Email già usata. FORBIDDEN", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (output == 200) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(CustomerProfileActivity.this, "SUCCESS, email aggiornata", Toast.LENGTH_SHORT).show();
                                            _tv_customer_profile_email.setText(new_email.getText().toString());
                                        }
                                    });
                                    dismiss();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(CustomerProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        }).execute(String.valueOf(user_id), new_email.getText().toString());

                    } else {
                        Toast.makeText(getContext(), "Errore: \n- Campi vuoti\n- Email non coincidono\n- Email nuova non valida", Toast.LENGTH_LONG).show();
                    }

                    //

                    break;
                case R.id.dialog_confirm_user_type_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }

        private boolean checkEmails() {
            boolean res = false;
            String n_e, c_e;
            n_e = new_email.getText().toString();
            c_e = confirm_email.getText().toString();
            if (n_e.isEmpty() || c_e.isEmpty()) {
                res = false;
                Log.e("EMAIL", "Campi vuoti");
            } else {
                if (!validateMail(n_e)) {
                    res = false;
                    Log.e("EMAIL", "Email non valida.");
                } else {
                    if (!n_e.equals(c_e)) {
                        res = false;
                        Log.e("EMAIL", "Email non uguali");
                    } else {
                        res = true;
                        Log.e("Email", "Tutto OK");
                    }
                }
            }
            return res;
        }

        private boolean validateMail(String email) {
            String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
            return email.matches(regex);
        }

    }

    public static class UpdateEmailConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public AsyncResponse delegate = null;

        public UpdateEmailConnection(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/user/update_email");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("email", params[1]);

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
                    Log.e("EMAIL", "CAMBIATA SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    Log.e("EMAIL", "Error 403!");
                    responseCode = 403;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                } else {
                    Log.e("EMAIL", "Error");
                    responseCode = 500;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 69;
                delegate.processFinish(responseCode);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }
    }

    //DISTURBI
    private void ChangeDiseases(Activity a, int user_id) {
        CustomerProfileActivity.CustomDialogDiseasesClass cdd = new CustomerProfileActivity.CustomDialogDiseasesClass(a, user_id);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogDiseasesClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Button Aggiorna, Esci;
        public EditText diseases;
        public Integer user_id;

        public CustomDialogDiseasesClass(Activity a, Integer user_id) {
            super(a);
            this.c = a;
            this.user_id = user_id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_update_diseases);
            Aggiorna = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_user_type_no);
            diseases = (EditText) findViewById(R.id.et_dialog_diseases);
            diseases.setText(_tv_customer_profile_disturbi.getText().toString());

            Aggiorna.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:
                    UpdateDiseasesConnection asyncTask = (UpdateDiseasesConnection) new UpdateDiseasesConnection(new UpdateDiseasesConnection.AsyncResponse() {

                        @Override
                        public void processFinish(Integer output) {
                            if (output == 200) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(CustomerProfileActivity.this, "SUCCESS, disturbi aggiornati", Toast.LENGTH_SHORT).show();
                                        _tv_customer_profile_disturbi.setText(diseases.getText().toString());
                                    }
                                });
                                dismiss();
                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(CustomerProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                    }).execute(String.valueOf(user_id), diseases.getText().toString());


                    //

                    break;
                case R.id.dialog_confirm_user_type_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    public static class UpdateDiseasesConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public AsyncResponse delegate = null;

        public UpdateDiseasesConnection(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/customer/update_diseases");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("diseases", params[1]);

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
                    Log.e("DISTURBI", "CAMBIATI SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("DISTURBI", "Error");
                    responseCode = 500;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 69;
                delegate.processFinish(responseCode);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }
    }

    //ALLERGIE
    private void ChangeAllergies(Activity a, int user_id) {
        CustomerProfileActivity.CustomDialogAllergiesClass cdd = new CustomerProfileActivity.CustomDialogAllergiesClass(a, user_id);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogAllergiesClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Button Aggiorna, Esci;
        public EditText allergies;
        public Integer user_id;

        public CustomDialogAllergiesClass(Activity a, Integer user_id) {
            super(a);
            this.c = a;
            this.user_id = user_id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_update_allergies);
            Aggiorna = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_user_type_no);
            allergies = (EditText) findViewById(R.id.et_dialog_qualification);
            allergies.setText(_tv_customer_profile_allergie.getText().toString());

            Aggiorna.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:
                    UpdateAllergiesConnection asyncTask = (UpdateAllergiesConnection) new UpdateAllergiesConnection(new UpdateAllergiesConnection.AsyncResponse() {

                        @Override
                        public void processFinish(Integer output) {
                            if (output == 200) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(CustomerProfileActivity.this, "SUCCESS, allergie aggiornate", Toast.LENGTH_SHORT).show();
                                        _tv_customer_profile_allergie.setText(allergies.getText().toString());
                                    }
                                });
                                dismiss();
                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(CustomerProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                    }).execute(String.valueOf(user_id), allergies.getText().toString());


                    //

                    break;
                case R.id.dialog_confirm_user_type_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    public static class UpdateAllergiesConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public AsyncResponse delegate = null;

        public UpdateAllergiesConnection(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/customer/update_allergies");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("allergies", params[1]);

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
                    Log.e("ALLERGIE", "CAMBIATE SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("ALLERGIE", "Error");
                    responseCode = 500;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 69;
                delegate.processFinish(responseCode);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }
    }


    //PASSWORD
    private void ChangePassword(Activity a, int user_id) {
        CustomerProfileActivity.CustomDialogPasswordClass cdd = new CustomerProfileActivity.CustomDialogPasswordClass(a, user_id);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogPasswordClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public Button Aggiorna, Esci;
        public EditText old_password, new_password, confirm_password;
        public Integer user_id;

        public CustomDialogPasswordClass(Activity a, Integer user_id) {
            super(a);
            this.c = a;
            this.user_id = user_id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_update_password);
            Aggiorna = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_user_type_no);
            old_password = (EditText) findViewById(R.id.et_old_password);
            new_password = (EditText) findViewById(R.id.ed_new_password);
            confirm_password = (EditText) findViewById(R.id.et_confirm_password);

            Aggiorna.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:
                    if (checkInputs()) {
                        UpdatePasswordConnection asyncTask = (UpdatePasswordConnection) new UpdatePasswordConnection(new UpdatePasswordConnection.AsyncResponse() {

                            @Override
                            public void processFinish(Integer output) {
                                if (output == 403) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(CustomerProfileActivity.this, "Vecchia password non corrisponde", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (output == 200) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(CustomerProfileActivity.this, "SUCCESS, password aggiornata", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dismiss();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(CustomerProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        }).execute(String.valueOf(user_id), old_password.getText().toString(), new_password.getText().toString());

                    } else {
                        Toast.makeText(getContext(), "Errore: \nCampi vuoti\nPassword non coincidono\nPassword nuova non abbstanza camplicata", Toast.LENGTH_LONG).show();
                    }

                    //

                    break;
                case R.id.dialog_confirm_user_type_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }

        private boolean checkInputs() {
            boolean res = false;
            String o_p, n_p, c_p;
            o_p = old_password.getText().toString();
            n_p = new_password.getText().toString();
            c_p = confirm_password.getText().toString();
            if (o_p.isEmpty() || n_p.isEmpty() || c_p.isEmpty()) {
                res = false;
                Log.e("PASSWORD", "Campi vuoti");
            } else {
                if (!validatePassword(n_p)) {
                    res = false;
                    Log.e("PASSWORD", "Password non valida.");
                } else {
                    if (!n_p.equals(c_p)) {
                        res = false;
                        Log.e("PASSWORD", "Password non uguali");
                    } else {
                        res = true;
                        Log.e("PASSWORD", "Tutto OK");
                    }
                }
            }
            return res;
        }

        private boolean validatePassword(String password) {
            String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            return password.matches(regex);
        }
    }

    public static class UpdatePasswordConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public AsyncResponse delegate = null;

        public UpdatePasswordConnection(AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/user/update_password");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("old_password", params[1]);
                paramsJson.addProperty("new_password", params[2]);

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
                    Log.e("PASSWORD", "CAMBIATA SUL DB");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    Log.e("PASSWORD", "Error 500!");
                    responseCode = 500;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    Log.e("PASSWORD", "Error 403!");
                    responseCode = 403;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 69;
                delegate.processFinish(responseCode);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }
    }
}