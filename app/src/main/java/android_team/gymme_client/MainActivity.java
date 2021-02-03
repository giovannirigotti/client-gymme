package android_team.gymme_client;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android_team.gymme_client.customer.CustomerHomeActivity;
import android_team.gymme_client.gym.GymHomeActivity;
import android_team.gymme_client.local_database.local_dbmanager.DBManagerStatus;
import android_team.gymme_client.local_database.local_dbmanager.DBManagerUser;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.nutritionist.NutritionistHomeActivity;
import android_team.gymme_client.support.NoNetworkActivity;
import android_team.gymme_client.support.UserInfo;
import android_team.gymme_client.trainer.TrainerHomeActivity;
import android_team.gymme_client.trainer.TrainerProfileActivity;


public class MainActivity extends AppCompatActivity {

    private DBManagerStatus dbManagerStatus = null;
    private DBManagerUser dbManagerUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManagerStatus = new DBManagerStatus(this);
        dbManagerStatus.open();
        dbManagerUser = new DBManagerUser(this);
        dbManagerUser.open();
        Cursor statusCursor = dbManagerStatus.fetch();
        int status = statusCursor.getInt(statusCursor.getColumnIndex("status"));
        // LOGGATO = 1;
        // NON-LOGGATO = 0;
        Log.e("Status", Integer.toString(status));

        if (status == dbManagerStatus.STATUS_LOGGED) {
            Cursor userCursor = dbManagerUser.fetch();
            int type = userCursor.getInt(userCursor.getColumnIndex("type"));
            int user_id = userCursor.getInt(userCursor.getColumnIndex("id"));
            Log.e("USER ID", String.valueOf(user_id));

            UserInfo.setUser_id(user_id);
            UserInfo.setUser_type(type);


            Log.e("Type", Integer.toString(type));
            dbManagerUser.close();
            dbManagerStatus.close();
            if (type == 0) {
                //Se è un utente "normale".
                Intent i = new Intent(getApplicationContext(), CustomerHomeActivity.class);
                i.putExtra("user_id",user_id);
                startActivity(i);
                finish();
            } else if (type == 1) {
                //Se è un trainer.
                Intent i = new Intent(getApplicationContext(), TrainerHomeActivity.class);
                i.putExtra("user_id",user_id); //DA ERRORI PERCHE user vuoto suppongo
                startActivity(i);
                finish();
            } else if (type == 2) {
                //Se è un nutrizionista.
                Intent i = new Intent(getApplicationContext(), NutritionistHomeActivity.class);
                i.putExtra("user_id",user_id); //DA ERRORI PERCHE user vuoto suppongo
                startActivity(i);
                finish();
            } else if (type == 3) {
                //Se è un proprietario.
                Intent i = new Intent(getApplicationContext(), GymHomeActivity.class);
                i.putExtra("user_id",user_id); //DA ERRORI PERCHE user vuoto suppongo
                startActivity(i);
                finish();
            }

        } else {

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo == null) {
                Intent i = new Intent(getApplicationContext(), NoNetworkActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}

