package android_team.gymme_client.gym;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android_team.gymme_client.gym.menage_course.GymCourseActivity;
import android_team.gymme_client.gym.menage_customer.GymCustomersActivity;
import android_team.gymme_client.gym.menage_profile.GymProfileActivity;
import android_team.gymme_client.gym.menage_worker.GymMenageWorkerActivity;

public class DrawerGymListener {

    Activity activity;
    Integer user_id;

    public DrawerGymListener(Activity activity, Integer user_id) {
        this.activity = activity;
        this.user_id = user_id;
    }

    public void toCourse() {
        redirectActivity(activity, GymCourseActivity.class);
    }
    public void toProfile() {
        redirectActivity(activity, GymProfileActivity.class);
    }
    public void toCustomer() {
        redirectActivity(activity, GymCustomersActivity.class);
    }
    public void toEmployees() {
        redirectActivity(activity, GymMenageWorkerActivity.class);
    }
    public void toHome() {
        redirectActivity(activity, GymHomeActivity.class);
    }

    public void redirectActivity(Activity a, Class c) {
        Log.e("REDIRECT", c.toString());
        Intent i = new Intent(a, c);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("user_id", user_id);
        a.startActivity(i);
    }
}
