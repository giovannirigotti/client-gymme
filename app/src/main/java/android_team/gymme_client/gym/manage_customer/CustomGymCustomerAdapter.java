package android_team.gymme_client.gym.manage_customer;

import android.app.Activity;
import android.app.Dialog;
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
import android_team.gymme_client.customer.CustomerSmallObject;
import android_team.gymme_client.gym.manage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.support.MyApplication;

public class CustomGymCustomerAdapter extends ArrayAdapter<CustomerSmallObject> implements Filterable {

    private static ArrayList<CustomerSmallObject> customers;
    private Activity context;

    public CustomGymCustomerAdapter(Activity _context, ArrayList<CustomerSmallObject> customers) {
        super(_context, R.layout.notification_item, customers);
        this.context = _context;
        this.customers = customers;
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomGymCustomerAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_trainer_assumed_item, null);
            viewHolder = new CustomGymCustomerAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomGymCustomerAdapter.ViewHolder) r.getTag();
        }

        final String user_id = customers.get(position).getUser_id();
        final String name = customers.get(position).getName();
        final String lastname = customers.get(position).getLasname();
        final String email = customers.get(position).getEmail();
        final String birthdate = customers.get(position).getBirthdate();


        viewHolder.tv_gym_trainer_name.setText(name);
        viewHolder.tv_gym_trainer_lastname.setText(lastname);
        viewHolder.tv_gym_trainer_email.setText(email);

        viewHolder.btn_gym_trainer_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoCustomer(context, user_id, name, lastname, email, birthdate, position);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_trainer_name, tv_gym_trainer_lastname, tv_gym_trainer_email;
        ImageView btn_gym_trainer_add;

        ViewHolder(View v) {
            tv_gym_trainer_name = v.findViewById(R.id.tv_gym_trainer_assumed_name);
            tv_gym_trainer_lastname = v.findViewById(R.id.tv_gym_trainer_assumed_lastname);
            tv_gym_trainer_email = v.findViewById(R.id.tv_gym_trainer_assumed_email);
            btn_gym_trainer_add = v.findViewById(R.id.btn_gym_trainer_assumed_add);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<CustomerSmallObject> allCustomers = GymCustomersActivity.getAllCustomers();
                if (constraint == null || constraint.length() == 0) {
                    results.values = allCustomers;
                    results.count = allCustomers.size();
                } else {
                    ArrayList<CustomerSmallObject> FilteredCustomers = new ArrayList<CustomerSmallObject>();
                    // perform your search here using the searchConstraint String.
                    constraint = constraint.toString().toLowerCase();
                    for (CustomerSmallObject c : customers) {
                        String dataNames = c.getName();
                        if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                            FilteredCustomers.add(c);
                        }
                    }
                    results.values = FilteredCustomers;
                    results.count = FilteredCustomers.size();
                    Log.e("VALUES", results.values.toString());
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.e("TEST", results.values.toString());
                customers = (ArrayList<CustomerSmallObject>) results.values;
                notifyDataSetChanged();
            }


        };
        return filter;
    }

    public void InfoCustomer(Activity a, String user_id, String name, String lastname, String email, String birthdate, Integer position) {
        CustomGymCustomerAdapter.CustomDialogCustomerInfo cdd = new CustomGymCustomerAdapter.CustomDialogCustomerInfo(a, user_id, name, lastname, email, birthdate, position);
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
                    CustomGymCustomerAdapter.RemoveCustomerConncection asyncTask = (CustomGymCustomerAdapter.RemoveCustomerConncection) new CustomGymCustomerAdapter.RemoveCustomerConncection(new CustomGymCustomerAdapter.RemoveCustomerConncection.AsyncResponse() {
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

        public CustomGymCustomerAdapter.RemoveCustomerConncection.AsyncResponse delegate = null;

        public RemoveCustomerConncection(CustomGymCustomerAdapter.RemoveCustomerConncection.AsyncResponse delegate) {
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
