package android_team.gymme_client.gym.manage_worker;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.nutritionist.NutritionistObject;
import android_team.gymme_client.support.MyApplication;

public class CustomGymNutritionistAssumedAdapter extends ArrayAdapter<NutritionistObject> {

    private ArrayList<NutritionistObject> nutritionist;
    private Activity context;

    public CustomGymNutritionistAssumedAdapter(Activity _context, ArrayList<NutritionistObject> _nutritionist) {
        super(_context, R.layout.notification_item, _nutritionist);
        this.context = _context;
        this.nutritionist = _nutritionist;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomGymNutritionistAssumedAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_nutritionist_assumed_item, null);
            viewHolder = new CustomGymNutritionistAssumedAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomGymNutritionistAssumedAdapter.ViewHolder) r.getTag();
        }

        final String nutritionist_id = nutritionist.get(position).user_id;
        final String name = nutritionist.get(position).name;
        final String lastname = nutritionist.get(position).lastname;
        final String email = nutritionist.get(position).email;
        final String qualification = nutritionist.get(position).qualification;
        final String fiscal_code = nutritionist.get(position).fiscal_code;

        viewHolder.tv_gym_nutritionist_assumed_name.setText(name);
        viewHolder.tv_gym_nutritionist_assumed_lastname.setText(lastname);
        viewHolder.tv_gym_nutritionist_assumed_email.setText(email);

        viewHolder.btn_gym_nutritionist_assumed_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissNutritionist(context, nutritionist_id, name, lastname, email, qualification, fiscal_code, position);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_nutritionist_assumed_name, tv_gym_nutritionist_assumed_lastname, tv_gym_nutritionist_assumed_email;
        ImageView btn_gym_nutritionist_assumed_add;

        ViewHolder(View v) {
            tv_gym_nutritionist_assumed_name = v.findViewById(R.id.tv_gym_nutritionist_assumed_name);
            tv_gym_nutritionist_assumed_lastname = v.findViewById(R.id.tv_gym_nutritionist_assumed_lastname);
            tv_gym_nutritionist_assumed_email = v.findViewById(R.id.tv_gym_nutritionist_assumed_email);

            btn_gym_nutritionist_assumed_add = v.findViewById(R.id.btn_gym_nutritionist_assumed_remove);
        }
    }

    public void dismissNutritionist(Activity a, String nutritionist_id, String name, String lastname, String email, String qualification, String fiscal_code, Integer position) {
        CustomGymNutritionistAssumedAdapter.CustomDialogRemoveNutritionist cdd = new CustomGymNutritionistAssumedAdapter.CustomDialogRemoveNutritionist(a, nutritionist_id, name, lastname, email, qualification, fiscal_code, position);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogRemoveNutritionist extends Dialog implements View.OnClickListener {

        public Activity c;
        public Button Licenzia, Esci;
        public TextView _name, _lastname, _email, _qualification, _fiscal_code;
        public String nutritionist_id, name, lastname, email, qualification, fiscal_code;
        Integer position;

        public CustomDialogRemoveNutritionist(Activity a, String nutritionist_id, String name, String lastname, String email, String qualification, String fiscal_code, Integer position) {
            super(a);
            this.c = a;
            this.nutritionist_id = nutritionist_id;
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.qualification = qualification;
            this.fiscal_code = fiscal_code;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_dismiss_nutritionist);
            Licenzia = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_user_type_no);

            _name = (TextView) findViewById(R.id.tv_dismiss_name);
            _lastname = (TextView) findViewById(R.id.tv_dismiss_lastname);
            _email = (TextView) findViewById(R.id.tv_dismiss_email);
            _qualification = (TextView) findViewById(R.id.tv_dismiss_qualification);
            _fiscal_code = (TextView) findViewById(R.id.tv_dismiss_fiscal_code);

            _name.setText(name);
            _lastname.setText(lastname);
            _email.setText(email);
            _qualification.setText(qualification);
            _fiscal_code.setText(fiscal_code);

            Licenzia.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:
                    CustomGymNutritionistAssumedAdapter.DismissNutritionistConnection asyncTask = (CustomGymNutritionistAssumedAdapter.DismissNutritionistConnection) new CustomGymNutritionistAssumedAdapter.DismissNutritionistConnection(new CustomGymNutritionistAssumedAdapter.DismissNutritionistConnection.AsyncResponse() {
                        @Override
                        public void processFinish(Integer output) {
                            if (output == 200) {
                                GymMenageWorkerActivity.runOnUI(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "Nutritionist licenziato", Toast.LENGTH_SHORT).show();
                                        GymMenageWorkerActivity.redoAdapterNutri(context, nutritionist, position);
                                    }
                                });
                            } else {
                                GymMenageWorkerActivity.runOnUI(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "ERRORE, server side", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).execute(nutritionist_id, GymMenageWorkerActivity.getGymId());
                    dismiss();
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

    public static class DismissNutritionistConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public CustomGymNutritionistAssumedAdapter.DismissNutritionistConnection.AsyncResponse delegate = null;

        public DismissNutritionistConnection(CustomGymNutritionistAssumedAdapter.DismissNutritionistConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/gym/dismiss_nutritionist/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("gym_id", params[1]);

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
                    //Log.e("GYM TRAINER", "LICENZIATO OK");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    //Log.e("GYM TRAINER", "Error");
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
}
