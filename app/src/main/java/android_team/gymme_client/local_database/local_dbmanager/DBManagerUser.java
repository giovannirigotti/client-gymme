package android_team.gymme_client.local_database.local_dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android_team.gymme_client.local_database.DBHelper;

public class DBManagerUser {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManagerUser(Context c) {
        context = c;
    }

    public DBManagerUser open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(int id, String name, String lastname, String email, String birthdate, int type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("lastname", lastname);
        contentValues.put("email", email);
        contentValues.put("birthdate", birthdate);
        contentValues.put("type", type);
        database.insert("user", null, contentValues);
    }

    public Cursor fetch() {
        String[] columns = {"id", "name", "lastname", "email", "birthdate", "type"};
        Cursor cursor = database.query("user", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(String name, String lastname, String email, String birthdate, int type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lastname", lastname);
        contentValues.put("email", email);
        contentValues.put("birthdate", birthdate);
        int columnsAffected = database.update("user", contentValues, null, null);
        return columnsAffected;
    }

    public void delete() {
        database.delete("user", null, null);
    }
}
