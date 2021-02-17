package android_team.gymme_client.customer;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android_team.gymme_client.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerTrainingSheetCalendarFragment extends Fragment {
    JsonObject training_sheet;
    JsonArray completedDays;
    CalendarView calendarView;
    CalendarTrainingSheetAdapter adapter;
    public static String static_seq=new String();
    String customer_id;
    String training_sheet_id;

    @BindView(R.id.trainingSheetCalendarView)
    CalendarView trainingSheetCalendarView;

    @BindView(R.id.trainingSheetCalRecycler)
    RecyclerView trainingSheetCalRecycler;

    @BindView(R.id.customer_calendar_day_title)
    TextView customer_calendar_day_title;

    @BindView(R.id.cus_cal_add_tr_day_btn)
    FloatingActionButton addCompletedTrainingDay;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_customer_training_sheet_calendar, null);
        ButterKnife.bind(this, root);
        calendarView = trainingSheetCalendarView;
        static_seq="";
        Bundle args = getArguments();
        training_sheet_id = args.getString("training_sheet_id");
        customer_id =  args.getString("customer_id");
        training_sheet = (JsonObject) JsonParser.parseString(args.getString("training_sheet"));
        completedDays = training_sheet.get("completed_days").getAsJsonArray();
        final List<EventDay> completedDaysList = new ArrayList<>();
        for (int i = 0; i < completedDays.size(); i++) {
            JsonObject completedDay = completedDays.get(i).getAsJsonObject();
            String dateString = completedDay.get("completion_date").getAsString();
            //Log.e("Date", dateString);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateString);
                //Log.e("Date", date.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            EventDay eventDay = new EventDay(calendar, R.drawable.ic_weightlifting);
            completedDaysList.add(eventDay);
        }
        calendarView.setEvents(completedDaysList);
        calendarView.setOnDayClickListener(eventDay -> {
            SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
            String stringDate = DateFor.format(eventDay.getCalendar().getTime());

            JsonObject today = new JsonObject();
            JsonArray completedDays = training_sheet.get("completed_days").getAsJsonArray();

            for (int i = 0; i < completedDays.size(); i++) {
                if (completedDays.get(i).getAsJsonObject().get("completion_date").getAsString().equals(stringDate)) {
                    customer_calendar_day_title.setText("Giornata: " + completedDays.get(i).getAsJsonObject().get("seq").getAsString());
                    today = completedDays.get(i).getAsJsonObject();
                }
            }
            //Log.e("today", today.toString());
            if (today.size() != 0) {
                addCompletedTrainingDay.setVisibility(View.GONE);
                trainingSheetCalRecycler.setVisibility(View.VISIBLE);
                JsonArray todayExercises = new JsonArray();
                JsonArray trainingSheetExercises = training_sheet.get("exercises").getAsJsonArray();
                for (int i = 0; i < trainingSheetExercises.size(); i++) {
                    if (trainingSheetExercises.get(i).getAsJsonObject().get("seq").getAsString().equals(today.get("seq").getAsString())) {
                        todayExercises.add(trainingSheetExercises.get(i).getAsJsonObject());
                    }
                }
                adapter = new CalendarTrainingSheetAdapter(todayExercises, getActivity());
                trainingSheetCalRecycler.setAdapter(adapter);
                trainingSheetCalRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                (customer_calendar_day_title).setText("Nessun esercizio oggi.");
                trainingSheetCalRecycler.setVisibility(View.GONE);
                addCompletedTrainingDay.setVisibility(View.VISIBLE);
                addCompletedTrainingDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(getActivity(), stringDate, training_sheet.get("training_sheet_id").getAsString(), training_sheet.get("training_days").getAsJsonArray());
                    }
                });
            }
        });
        return root;
    }



    private class InsertCompletedTrainingDayDialog extends Dialog implements android.view.View.OnClickListener {

        public CustomerTrainingSheetActivity a;
        public Dialog d;
        public Button close, confirm;
        public TextView date_text_view, seq_text_view;
        public RecyclerView training_days_recycler;

        String completion_date, training_sheet_id;
        String user_comment;
        JsonArray training_days;
        ProgressBar spinner;
        LinearLayout buttons;
        EditText user_comment_edit_text;

        public InsertCompletedTrainingDayDialog(Activity a, String completion_date, String training_sheet_id, JsonArray training_days) {
            super(a);
            this.a = (CustomerTrainingSheetActivity) a;
            this.completion_date = completion_date;
            this.training_sheet_id = training_sheet_id;
            this.training_days = training_days;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.dialog_insert_completed_training_day);
            close = (Button) findViewById(R.id.cus_comp_tr_day_dial_close);
            confirm = (Button) findViewById(R.id.cus_comp_tr_day_dial_confirm);
            date_text_view = (TextView) findViewById(R.id.cus_comp_tr_day_dial_date);
            seq_text_view = (TextView) findViewById(R.id.cus_comp_tr_day_dial_day_sel);
            training_days_recycler = (RecyclerView) findViewById(R.id.cus_comp_tr_day_dial_recycler);
            spinner = (ProgressBar) findViewById(R.id.cus_comp_tr_day_dial_spinner);
            buttons =  (LinearLayout) findViewById(R.id.cus_comp_tr_day_dial_buttons);
            user_comment_edit_text = (EditText) findViewById(R.id.cus_comp_tr_day_dial_edit_text);

            //Log.e("training days", training_days.toString());

            TrainingDaysChooseAdapter adapter = new TrainingDaysChooseAdapter(training_days, a, seq_text_view);
            training_days_recycler.setAdapter(adapter);
            training_days_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

            date_text_view.setText(completion_date);

            close.setOnClickListener(this);
            confirm.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cus_comp_tr_day_dial_close:
                    dismiss();
                    break;
                case R.id.cus_comp_tr_day_dial_confirm:
                    if(!static_seq.isEmpty()) {
                        //Log.e("Sequenza", static_seq);
                        user_comment = user_comment_edit_text.getText().toString();
                        new InsertCompletedTrainingDay(a, Integer.parseInt(training_sheet_id), Integer.parseInt(static_seq), completion_date, user_comment, spinner, buttons, this).execute();
                        static_seq="";

                       /* Intent i = new Intent(getContext(), CustomerTrainingSheetActivity.class);
                        i.putExtra("customer_id", customer_id);
                        i.putExtra("training_sheet_id", training_sheet_id);
                        startActivity(i);
                        a.finish(); */

                        a.getTrainingSheet(0);
                    } else {
                        Toast.makeText(getContext(), "Scegli una giornata!", Toast.LENGTH_LONG).show();
                    }
                default:
                    break;
            }
        }
    }

    private void showDialog(Activity a, String completion_date, String training_sheet_id, JsonArray training_days) {
        InsertCompletedTrainingDayDialog cdd = new InsertCompletedTrainingDayDialog(a, completion_date, training_sheet_id, training_days);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }


    private class InsertCompletedTrainingDay extends AsyncTask<String, String, Integer> {

        String toastMessage = null;
        Activity a;
        int training_sheet_id;
        int seq;
        String completion_date;
        String user_comment;
        ProgressBar spinner;
        LinearLayout buttons;
        InsertCompletedTrainingDayDialog dialog;


        public InsertCompletedTrainingDay(Activity a, int training_sheet_id, int seq, String completion_date, String user_comment, ProgressBar spinner, LinearLayout buttons, InsertCompletedTrainingDayDialog dialog) {

            this.a = a;
            this.training_sheet_id = training_sheet_id;
            this.seq = seq;
            this.completion_date = completion_date;
            this.user_comment = user_comment;
            this.spinner = spinner;
            this.buttons = buttons;
            this.dialog=dialog;
        }

        @Override
        protected void onPreExecute() {
            buttons.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/customer/insert_completed_training_day/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("training_sheet_id", training_sheet_id);
                paramsJson.addProperty("seq", seq);
                paramsJson.addProperty("completion_date", completion_date);
                paramsJson.addProperty("user_comment", user_comment);

                //Log.e("json", paramsJson.toString());

                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(paramsJson.toString());
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Log.e("ok", "ok");
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    //Log.e("Server response", "Error during insert completed training day!");
                    toastMessage = "Errore nella registrazione di un nuovo completed training day!";
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                toastMessage = "Impossibile connettersi!";
                responseCode = 69;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {

            if(responseCode==200){
                dialog.dismiss();
                Toast.makeText(a,"Giornata di allenamento inserita correttamente", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(a,"Errore!", Toast.LENGTH_LONG);
                dialog.dismiss();
            }
        }
    }


}