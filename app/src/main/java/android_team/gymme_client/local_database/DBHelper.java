package android_team.gymme_client.local_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "GYMMEMORELOCALDB";


    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE user (id INTEGER, name TEXT, lastname TEXT, birthdate TEXT, email TEXT, type INTEGER)";
        String sql2 = "CREATE TABLE status (status INTEGER)";
        String sql3 = "INSERT INTO status (status) VALUES (0)";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
