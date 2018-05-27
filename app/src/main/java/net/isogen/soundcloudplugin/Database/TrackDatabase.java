package net.isogen.soundcloudplugin.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.isogen.soundcloudplugin.Soundcloud.Track;

import org.json.JSONException;
import org.json.JSONObject;

public class TrackDatabase {
    public final static String DATABASE_NAME = "tracks.db";
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "TRACK";
    public final static String COLUMN_ID =  "ID";
    public final static String COLUMN_DATA =  "DATA";
    private SCDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public TrackDatabase(Context context){
        dbHelper = new SCDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
    }

    public Track getTrack(int id){
        Integer ids[] = {id};
        Track track = null;
        Cursor cursor = database.query(TABLE_NAME, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if(cursor.moveToFirst()){
            try {
                JSONObject obj = new JSONObject(cursor.getString(1));
                track = new Track(obj, null);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return track;
    }

    public Cursor get
}
