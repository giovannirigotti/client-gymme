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

public class CustomGymTrainerAssumedAdapter extends ArrayAdapter<TrainerObject> {

    private static ArrayList<TrainerObject> trainers;
    private Activity context;

    public CustomGymTrainerAssumedAdapter(Activity _context, ArrayList<TrainerObject> _trainers) {
        super(_context, R.layout.notification_item, _trainers);
        this.context = _context;
        this.trainers = _trainers;
    }

    @Override
    public int getCount() {
        return trainers.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomGymTrainerAssumedAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_trainer_assumed_item, null);
            viewHolder = new CustomGymTrainerAssumedAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomGymTrainerAssumedAdapter.ViewHolder) r.getTag();
        }
        String name = trainers.get(position).name;
        String lastname = trainers.get(position).lastname;
        String email = trainers.get(position).email;

        viewHolder.tv_gym_trainer_assumed_name.setText(name);
        viewHolder.tv_gym_trainer_assumed_lastname.setText(lastname);
        viewHolder.tv_gym_trainer_assumed_email.setText(email);

        viewHolder.btn_gym_trainer_assumed_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:
                //LOGICA AGGIUNTA TRAINER SCELTO TRA I PROPRI DIPENDENDTI
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_trainer_assumed_name, tv_gym_trainer_assumed_lastname, tv_gym_trainer_assumed_email;
        ImageView btn_gym_trainer_assumed_add;

        ViewHolder(View v) {
            tv_gym_trainer_assumed_name = v.findViewById(R.id.tv_gym_trainer_assumed_name);
            tv_gym_trainer_assumed_lastname = v.findViewById(R.id.tv_gym_trainer_assumed_lastname);
            tv_gym_trainer_assumed_email = v.findViewById(R.id.tv_gym_trainer_assumed_email);

            btn_gym_trainer_assumed_add = v.findViewById(R.id.btn_gym_trainer_assumed_add);
        }
    }

}
