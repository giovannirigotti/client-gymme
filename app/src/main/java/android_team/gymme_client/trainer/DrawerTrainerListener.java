package android_team.gymme_client.trainer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import android_team.gymme_client.trainer.manage_training_sheet.TrainerMenageTrainingSheet;

public class DrawerTrainerListener {

    Activity activity;
    Integer user_id;

    public DrawerTrainerListener(Activity activity, Integer user_id) {
        this.activity = activity;
        this.user_id = user_id;
    }


    public void toTrainingSheet() {
        redirectActivity(activity, TrainerMenageTrainingSheet.class);
    }
    public void toProfile() {
        redirectActivity(activity, TrainerProfileActivity.class);
    }
    public void toHome() {
        redirectActivity(activity, TrainerHomeActivity.class);
    }

    public void redirectActivity(Activity a, Class c) {
        Log.e("REDIRECT", c.toString());
        Intent i = new Intent(a, c);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("user_id", user_id);
        a.startActivity(i);
    }
}
