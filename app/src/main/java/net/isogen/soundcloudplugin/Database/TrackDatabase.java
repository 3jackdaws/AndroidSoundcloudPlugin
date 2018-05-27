package net.isogen.soundcloudplugin.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.isogen.soundcloudplugin.Soundcloud.Track;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class TrackDatabase {
    public final static String DATABASE_NAME = "tracks.db";
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "TRACK";
    public final static String COLUMN_ID =  "ID";
    public final static String COLUMN_DATA =  "DATA";
    public final static String COLUMN_TIME =  "TIME";
    private SCDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public TrackDatabase(Context context){
        dbHelper = new SCDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
    }

    public Track get(int id){
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
            finally {
                cursor.close();
            }
        }

        return track;
    }

    public List<Track> getRecentTracks(int limit){
        List<Track> tracks = new LinkedList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, "TIME DESC", String.valueOf(limit));
        int dataColumnIndex = cursor.getColumnIndex(COLUMN_DATA);
        while(cursor.moveToNext()){
            try {
                JSONObject obj = new JSONObject(cursor.getString(dataColumnIndex));
                tracks.add(new Track(obj, null));
            }catch (JSONException e){
                Log.e("SC", "[TrackDatabase.getRecentTracks] JSON error: " + e.getMessage());
            }
        }
        return tracks;
    }

    public boolean trackExists(int id){
        Cursor cursor = database.rawQuery("SELECT 1 FROM TRACK WHERE ID=?", new String[]{String.valueOf(id)});
        return cursor.moveToNext();
    }

    public void put(Track track){
        if(trackExists(track.getId())){
            database.execSQL("UPDATE TRACK SET TIME=CURRENT_TIMESTAMP WHERE ID=?", new Integer[]{track.getId()});
        }else{
            database.execSQL("INSERT INTO TRACK (ID, DATA) VALUES(?,?)", new Object[]{track.getId(), track.getJSON()});
        }

    }
}
