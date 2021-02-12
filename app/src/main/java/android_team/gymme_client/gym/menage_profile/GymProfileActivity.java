package android_team.gymme_client.gym.menage_profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.DrawerListener;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.support.Utili;

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

public class GymProfileActivity extends AppCompatActivity {

    private int user_id;

    DrawerListener drawerListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    TextView tv_gym_nome, tv_gym_cognome, tv_gym_nascita, tv_gym_email;
    String nome, cognome, nascita, email;

    Button btn_gym_email, btn_gym_password, btn_gym_logout, btn_gym_orari, btn_gym_dati_palestra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);

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

        tv_gym_nome = (TextView) findViewById(R.id.tv_gym_nome);
        tv_gym_cognome = (TextView) findViewById(R.id.tv_gym_cognome);
        tv_gym_nascita = (TextView) findViewById(R.id.tv_gym_nascita);
        tv_gym_email = (TextView) findViewById(R.id.tv_gym_email);

        btn_gym_email = (Button) findViewById(R.id.btn_gym_email);
        btn_gym_password = (Button) findViewById(R.id.btn_gym_password);
        btn_gym_orari = (Button) findViewById(R.id.btn_gym_orari);
        btn_gym_dati_palestra = (Button) findViewById(R.id.btn_gym_dati_palestra);
        btn_gym_logout = (Button) findViewById(R.id.btn_gym_logout);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_gym_activity);
        drawerListener = new DrawerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("PROFILO");

        //Get User Data & Customer Data from DB
        GetDataSetView(user_id);

        //Update email menegment
        btn_gym_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEmail(GymProfileActivity.this, user_id);
            }
        });

        //logout menegment
        btn_gym_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogout();
            }
        });

        //Update password menegment
        btn_gym_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword(GymProfileActivity.this, user_id);
            }
        });

        //Redirect menagment orari
        btn_gym_orari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Profile Activity");
                Intent i = new Intent(getApplicationContext(), GymEditHoursActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        //Redirect menagment dati palestra
        btn_gym_dati_palestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Edit Data Activity");
                Intent i = new Intent(getApplicationContext(), GymEditDataActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });


    }

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

    public void gymToCorsi(View view){
        drawerListener.toCourse();
    }
    public void gymToClienti(View view){
        drawerListener.toCustomer();
    }
    public void gymToDipendenti(View view){
        drawerListener.toEmployees();
    }
    public void gymToProfilo(View view){
        drawerListener.toProfile();
    }
    public void gymToHome(View view){
        drawerListener.toHome();
    }
    //endregion

    //USER DATA
    private void GetDataSetView(int user_id) {
        //User Data
        GymProfileActivity.GetUserDataConnection asyncTaskUser = (GymProfileActivity.GetUserDataConnection) new GymProfileActivity.GetUserDataConnection(new GymProfileActivity.GetUserDataConnection.AsyncResponse() {
            @Override
            public void processFinish(String _name, String _cognome, String _email, String _nascita) {

                if (_name.equals("error")) {
                    Toast.makeText(GymProfileActivity.this, "ERRORE CARICAMENTO DATI", Toast.LENGTH_SHORT).show();
                    Intent new_i = new Intent(GymProfileActivity.this, LoginActivity.class);
                    startActivity(new_i);
                    finish();
                } else if (!_name.equals("error")) {
                    nome = _name;
                    cognome = _cognome;
                    email = _email;
                    nascita = _nascita;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymProfileActivity.this, "Ben tornato", Toast.LENGTH_SHORT).show();
                            tv_gym_nome.setText(nome);
                            tv_gym_cognome.setText(cognome);
                            tv_gym_email.setText(email);
                            tv_gym_nascita.setText(nascita.split("T")[0]);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
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

        public GymProfileActivity.GetUserDataConnection.AsyncResponse delegate = null;

        public GetUserDataConnection(GymProfileActivity.GetUserDataConnection.AsyncResponse delegate) {
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
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.e("Server user response", responseString);
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

    //EMAIL
    private void ChangeEmail(Activity a, int user_id) {
        GymProfileActivity.CustomDialogEmailClass cdd = new GymProfileActivity.CustomDialogEmailClass(a, user_id);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogEmailClass extends Dialog implements View.OnClickListener {

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
                        GymProfileActivity.UpdateEmailConnection asyncTask = (GymProfileActivity.UpdateEmailConnection) new GymProfileActivity.UpdateEmailConnection(new GymProfileActivity.UpdateEmailConnection.AsyncResponse() {

                            @Override
                            public void processFinish(Integer output) {
                                if (output == 403) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(GymProfileActivity.this, "Email già usata. FORBIDDEN", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (output == 200) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(GymProfileActivity.this, "SUCCESS, email aggiornata", Toast.LENGTH_SHORT).show();
                                            tv_gym_email.setText(new_email.getText().toString());
                                        }
                                    });
                                    dismiss();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(GymProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
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

        public GymProfileActivity.UpdateEmailConnection.AsyncResponse delegate = null;

        public UpdateEmailConnection(GymProfileActivity.UpdateEmailConnection.AsyncResponse delegate) {
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


    //PASSWORD
    private void ChangePassword(Activity a, int user_id) {
        GymProfileActivity.CustomDialogPasswordClass cdd = new GymProfileActivity.CustomDialogPasswordClass(a, user_id);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogPasswordClass extends Dialog implements View.OnClickListener {

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
                        GymProfileActivity.UpdatePasswordConnection asyncTask = (GymProfileActivity.UpdatePasswordConnection) new GymProfileActivity.UpdatePasswordConnection(new GymProfileActivity.UpdatePasswordConnection.AsyncResponse() {

                            @Override
                            public void processFinish(Integer output) {
                                if (output == 403) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(GymProfileActivity.this, "Vecchia password non corrisponde", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (output == 200) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(GymProfileActivity.this, "SUCCESS, password aggiornata", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dismiss();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(GymProfileActivity.this, "ERRORE, server side", Toast.LENGTH_SHORT).show();
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

        public GymProfileActivity.UpdatePasswordConnection.AsyncResponse delegate = null;

        public UpdatePasswordConnection(GymProfileActivity.UpdatePasswordConnection.AsyncResponse delegate) {
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


    //LOGOUT
    private void DoLogout() {
        Utili.logout(GymProfileActivity.this);
        Intent intent = new Intent(GymProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}