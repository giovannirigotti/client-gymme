package android_team.gymme_client.gym;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.nutritionist.NutritionistObject;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        String name = nutritionist.get(position).name;
        String lastname = nutritionist.get(position).lastname;
        String email = nutritionist.get(position).email;

        viewHolder.tv_gym_nutritionist_assumed_name.setText(name);
        viewHolder.tv_gym_nutritionist_assumed_lastname.setText(lastname);
        viewHolder.tv_gym_nutritionist_assumed_email.setText(email);

        viewHolder.btn_gym_nutritionist_assumed_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LOGICA AGGIUNTA Nutritionist SCELTO TRA I PROPRI DIPENDENDTI
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_nutritionist_assumed_name,tv_gym_nutritionist_assumed_lastname,tv_gym_nutritionist_assumed_email;
        ImageView btn_gym_nutritionist_assumed_add;

        ViewHolder(View v) {
            tv_gym_nutritionist_assumed_name = v.findViewById(R.id.tv_gym_nutritionist_assumed_name);
            tv_gym_nutritionist_assumed_lastname = v.findViewById(R.id.tv_gym_nutritionist_assumed_lastname);
            tv_gym_nutritionist_assumed_email = v.findViewById(R.id.tv_gym_nutritionist_assumed_email);

            btn_gym_nutritionist_assumed_add = v.findViewById(R.id.btn_gym_nutritionist_assumed_add);
        }
    }
}
