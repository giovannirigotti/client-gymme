package android_team.gymme_client.customer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import android_team.gymme_client.R;

public class ListTrainingSheetsAdapter extends RecyclerView.Adapter<ListTrainingSheetsAdapter.ViewHolder> {
    JsonArray training_sheets;
    Context context;



    public ListTrainingSheetsAdapter(JsonArray training_sheets, Context context) {
        this.training_sheets = training_sheets;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public TextView description;
        public TextView creation_date;
        public PieChart chart;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.training_item_title_customer);
            author = itemView.findViewById(R.id.training_item_author_customer);
            description = itemView.findViewById(R.id.training_item_description_customer);
            creation_date = itemView.findViewById(R.id.training_item_creation_date_customer);
            chart = itemView.findViewById(R.id.training_item_chart_customer);
        }
    }

    @NonNull
    @Override
    public ListTrainingSheetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_training_sheets, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListTrainingSheetsAdapter.ViewHolder holder, int position) {

        TextView title = holder.title;
        TextView author = holder.author;
        TextView description = holder.description;
        TextView creation_date = holder.creation_date;
        PieChart chart = holder.chart;
        JsonObject training_sheet = training_sheets.get(position).getAsJsonObject();

        title.setText(training_sheet.get("title").getAsString());
        author.setText(training_sheet.get("trainer_name").getAsString());
        description.setText(training_sheet.get("description").getAsString());
        creation_date.setText(training_sheet.get("creation_date").getAsString());

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.parseColor("#ff9700"));

        chart.setTransparentCircleColor(Color.parseColor("#ff9700"));
        chart.setTransparentCircleAlpha(100);

        chart.setHoleRadius(25f);
        chart.setTransparentCircleRadius(27f);

        chart.setDrawCenterText(true);
        chart.setRotationAngle(90);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setEntryLabelTextSize(12f);
        //setData(chart, );

    }

    @Override
    public int getItemCount() {
        if (training_sheets.size() == 0)
            return 0;
        else return training_sheets.size();
    }

    public JsonObject getItem(int position) {
        return training_sheets.get(position).getAsJsonObject();
    }


    private void setData(PieChart chart, int days, int totalDays) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        entries.add(new PieEntry(60));
        entries.add(new PieEntry(40f));

        PieDataSet dataSet = new PieDataSet(entries, "Percentuale completamento");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(3f);


        ArrayList<Integer> colors = new ArrayList<>();


        colors.add(Color.parseColor("#ff9700"));
        colors.add(Color.parseColor("#8f0032"));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }
}