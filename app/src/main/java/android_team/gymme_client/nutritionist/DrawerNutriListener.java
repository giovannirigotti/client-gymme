package android_team.gymme_client.nutritionist;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class DrawerNutriListener {

    Activity activity;
    Integer user_id;

    public DrawerNutriListener(Activity activity, Integer user_id) {
        this.activity = activity;
        this.user_id = user_id;
    }

    public void toProfile() {
        redirectActivity(activity, NutritionistProfileActivity.class);
    }
    public void toHome() {
        redirectActivity(activity, NutritionistHomeActivity.class);
    }

    public void redirectActivity(Activity a, Class c) {
        //Log.e("REDIRECT", c.toString());
        Intent i = new Intent(a, c);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("user_id", user_id);
        a.startActivity(i);
    }
}
