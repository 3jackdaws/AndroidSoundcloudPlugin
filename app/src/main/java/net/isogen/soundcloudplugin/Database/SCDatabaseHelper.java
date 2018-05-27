package net.isogen.soundcloudplugin.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SCDatabaseHelper extends SQLiteOpenHelper {

    public SCDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format(
                "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY," +
                "%s TEXT" +
                ")",
                TrackDatabase.TABLE_NAME,
                TrackDatabase.COLUMN_ID,
                TrackDatabase.COLUMN_DATA
        );
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
