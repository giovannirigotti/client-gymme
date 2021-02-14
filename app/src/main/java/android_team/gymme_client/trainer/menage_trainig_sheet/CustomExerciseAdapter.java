package android_team.gymme_client.trainer.menage_trainig_sheet;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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

public class CustomExerciseAdapter extends ArrayAdapter<ExerciseObject> {

    private static ArrayList<ExerciseObject> exercises;
    private Activity context;



    public CustomExerciseAdapter(Activity _context, ArrayList<ExerciseObject> exercises) {
        super(_context, R.layout.trainer_exercise_item, exercises);
        this.context = _context;
        this.exercises = exercises;

    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomExerciseAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.trainer_exercise_item, null);
            viewHolder = new CustomExerciseAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomExerciseAdapter.ViewHolder) r.getTag();
        }

        viewHolder.tv_nome.setText(exercises.get(position).getName());

        viewHolder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainerCreateSingleDayActivity.selectExercise(exercises.get(position));
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_nome;
        ImageView btn_select;

        ViewHolder(View v) {
            tv_nome = v.findViewById(R.id.tv_view_exercise_name);
            btn_select = v.findViewById(R.id.btn_select_exercise);
        }
    }

}
