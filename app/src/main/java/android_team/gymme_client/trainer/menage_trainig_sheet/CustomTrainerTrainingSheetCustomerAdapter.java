package android_team.gymme_client.trainer.menage_trainig_sheet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.customer.CustomerSmallObject;
import android_team.gymme_client.gym.menage_customer.GymCustomersActivity;
import android_team.gymme_client.gym.menage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.support.MyApplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomTrainerTrainingSheetCustomerAdapter extends ArrayAdapter<TrainingSheetObject>  {

    private static ArrayList<TrainingSheetObject> sheets;
    private Activity context;

    public CustomTrainerTrainingSheetCustomerAdapter(Activity _context, ArrayList<TrainingSheetObject> sheets) {
        super(_context, R.layout.training_sheet_item, sheets);
        this.context = _context;
        this.sheets = sheets;
    }

    @Override
    public int getCount() {
        return sheets.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomTrainerTrainingSheetCustomerAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.training_sheet_item, null);
            viewHolder = new CustomTrainerTrainingSheetCustomerAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomTrainerTrainingSheetCustomerAdapter.ViewHolder) r.getTag();
        }

        final String training_sheet_id = sheets.get(position).getTraining_sheet_id();
        final String customer_id = sheets.get(position).getCustomer_id();
        final String trainer_id = sheets.get(position).getTrainer_id();
        final String creation_date = sheets.get(position).getCreation_date();
        final String title = sheets.get(position).getTitle();
        final String description = sheets.get(position).getDescription();
        final String number_of_days = sheets.get(position).getNumber_of_days();
        final String strength = sheets.get(position).getStrength();

        viewHolder.tv_title.setText(title);
        viewHolder.tv_date.setText(creation_date);
        viewHolder.tv_days.setText(number_of_days);

        viewHolder.btn_sheet_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_title, tv_date, tv_days;
        ImageView btn_sheet_info;

        ViewHolder(View v) {
            tv_title = v.findViewById(R.id.tv_sheet_item_title);
            tv_date = v.findViewById(R.id.tv_sheet_item_date);
            tv_days = v.findViewById(R.id.tv_sheet_item_days);
            btn_sheet_info = v.findViewById(R.id.btn_sheet_item_info);
        }
    }


    public void InfoCustomer(Activity a, String user_id, String name, String lastname, String email, String birthdate, Integer position) {
        CustomTrainerTrainingSheetCustomerAdapter.CustomDialogCustomerInfo cdd = new CustomTrainerTrainingSheetCustomerAdapter.CustomDialogCustomerInfo(a, user_id, name, lastname, email, birthdate, position);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogCustomerInfo extends Dialog implements View.OnClickListener {

        public Activity c;
        public Button Rimouvi, Esci;
        public TextView _name, _lastname, _email, _birthdate;
        public String user_id, name, lastname, email, birthdate;
        Integer position;

        public CustomDialogCustomerInfo(Activity a, String user_id, String name, String lastname, String email, String birthdate, Integer position) {
            super(a);
            this.c = a;
            this.user_id = user_id;
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.birthdate= birthdate;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_info_costumer);
            Rimouvi = (Button) findViewById(R.id.dialog_remove_customer);
            Esci = (Button) findViewById(R.id.dialog_exit_from_info_cutomer);

            _name = (TextView) findViewById(R.id.tv_dismiss_name);
            _lastname = (TextView) findViewById(R.id.tv_dismiss_lastname);
            _email = (TextView) findViewById(R.id.tv_dismiss_email);
            _birthdate = (TextView) findViewById(R.id.tv_dismiss_birthdate);

            _name.setText(name);
            _lastname.setText(lastname);
            _email.setText(email);
            _birthdate.setText(birthdate.split("T")[0]);

            Rimouvi.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.dialog_remove_customer:
                    CustomTrainerTrainingSheetCustomerAdapter.RemoveCustomerConncection asyncTask = (CustomTrainerTrainingSheetCustomerAdapter.RemoveCustomerConncection) new CustomTrainerTrainingSheetCustomerAdapter.RemoveCustomerConncection(new CustomTrainerTrainingSheetCustomerAdapter.RemoveCustomerConncection.AsyncResponse() {
                        @Override
                        public void processFinish(Integer output) {
                            if (output == 200) {
                                GymMenageWorkerActivity.runOnUI(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "SUCCESS, Cliente rimosso", Toast.LENGTH_SHORT).show();
                                        GymCustomersActivity.redirectManage(context);
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
                    }).execute(user_id, GymCustomersActivity.getGymId());
                    dismiss();
                    break;
                case R.id.dialog_exit_from_info_cutomer:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }

    }

    public static class RemoveCustomerConncection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public CustomTrainerTrainingSheetCustomerAdapter.RemoveCustomerConncection.AsyncResponse delegate = null;

        public RemoveCustomerConncection(CustomTrainerTrainingSheetCustomerAdapter.RemoveCustomerConncection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/gym/delete_gym_customer/");
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
                    Log.e("GYM CUSTOMER", "Cancellazione ok");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("GYM CUSTOMER", "Error cancellazione");
                    responseCode = 500;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 69;
                Log.e("GYM CUSTOMER", "Error I/O");
                delegate.processFinish(responseCode);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }

    }
}
