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
import android_team.gymme_client.trainer.TrainerObject;

public class CustomGymTrainerAdapter extends ArrayAdapter<TrainerObject> {

    private ArrayList<TrainerObject> trainers;
    private Activity context;

    public CustomGymTrainerAdapter(Activity _context, ArrayList<TrainerObject> _trainers) {
        super(_context, R.layout.notification_item, _trainers);
        this.context = _context;
        this.trainers = _trainers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomGymTrainerAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_trainer_item, null);
            viewHolder = new CustomGymTrainerAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomGymTrainerAdapter.ViewHolder) r.getTag();
        }
        String name = trainers.get(position).name;
        String lastname = trainers.get(position).lastname;
        String email = trainers.get(position).email;

        viewHolder.tv_gym_trainer_name.setText(name);
        viewHolder.tv_gym_trainer_lastname.setText(lastname);
        viewHolder.tv_gym_trainer_email.setText(email);

        viewHolder.btn_gym_trainer_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LOGICA AGGIUNTA TRAINER SCELTO TRA I PROPRI DIPENDENDTI
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_trainer_name,tv_gym_trainer_lastname,tv_gym_trainer_email;
        ImageView btn_gym_trainer_add;

        ViewHolder(View v) {
            tv_gym_trainer_name = v.findViewById(R.id.tv_gym_trainer_name);
            tv_gym_trainer_lastname = v.findViewById(R.id.tv_gym_trainer_lastname);
            tv_gym_trainer_email = v.findViewById(R.id.tv_gym_trainer_email);

            btn_gym_trainer_add = v.findViewById(R.id.btn_gym_trainer_add);
        }
    }
}
