package android_team.gymme_client.customer;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.GymObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomCustomerGymAdapter extends ArrayAdapter<GymObject>  implements Filterable {

    private ArrayList<GymObject> gym;
    private Activity context;

    public CustomCustomerGymAdapter(Activity _context, ArrayList<GymObject> _gym) {
        super(_context, R.layout.customer_gym_item, _gym);
        this.context = _context;
        this.gym = _gym;
    }

    @Override
    public int getCount() {
        return gym.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomCustomerGymAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.customer_gym_item, null);
            viewHolder = new  CustomCustomerGymAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = ( CustomCustomerGymAdapter.ViewHolder) r.getTag();
        }
        String gym_name = gym.get(position).gym_name;
        String gym_address = gym.get(position).gym_address;


        viewHolder.tv_customer_gym_item_name.setText(gym_name);
        viewHolder.tv_customer_gym_item_address.setText(gym_address);


        return r;
    }

    class ViewHolder {
        TextView tv_customer_gym_item_name,tv_customer_gym_item_address;

        ViewHolder(View v) {
            tv_customer_gym_item_name = v.findViewById(R.id.tv_customer_gym_item_name);
            tv_customer_gym_item_address = v.findViewById(R.id.tv_customer_gym_item_address);


        }
    }

    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<GymObject> allGyms = CustomerAddGymActivity.getAllGyms();
                if (constraint == null || constraint.length() == 0) {
                    results.values = allGyms;
                    results.count = allGyms.size();
                } else {
                    ArrayList<GymObject> FilteredGyms = new ArrayList<GymObject>();
                    // perform your search here using the searchConstraint String.
                    constraint = constraint.toString().toLowerCase();
                    for (GymObject g : gym) {
                        String dataNames = g.gym_name;

                        Log.e("VALUES GYM NAME", g.gym_name);
                        if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                            FilteredGyms.add(g);
                            Log.e("ADD", g.gym_name);
                        }
                    }
                    results.values = FilteredGyms;
                    results.count = FilteredGyms.size();
                    Log.e("VALUES", results.values.toString());
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.e("TEST", results.values.toString());
                gym = (ArrayList<GymObject>) results.values;
                notifyDataSetChanged();
            }


        };
        return filter;
    }
}

