package android_team.gymme_client.customer;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import android_team.gymme_client.support.Drawer;
import android_team.gymme_client.trainer.TrainerHomeActivity;
import android_team.gymme_client.trainer.TrainerProfileActivity;

public class DrawerCustomerListener {

    Activity activity;
    Integer user_id;

    public DrawerCustomerListener(Activity activity, Integer user_id) {
        this.activity = activity;
        this.user_id = user_id;
    }


    public void toNotify() {
        redirectActivity(activity, CustomerNotificationActivity.class);
    }
    public void toTrainings() {
        redirectActivity(activity, CustomerTrainingSheetsActivity.class);
    }
    public void toGym() {
        redirectActivity(activity, CustomerManageGymActivity.class);
    }
    public void toCourse() {
        redirectActivity(activity, CustomerManageCourseActivity.class);
    }
    public void toProfile() {
        redirectActivity(activity, CustomerProfileActivity.class);
    }
    public void toHome() {
        redirectActivity(activity, CustomerHomeActivity.class);
    }

    public void redirectActivity(Activity a, Class c) {
      //  //Log.e("REDIRECT", c.toString());
        Intent i = new Intent(a, c);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("user_id", user_id);
        a.startActivity(i);
    }

}

