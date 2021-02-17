package android_team.gymme_client.customer;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.support.BasicActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingSheetItemActivity extends BasicActivity {

    @BindView(R.id.training_item_chart_customer)
    PieChart chart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_training_sheets);

        ButterKnife.bind(this);

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
        setData();

    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        entries.add(new PieEntry(60f));
        entries.add(new PieEntry(40f));

        PieDataSet dataSet = new PieDataSet(entries, "Percentuale completamento");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(3f);

        // add a lot of colors

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