package android_team.gymme_client.customer;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android_team.gymme_client.R;

public class TrainingDayDetailDialogExercisesAdapter extends RecyclerView.Adapter<TrainingDayDetailDialogExercisesAdapter.ViewHolder> {
    JsonArray exercises;
    Context context;

    public TrainingDayDetailDialogExercisesAdapter(JsonArray exercises, Context context) {
        this.exercises = exercises;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exTitle;
        public TextView exRep;
        public ImageView detailsButton;

        public ViewHolder(View itemView) {
            super(itemView);
            exTitle = itemView.findViewById(R.id.cus_comp_tr_day_dial_item_title);
            exRep = itemView.findViewById(R.id.cus_cal_list_ex_rep);
            detailsButton = itemView.findViewById(R.id.cus_cal_list_ex_details);
        }
    }

    @NonNull
    @Override
    public TrainingDayDetailDialogExercisesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_customer_calendar_exercise, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingDayDetailDialogExercisesAdapter.ViewHolder holder, int position) {
        TextView exTitle = holder.exTitle;
        TextView exRep = holder.exRep;
        ImageView detailsButton = holder.detailsButton;

        JsonObject exercise = exercises.get(position).getAsJsonObject();
        //Log.e("Esercizio list", exercise.toString());
        exTitle.setText(exercise.get("name").getAsString());
        exRep.setText(exercise.get("repetitions").getAsString());
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog((Activity) context, exercise.get("name").getAsString(), exercise.get("description").getAsString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (exercises.size() == 0)
            return 0;
        else return exercises.size();
    }

    public JsonObject getItem(int position) {
        return exercises.get(position).getAsJsonObject();
    }


    private class CustomDialogClass extends Dialog implements View.OnClickListener {

        public Activity a;
        public Dialog d;
        public Button close;
        public TextView ex_title, ex_description;
        String description, title;

        public CustomDialogClass(Activity a, String title, String description) {
            super(a);
            this.a = a;
            this.description = description;
            this.title = title;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_details_exercise);
            close = (Button) findViewById(R.id.ex_det_dial_close);
            ex_description = (TextView) findViewById(R.id.ex_det_dial_descr);
            ex_title = (TextView) findViewById(R.id.ex_det_dial_title);
            ex_description.setText(description);
            ex_title.setText(title);
            close.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ex_det_dial_close:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private void showDialog(Activity a, String title, String description) {
        CustomDialogClass cdd = new CustomDialogClass(a, title, description);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }






}

