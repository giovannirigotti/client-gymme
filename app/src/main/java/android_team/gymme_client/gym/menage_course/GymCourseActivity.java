package android_team.gymme_client.gym.menage_course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;

public class GymCourseActivity extends AppCompatActivity {

    private int user_id;

    static ArrayList<CourseObject> courses_list;

    static ListView lv_my_courses;
    private Button btn_create_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_course);

        //region CHECK INTENT
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
        //endregion

        lv_my_courses = (ListView) findViewById(R.id.lv_my_courses);
        btn_create_course = (Button) findViewById(R.id.btn_create_course);

        btn_create_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("REDIRECT", "Gym Courses Activity");
                Intent i = new Intent(getApplicationContext(), GymAddCoursesActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

    }


}