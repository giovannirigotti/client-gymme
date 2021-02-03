package android_team.gymme_client.local_database.local_dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android_team.gymme_client.local_database.DBHelper;

public class DBManagerStatus {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public final Integer STATUS_LOGGED = 1;
    public final Integer STATUS_NOT_LOGGED = 0;

    public DBManagerStatus(Context c) {
        context = c;
    }

    public DBManagerStatus open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    //Insert non necessario.

    public Cursor fetch() {
        String[] columns = {"status"};
        Cursor cursor = database.query("status", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        int columnsAffected = database.update("status", contentValues, null, null);
        return columnsAffected;
    }

    //Delete non necessario.


}