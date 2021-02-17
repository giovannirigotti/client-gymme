package android_team.gymme_client.gym.manage_course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.DrawerGymListener;
import android_team.gymme_client.gym.GymHomeActivity;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;

public class GymCourseActivity extends AppCompatActivity {

    private int user_id;

    static ArrayList<CourseObject> courses_list;
    static CustomCourseAdapter course_adapter;

    DrawerGymListener drawerGymListener;
    DrawerLayout drawerLayout;
    TextView tv_title;

    static ListView lv_my_courses;
    private Button btn_create_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_course);

        //region CHECK INTENT
        Intent i = getIntent();
        if (!i.hasExtra("user_id")) {
            Toast.makeText(this, "user_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            user_id = i.getIntExtra("user_id", -1);
            Log.w("user_id ricevuto:", String.valueOf(user_id));
            if (user_id == -1) {
                Toast.makeText(this, "Utente non creato", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }
        //endregion

        //inizializzo
        lv_my_courses = (ListView) findViewById(R.id.lv_my_courses);
        btn_create_course = (Button) findViewById(R.id.btn_create_course);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_gym_activity);
        drawerGymListener = new DrawerGymListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("CORSI");
        
        //carico corsi
        loadCourses();
        

        btn_create_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Gym Courses Activity");
                Intent i = new Intent(getApplicationContext(), GymAddCoursesActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //Log.e("REDIRECT", "Gym Home Activity");
        Intent i = new Intent(getApplicationContext(), GymHomeActivity.class);
        i.putExtra("user_id", user_id);
        startActivity(i);
    }

    //region DRAWER
    @Override
    protected void onPause() {
        super.onPause();
        Drawer.closeDrawer(drawerLayout);
    }
    public void ClickMenu(View view) {
        Drawer.openDrawer(drawerLayout);
    }

    public void ClickDrawer(View view) {
        Drawer.closeDrawer(drawerLayout);
    }

    public void gymToCorsi(View view){
        drawerGymListener.toCourse();
    }
    public void gymToClienti(View view){
        drawerGymListener.toCustomer();
    }
    public void gymToDipendenti(View view){
        drawerGymListener.toEmployees();
    }
    public void gymToProfilo(View view){
        drawerGymListener.toProfile();
    }
    public void gymToHome(View view){
        drawerGymListener.toHome();
    }
    //endregion

    private void loadCourses() {
        GymCourseActivity.ReceiveCourseConn asyncTaskUser = (GymCourseActivity.ReceiveCourseConn) new GymCourseActivity.ReceiveCourseConn(new GymCourseActivity.ReceiveCourseConn.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<CourseObject> courses) {

                courses_list = courses;
                if (courses_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            course_adapter = new CustomCourseAdapter(GymCourseActivity.this, courses_list);
                            lv_my_courses.setAdapter(course_adapter);

                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GymCourseActivity.this, "Nessun corso", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }).execute(String.valueOf(user_id));
    }

    private static class ReceiveCourseConn extends AsyncTask<String, String, JsonArray> {

        public interface AsyncResponse {
            void processFinish(ArrayList<CourseObject> courses);
        }

        public GymCourseActivity.ReceiveCourseConn.AsyncResponse delegate = null;

        public ReceiveCourseConn(GymCourseActivity.ReceiveCourseConn.AsyncResponse delegate) {
            this.delegate = delegate;
        }

        @SuppressLint("WrongThread")
        @Override
        protected JsonArray doInBackground(String... params) {

            URL url;
            HttpURLConnection urlConnection = null;
            JsonArray _courses = null;
            ArrayList<CourseObject> c_objects = new ArrayList<>();

            try {
                url = new URL("http://10.0.2.2:4000/gym/get_courses/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    //Log.e("Server response", "HTTP_OK");
                    String responseString = readStream(urlConnection.getInputStream());
                    _courses = JsonParser.parseString(responseString).getAsJsonArray();

                    for (int i = 0; i < _courses.size(); i++) {
                        JsonObject course = (JsonObject) _courses.get(i);
                        String course_id = course.get("course_id").getAsString().trim();
                        String name = course.get("name").getAsString().trim();
                        String lastname = course.get("lastname").getAsString().trim();
                        String description = course.get("description").getAsString().trim();
                        String title = course.get("title").getAsString().trim();
                        String category = course.get("category").getAsString().trim();
                        String start_date = course.get("start_date").getAsString().trim();
                        String end_date = course.get("end_date").getAsString().trim();
                        String max_persons = course.get("max_persons").getAsString().trim();

                        CourseObject c_obj = new CourseObject(course_id, name, lastname, description, title, category, start_date, end_date, max_persons);
                        c_objects.add(c_obj);

                    }
                    //SE VA TUTTO A BUON FINE INVIO AL METODO procesFinish();
                    delegate.processFinish(c_objects);

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    //Log.e("GET TRAINER", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<CourseObject>());
                } else {
                    //Log.e("GET TRAINER", "SERVER ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
                //Log.e("GET TRAINER", "I/O EXCEPTION ERROR");
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return _courses;
        }

        private String readStream(InputStream in) throws UnsupportedEncodingException {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }

    public static void redoAdapterCourse(Activity context, ArrayList<CourseObject> courses, Integer position) {
        ArrayList<CourseObject> new_t = new ArrayList<>();
        for(int i = 0; i < courses.size(); i++){
            if(i != position){
                new_t.add(courses.get(i));
            }
        }
        course_adapter = new CustomCourseAdapter(context, new_t);
        lv_my_courses.setAdapter(course_adapter);
    }


}