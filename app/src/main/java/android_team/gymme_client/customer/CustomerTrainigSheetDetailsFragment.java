package android_team.gymme_client.customer;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import android_team.gymme_client.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerTrainigSheetDetailsFragment extends Fragment {

    @BindView(R.id.tr_sheet_dtl_title)
    TextView title;
    @BindView(R.id.tr_sheet_dtl_author)
    TextView author;
    @BindView(R.id.tr_sheet_dtl_creation_date)
    TextView creation_date;
    @BindView(R.id.tr_sheet_dtl_training_days_no)
    TextView days_number;
    @BindView(R.id.tr_sheet_dtl_recycler)
    RecyclerView trainingDaysRecycler;
    @BindView(R.id.tr_sheet_dtls_desc)
    TextView description;
    @BindView(R.id.training_sheet_detail_chart_customer)
    PieChart chart;

    JsonObject training_sheet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_customer_training_sheet_details, null);
        ButterKnife.bind(this, root);
        Bundle args = getArguments();
        training_sheet = (JsonObject) JsonParser.parseString(args.getString("training_sheet"));
        Log.e("tr sheet", training_sheet.toString());

        title.setText(training_sheet.get("title").getAsString());
        author.setText(training_sheet.get("trainer_name").getAsString());
        creation_date.setText(training_sheet.get("creation_date").getAsString());
        description.setText(training_sheet.get("description").getAsString());
        days_number.setText(training_sheet.get("number_of_days").getAsString());

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.parseColor("#FCA01F"));

        chart.setTransparentCircleColor(Color.parseColor("#FCA01F"));
        chart.setTransparentCircleAlpha(100);

        chart.setCenterText(training_sheet.get("strength").getAsString() + " %");
        chart.setCenterTextColor(Color.BLACK);
        chart.setCenterTextSize(20);


        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(40f);

        chart.setDrawCenterText(true);
        //chart.setRotationAngle(90);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);
        chart.setEntryLabelTextSize(12f);
        setData(chart, training_sheet.get("strength").getAsInt());

        trainingDaysRecycler.setAdapter(new TrainingDaysDetailChooseAdapter(training_sheet.get("training_days").getAsJsonArray(),
                training_sheet.get("exercises").getAsJsonArray(),getContext()));
        trainingDaysRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;

    }

    private void setData(PieChart chart, int strenght) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        entries.add(new PieEntry(strenght));
        entries.add(new PieEntry(100-strenght));

        PieDataSet dataSet = new PieDataSet(entries, "Difficoltà");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(3f);


        ArrayList<Integer> colors = new ArrayList<>();



        colors.add(Color.parseColor("#8f0032"));
        colors.add(Color.parseColor("#FCA01F"));

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