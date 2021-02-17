package android_team.gymme_client.trainer.manage_training_sheet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.customer.CustomerSmallObject;

public class CustomTrainerCustomerAdapter extends ArrayAdapter<CustomerSmallObject> implements Filterable {

    private static ArrayList<CustomerSmallObject> customers;
    private Activity context;

    public CustomTrainerCustomerAdapter(Activity _context, ArrayList<CustomerSmallObject> customers) {
        super(_context, R.layout.trainer_customer_item, customers);
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
        CustomTrainerCustomerAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.trainer_customer_item, null);
            viewHolder = new CustomTrainerCustomerAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomTrainerCustomerAdapter.ViewHolder) r.getTag();
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
                //Log.e("REDIRECT", "Trainer Training Sheet Customer Activity");
                Intent i = new Intent(context, TrainerTrainingSheetCustomer.class);
                //Log.e("User_id", user_id);
                i.putExtra("user_id", Integer.parseInt(user_id));
                //Log.e("Trainer_id",  ""+TrainerMenageTrainingSheet.getTrainerId());
                i.putExtra("trainer_id", TrainerMenageTrainingSheet.getTrainerId());
                context.startActivity(i);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_trainer_name, tv_gym_trainer_lastname, tv_gym_trainer_email;
        ImageView btn_gym_trainer_add;

        ViewHolder(View v) {
            tv_gym_trainer_name = v.findViewById(R.id.tv_trainer_customer_name);
            tv_gym_trainer_lastname = v.findViewById(R.id.tv_trainer_customer_lastname);
            tv_gym_trainer_email = v.findViewById(R.id.tv_trainer_customer_email);
            btn_gym_trainer_add = v.findViewById(R.id.btn_trainer_training_sheet_add);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<CustomerSmallObject> allCustomers = TrainerMenageTrainingSheet.getAllCustomers();
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
                    //Log.e("VALUES", results.values.toString());
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //Log.e("TEST", results.values.toString());
                customers = (ArrayList<CustomerSmallObject>) results.values;
                notifyDataSetChanged();
            }


        };
        return filter;
    }


}
