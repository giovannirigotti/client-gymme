package android_team.gymme_client;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TrainingDaysDetailChooseAdapter extends RecyclerView.Adapter<TrainingDaysDetailChooseAdapter.ViewHolder> {
    JsonArray trainingDays;
    JsonArray exercises;
    Context context;

    public TrainingDaysDetailChooseAdapter(JsonArray trainingDays, JsonArray exercises, Context context) {
        this.trainingDays = trainingDays;
        this.context = context;
        this.exercises = exercises;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
        public LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.cus_comp_tr_day_dial_item_title_choose);
            container = itemView.findViewById(R.id.cus_comp_tr_day_dial_item_container);
        }
    }

    @NonNull
    @Override
    public TrainingDaysDetailChooseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_training_day_choose, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingDaysDetailChooseAdapter.ViewHolder holder, int position) {
        TextView itemTitle = holder.itemTitle;
        LinearLayout container = holder.container;

        JsonObject trainingDay = trainingDays.get(position).getAsJsonObject();
        Log.e("Training day", trainingDay.toString());
        itemTitle.setText("Giornata: " + trainingDay.get("seq").getAsString());

        JsonArray todayExercises = new JsonArray();

        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getAsJsonObject().get("seq").getAsString().equals(trainingDay.get("seq").getAsString())) {
                todayExercises.add(exercises.get(i).getAsJsonObject());
            }
        }

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog((Activity) context, todayExercises, trainingDay.get("seq").getAsString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (trainingDays.size() == 0)
            return 0;
        else return trainingDays.size();
    }

    public JsonObject getItem(int position) {
        return trainingDays.get(position).getAsJsonObject();
    }


    private class TrainingDayDetailsDialog extends Dialog implements android.view.View.OnClickListener {

        public Activity a;
        public Dialog d;
        public Button close;
        public TextView title;
        public RecyclerView exercisesRecView;
        JsonArray exercises;
        String day;

        public TrainingDayDetailsDialog(Activity a, JsonArray exercises, String day) {
            super(a);
            this.a = a;
            this.exercises = exercises;
            this.day = day;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_details_training_sheet_ex_training_day_details);
            close = (Button) findViewById(R.id.cus_tr_sheet_ex_dial_close);
            title = (TextView) findViewById(R.id.tr_day_det_tr_day_det_title);
            title.setText("Esercizi della giornata " + day + ":");
            exercisesRecView = (RecyclerView) findViewById(R.id.cus_comp_tr_day_dial_recycler);

            exercisesRecView.setAdapter(new TrainingDayDetailDialogExercisesAdapter(exercises, context));
            exercisesRecView.setLayoutManager(new LinearLayoutManager(context));

            close.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cus_tr_sheet_ex_dial_close:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private void showDialog(Activity a, JsonArray exercises, String day) {
        TrainingDayDetailsDialog cdd = new TrainingDayDetailsDialog(a, exercises, day);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }


}

