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

public class CustomCourseTrainerAdapter extends ArrayAdapter<TrainerObject> {

    private static ArrayList<TrainerObject> trainers;
    private Activity context;

    public CustomCourseTrainerAdapter(Activity _context, ArrayList<TrainerObject> _trainers) {
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
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View r = convertView;
        CustomCourseTrainerAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_trainer_course_item, null);
            viewHolder = new CustomCourseTrainerAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomCourseTrainerAdapter.ViewHolder) r.getTag();
        }

        final String trainer_id = trainers.get(position).user_id;
        final String name = trainers.get(position).name;
        final String lastname = trainers.get(position).lastname;
        final String email = trainers.get(position).email;
        final String qualification = trainers.get(position).qualification;
        final String fiscal_code = trainers.get(position).fiscal_code;

        viewHolder.tv_gym_trainer_assumed_name.setText(name);
        viewHolder.tv_gym_trainer_assumed_lastname.setText(lastname);
        viewHolder.tv_gym_trainer_assumed_email.setText(email);

        viewHolder.btn_gym_trainer_assumed_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GymCoursesActivity.selectTrainer(position);
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
