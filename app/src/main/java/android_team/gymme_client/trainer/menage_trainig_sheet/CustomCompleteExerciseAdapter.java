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

public class CustomCompleteExerciseAdapter extends ArrayAdapter<CompleteExerciseObject>{

    private static ArrayList<CompleteExerciseObject> exercises;
    private Activity context;



    public CustomCompleteExerciseAdapter(Activity _context, ArrayList<CompleteExerciseObject> exercises) {
        super(_context, R.layout.complete_exercise_item, exercises);
        this.context = _context;
        this.exercises = exercises;

    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomCompleteExerciseAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.complete_exercise_item, null);
            viewHolder = new CustomCompleteExerciseAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomCompleteExerciseAdapter.ViewHolder) r.getTag();
        }

        viewHolder.tv_name.setText(exercises.get(position).getName());
        viewHolder.tv_rep.setText(exercises.get(position).getRepetitions());

        viewHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Premuto", "Dai vai");
                TrainerCreateSingleDayActivity.removeFromCompleteAdapter(context, position);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_name, tv_rep;
        ImageView btn_remove;

        ViewHolder(View v) {
            tv_name = v.findViewById(R.id.tv_complete_item_name);
            tv_rep = v.findViewById(R.id.tv_complete_item_repetitions);
            btn_remove = v.findViewById(R.id.btn_complete_item_remove);
        }
    }

}
