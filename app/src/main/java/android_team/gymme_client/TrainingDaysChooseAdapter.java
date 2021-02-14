package android_team.gymme_client;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

public class TrainingDaysChooseAdapter extends RecyclerView.Adapter<TrainingDaysChooseAdapter.ViewHolder> {
    JsonArray trainingDays;
    Context context;
    TextView dialog_seq_text_view;

    public TrainingDaysChooseAdapter(JsonArray trainingDays, Context context, TextView dialog_seq_text_view) {
        this.trainingDays = trainingDays;
        this.context = context;
        this.dialog_seq_text_view=dialog_seq_text_view;
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
    public TrainingDaysChooseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_training_day_choose, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingDaysChooseAdapter.ViewHolder holder, int position) {
        TextView itemTitle = holder.itemTitle;
        LinearLayout container = holder.container;

        JsonObject trainingDay = trainingDays.get(position).getAsJsonObject();
        Log.e("Training day", trainingDay.toString());
        itemTitle.setText("Giornata: " + trainingDay.get("seq").getAsString());

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerTrainingSheetCalendarFragment.static_seq=trainingDay.get("seq").getAsString();
                Log.e("seq", CustomerTrainingSheetCalendarFragment.static_seq);
                dialog_seq_text_view.setText(trainingDay.get("seq").getAsString());
                dialog_seq_text_view.setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
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

}

