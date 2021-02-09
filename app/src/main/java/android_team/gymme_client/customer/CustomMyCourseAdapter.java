package android_team.gymme_client.customer;

import android.annotation.SuppressLint;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.menage_course.CourseObject;
import android_team.gymme_client.gym.menage_course.CustomCourseAdapter;
import android_team.gymme_client.gym.menage_course.GymCourseActivity;
import android_team.gymme_client.gym.menage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.support.MyApplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomMyCourseAdapter extends ArrayAdapter<CourseObject> {
    private static ArrayList<CourseObject> courses;
    private Activity context;

    public CustomMyCourseAdapter(Activity _context, ArrayList<CourseObject> _courses) {
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
        CustomMyCourseAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_course_item, null);
            viewHolder = new CustomMyCourseAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomMyCourseAdapter.ViewHolder) r.getTag();
        }
        final String course_id = courses.get(position).getCourse_id();
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
                courseInfo(context, course_id, name, lastname, description, title, category, start_date, end_date, max_persons, position);
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


    public void courseInfo(Activity a, String course_id, String name, String lastname, String description, String title, String category, String start_date, String end_date, String max_persons, Integer position) {
        CustomMyCourseAdapter.CustomDialogRemoveCourse cdd = new CustomMyCourseAdapter.CustomDialogRemoveCourse(a, course_id, name, lastname, description, title, category, start_date, end_date, max_persons, position);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogRemoveCourse extends Dialog implements View.OnClickListener {

        public Activity c;
        public Button Cancella, Esci;
        public TextView _nominativo, _description, _title, _category, _start_date, _end_date, _max_persons;
        public String course_id, name, lastname, description, title, category, start_date, end_date, max_persons;
        Integer position;

        public CustomDialogRemoveCourse(Activity a, String course_id, String name, String lastname, String description, String title, String category, String start_date, String end_date, String max_persons, Integer position) {
            super(a);
            this.c = a;
            this.course_id = course_id;
            this.name = name;
            this.lastname = lastname;
            this.description = description;
            this.title = title;
            this.category = category;
            this.start_date = start_date;
            this.end_date = end_date;
            this.max_persons = max_persons;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_course_info);
            Cancella = (Button) findViewById(R.id.btn_delete_course);
            Esci = (Button) findViewById(R.id.btn_exit_course);

            _nominativo = (TextView) findViewById(R.id.tv_course_nominativo);
            _description = (TextView) findViewById(R.id.tv_course_description);
            _title = (TextView) findViewById(R.id.tv_course_title);
            _category = (TextView) findViewById(R.id.tv_course_category);
            _start_date = (TextView) findViewById(R.id.tv_course_start_date);
            _end_date = (TextView) findViewById(R.id.tv_course_end_date);
            _max_persons = (TextView) findViewById(R.id.tv_course_max_persons);


            _nominativo.setText(name + " " + lastname);
            _description.setText(description);
            _title.setText(title);
            _category.setText(category);
            _start_date.setText(start_date.split("T")[0]);
            _end_date.setText(end_date.split("T")[0]);
            _max_persons.setText(max_persons);
            Cancella.setText("Disiscriviti");
            Cancella.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_delete_course:
                    CustomMyCourseAdapter.DeleteCourseConnection asyncTask = (CustomMyCourseAdapter.DeleteCourseConnection) new CustomMyCourseAdapter.DeleteCourseConnection(new CustomMyCourseAdapter.DeleteCourseConnection.AsyncResponse() {
                        @Override
                        public void processFinish(Integer output) {
                            if (output == 200) {
                                GymMenageWorkerActivity.runOnUI(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "Corso cancellato", Toast.LENGTH_SHORT).show();
                                        GymCourseActivity.redoAdapterCourse(context, courses, position);
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
                    }).execute(course_id);
                    dismiss();
                    break;
                case R.id.btn_exit_course:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    private static class DeleteCourseConnection extends AsyncTask<String, String, Integer> {

        public interface AsyncResponse {
            void processFinish(Integer output);
        }

        public CustomMyCourseAdapter.DeleteCourseConnection.AsyncResponse delegate = null;

        public DeleteCourseConnection(CustomMyCourseAdapter.DeleteCourseConnection.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected Integer doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            Integer responseCode = 500;

            try {
                url = new URL("http://10.0.2.2:4000/gym/send_del_course_notification/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(HttpURLConnection.HTTP_OK);

                } else {
                    Log.e("DELETE COURSE", "SERVER ERROR");
                    delegate.processFinish(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("DELETE COURSE", "I/O EXCEPTION ERROR");
                delegate.processFinish(500);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return responseCode;
        }
    }

}

