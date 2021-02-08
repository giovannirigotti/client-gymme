package android_team.gymme_client.gym.menage_course;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.menage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.support.MyApplication;
import android_team.gymme_client.trainer.TrainerObject;

public class CustomCourseAdapter extends ArrayAdapter<CourseObject> {

    private static ArrayList<CourseObject> courses;
    private Activity context;

    public CustomCourseAdapter(Activity _context, ArrayList<CourseObject> _courses) {
        super(_context, R.layout.notification_item, _courses);
        this.context = _context;
        this.courses = _courses;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View r = convertView;
        CustomCourseAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_course_item, null);
            viewHolder = new CustomCourseAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomCourseAdapter.ViewHolder) r.getTag();
        }

        final String name = courses.get(position).getTrainer_name();
        final String lastname = courses.get(position).getTrainer_lastname();
        final String description = courses.get(position).getDescription();
        final String title = courses.get(position).getTitle();
        final String category = courses.get(position).getCategory();
        final String start_date = courses.get(position).getStart_date();
        final String end_date = courses.get(position).getEnd_date();
        final String max_persons = courses.get(position).getFree_spaces();

        viewHolder.tv_course_item_title.setText(title);
        viewHolder.tv_course_item_name.setText(name);
        viewHolder.tv_course_item_lastname.setText(lastname);

        viewHolder.btn_course_item_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO dialog con informazioni
                //dismissTrainer(context, trainer_id, name, lastname, email, qualification, fiscal_code, position);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_course_item_title, tv_course_item_name, tv_course_item_lastname;
        ImageView btn_course_item_info;

        ViewHolder(View v) {
            tv_course_item_title = v.findViewById(R.id.tv_course_item_title);
            tv_course_item_name = v.findViewById(R.id.tv_course_item_name);
            tv_course_item_lastname = v.findViewById(R.id.tv_course_item_lastname);

            btn_course_item_info = v.findViewById(R.id.btn_course_item_info);
        }
    }

/*
    public void dismissTrainer(Activity a, String trainer_id, String name, String lastname, String email, String qualification, String fiscal_code, Integer position) {
        CustomCourseAdapter.CustomDialogRemoveTrainer cdd = new CustomCourseAdapter.CustomDialogRemoveTrainer(a, trainer_id, name, lastname, email, qualification, fiscal_code, position);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogRemoveTrainer extends Dialog implements View.OnClickListener {

        public Activity c;
        public Button Licenzia, Esci;
        public TextView _name, _lastname, _email, _qualification, _fiscal_code;
        public String trainer_id, name, lastname, email, qualification, fiscal_code;
        Integer position;

        public CustomDialogRemoveTrainer(Activity a, String trainer_id, String name, String lastname, String email, String qualification, String fiscal_code, Integer position) {
            super(a);
            this.c = a;
            this.trainer_id = trainer_id;
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.qualification = qualification;
            this.fiscal_code = fiscal_code;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_dismiss_trainer);
            Licenzia = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_user_type_no);

            _name = (TextView) findViewById(R.id.tv_dismiss_name);
            _lastname = (TextView) findViewById(R.id.tv_dismiss_lastname);
            _email = (TextView) findViewById(R.id.tv_dismiss_email);
            _qualification = (TextView) findViewById(R.id.tv_dismiss_qualification);
            _fiscal_code = (TextView) findViewById(R.id.tv_dismiss_fiscal_code);

            _name.setText(name);
            _lastname.setText(lastname);
            _email.setText(email);
            _qualification.setText(qualification);
            _fiscal_code.setText(fiscal_code);

            Licenzia.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:
                    CustomCourseAdapter.DismissTrainerConnection asyncTask = (CustomCourseAdapter.DismissTrainerConnection) new CustomCourseAdapter.DismissTrainerConnection(new CustomCourseAdapter.DismissTrainerConnection.AsyncResponse() {
                        @Override
                        public void processFinish(Integer output) {
                            if (output == 200) {
                                GymMenageWorkerActivity.runOnUI(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "SUCCESS, trainer licenziato", Toast.LENGTH_SHORT).show();
                                        GymMenageWorkerActivity.redoAdapterTrainer(context, trainers, position);
                                    }
                                });
                            } else {
                                GymMenageWorkerActivity.runOnUI(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "ERRORE, server side", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).execute(trainer_id, GymMenageWorkerActivity.getGymId());
                    dismiss();
                    break;
                case R.id.dialog_confirm_user_type_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }

    }

    public static class DismissTrainerConnection extends AsyncTask<String, String, Integer> {

        // you may separate this or combined to caller class.
        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public CustomCourseAdapter.DismissTrainerConnection.AsyncResponse delegate = null;

        public DismissTrainerConnection(CustomCourseAdapter.DismissTrainerConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JsonObject user = null;
            int responseCode = 500;
            try {
                url = new URL("http://10.0.2.2:4000/gym/dismiss_trainer/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JsonObject paramsJson = new JsonObject();

                paramsJson.addProperty("user_id", params[0]);
                paramsJson.addProperty("gym_id", params[1]);

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
                    Log.e("GYM TRAINER", "LICENZIATO OK");
                    responseCode = 200;
                    delegate.processFinish(responseCode);
                } else {
                    Log.e("GYM TRAINER", "Error");
                    responseCode = 500;
                    delegate.processFinish(responseCode);
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 69;
                delegate.processFinish(responseCode);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }

    }

    */

}
