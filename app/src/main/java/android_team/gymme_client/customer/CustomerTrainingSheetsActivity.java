package android_team.gymme_client.customer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerTrainingSheetsActivity extends AppCompatActivity {

    private int user_id;


    @BindView(R.id.drawer_layout_home_activity)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_toolbar_title)
    TextView main_toolbar_title;

    @BindView(R.id.drawer_trainings_link)
    LinearLayout drawer_trainings_link;

    @BindView(R.id.chart1)
    PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_training_sheets_list);
        ButterKnife.bind(this);

        main_toolbar_title.setText("Allenamenti");

        drawer_trainings_link.setPadding(20, 10, 20, 10);
        drawer_trainings_link.setBackground(getDrawable(R.drawable.rounded_rectangle));
        drawer_trainings_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));


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

    ////Gestione Drawer

    public void ClickMenu(View view) {
        Drawer.openDrawer(drawerLayout);
    }

    public void ClickDrawer(View view) {
        Drawer.closeDrawer(drawerLayout);
    }

    public void ToTrainings(View view) {
        redirectActivity(this, CustomerTrainingSheetsActivity.class);
    }

    public void redirectActivity(Activity a, Class c){
        Intent i = new Intent(a,c);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivity(i);
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
