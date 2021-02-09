package android_team.gymme_client.customer;

import android_team.gymme_client.R;
import android_team.gymme_client.gym.GymObject;
import android_team.gymme_client.gym.menage_course.CourseObject;

import android_team.gymme_client.login.LoginActivity;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class CustomerManageCourseActivity extends AppCompatActivity {

    @BindView(R.id.btn_customer_manage_course_to_disponible_gym)
    Button _btn_customer_manage_course_to_disponible_gym;

    static CustomMyCourseAdapter course_adapter;
    private int user_id;
    ListView lv_course;
    public static ArrayList<CourseObject> course_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_my_course);

        ButterKnife.bind(this);
        course_list = new ArrayList<CourseObject>();

        Intent i = getIntent();
        if (!i.hasExtra("user_id")) {
            Toast.makeText(this, "User_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            user_id = i.getIntExtra("user_id", -1);
            Log.w("user_id ricevuto:", String.valueOf(user_id));
            if (user_id == -1) {
                Toast.makeText(this, "Utente non creato.", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }
        lv_course = (ListView) findViewById(R.id.lv_customer_gym);

        getCourse();

        _btn_customer_manage_course_to_disponible_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Customer Manage Gym Activity");
                Intent i = new Intent(getApplicationContext(), CustomerAddCourseActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });
    }
    private void getCourse() {
        CustomerManageCourseActivity.ReceiveCourseConn asyncTaskUser = (CustomerManageCourseActivity.ReceiveCourseConn) new CustomerManageCourseActivity.ReceiveCourseConn(new CustomerManageCourseActivity.ReceiveCourseConn.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<CourseObject> courses) {

                course_list = courses;
                if (course_list.size() > 0) {
                    //DATI RICEVUTI
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //setto tramite l'adapter la lista dei trainer da visualizzare nella recycler view(notificationView)
                            course_adapter = new CustomMyCourseAdapter(CustomerManageCourseActivity.this, course_list);
                            lv_course.setAdapter(course_adapter);

                        }
                    });
                } else {
                    // NESSUN DATO RICEVUTO PERCHE' NESSUNA TRAINER LAVORA PER QUESTA PALESTRA
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CustomerManageCourseActivity.this, "Nessun corso", Toast.LENGTH_SHORT).show();
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

        public CustomerManageCourseActivity.ReceiveCourseConn.AsyncResponse delegate = null;

        public ReceiveCourseConn(CustomerManageCourseActivity.ReceiveCourseConn.AsyncResponse delegate) {
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
                url = new URL("http://10.0.2.2:4000/customer/get_course_customers/" + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    Log.e("Server response", "HTTP_OK");
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
                    Log.e("GET TRAINER", "response: HTTP_NOT_FOUND");
                    delegate.processFinish(new ArrayList<CourseObject>());
                } else {
                    Log.e("GET TRAINER", "SERVER ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("GET TRAINER", "I/O EXCEPTION ERROR");
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

}