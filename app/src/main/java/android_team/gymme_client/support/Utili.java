package android_team.gymme_client.support;

import android.content.Context;
import android.content.Intent;

import android_team.gymme_client.local_database.local_dbmanager.DBManagerStatus;
import android_team.gymme_client.local_database.local_dbmanager.DBManagerUser;
import android_team.gymme_client.login.LoginActivity;

public class Utili{

    public static final String SERVER = "http://10.0.2.2:4000/";

    public static void logout(Context c) {

        DBManagerStatus DBstatus = new DBManagerStatus(c);
        DBstatus.open();
        DBstatus.update(DBstatus.STATUS_NOT_LOGGED);

        DBManagerUser DBuser = new DBManagerUser(c);
        DBuser.open();
        DBuser.delete();

    }
}
