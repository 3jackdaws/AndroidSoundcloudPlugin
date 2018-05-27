package net.isogen.soundcloudplugin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import net.isogen.soundcloudplugin.Database.SCDatabaseHelper;

public class RecentsActivity extends AppCompatActivity {

    ListView recentList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);

        recentList = (ListView)findViewById(R.id.recent_list);

        SCDatabaseHelper db = new SCDatabaseHelper(this, "tracks", SQLiteDatabase.CursorFactory)

        CursorAdapter RecentListAdapter = new SimpleCursorAdapter(this, R.id.recent_list);


    }
}
