package android_team.gymme_client.gym;

import android.app.Activity;
import android.util.Log;
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
import android_team.gymme_client.nutritionist.NutritionistObject;
import android_team.gymme_client.trainer.TrainerObject;

public class CustomGymNutritionistAdapter extends ArrayAdapter<NutritionistObject> implements Filterable {

    private ArrayList<NutritionistObject> nutritionist;
    private Activity context;

    public CustomGymNutritionistAdapter(Activity _context, ArrayList<NutritionistObject> _nutritionist) {
        super(_context, R.layout.notification_item, _nutritionist);
        this.context = _context;
        this.nutritionist = _nutritionist;
    }
    @Override
    public int getCount() {
        return nutritionist.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomGymNutritionistAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_nutritionist_item, null);
            viewHolder = new CustomGymNutritionistAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomGymNutritionistAdapter.ViewHolder) r.getTag();
        }
        String name = nutritionist.get(position).name;
        String lastname = nutritionist.get(position).lastname;
        String email = nutritionist.get(position).email;

        viewHolder.tv_gym_nutritionist_name.setText(name);
        viewHolder.tv_gym_nutritionist_lastname.setText(lastname);
        viewHolder.tv_gym_nutritionist_email.setText(email);

        viewHolder.btn_gym_nutritionist_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LOGICA AGGIUNTA Nutritionist SCELTO TRA I PROPRI DIPENDENDTI
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_nutritionist_name,tv_gym_nutritionist_lastname,tv_gym_nutritionist_email;
        ImageView btn_gym_nutritionist_add;

        ViewHolder(View v) {
            tv_gym_nutritionist_name = v.findViewById(R.id.tv_gym_nutritionist_name);
            tv_gym_nutritionist_lastname = v.findViewById(R.id.tv_gym_nutritionist_lastname);
            tv_gym_nutritionist_email = v.findViewById(R.id.tv_gym_nutritionist_email);

            btn_gym_nutritionist_add = v.findViewById(R.id.btn_gym_nutritionist_add);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<NutritionistObject> allNutritionist = GymAddNutritionistActivity.getAllNutritionist();
                if (constraint == null || constraint.length() == 0) {
                    results.values = allNutritionist;
                    results.count = allNutritionist.size();
                } else {
                    ArrayList<NutritionistObject> FilteredNutritionists = new ArrayList<NutritionistObject>();
                    // perform your search here using the searchConstraint String.
                    constraint = constraint.toString().toLowerCase();
                    for (NutritionistObject t : nutritionist) {
                        String dataNames = t.name;
                        if (dataNames.toLowerCase().startsWith(constraint.toString())) {
                            FilteredNutritionists.add(t);
                        }
                    }
                    results.values = FilteredNutritionists;
                    results.count = FilteredNutritionists.size();
                    Log.e("VALUES", results.values.toString());
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.e("TEST", results.values.toString());
                nutritionist = (ArrayList<NutritionistObject>) results.values;
                notifyDataSetChanged();
            }


        };
        return filter;
    }
}
