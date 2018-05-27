package net.isogen.soundcloudplugin.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.isogen.soundcloudplugin.Activities.TrackDetailActivity;
import net.isogen.soundcloudplugin.Database.TrackDatabase;
import net.isogen.soundcloudplugin.R;
import net.isogen.soundcloudplugin.Soundcloud.Track;

import java.util.List;

public class RecentsActivity extends AppCompatActivity {

    ListView recentList;
    Context context;
    Toolbar toolbar;
    List<Track> recentTracks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        context = this;
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Recent Tracks");

        Log.i("SC", getFilesDir().toString());

        recentList = (ListView)findViewById(R.id.recent_list);

        recentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track selectedTrack = recentTracks.get(position);
                Intent i = new Intent(context, TrackDetailActivity.class);
                i.putExtra("TRACK_ID", selectedTrack.getId());
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecents();
    }

    void loadRecents(){
        TrackDatabase database = new TrackDatabase(this);

        recentTracks = database.getRecentTracks(50);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_text_view);

        adapter.addAll(recentTracks);

        recentList.setAdapter(adapter);
    }
}
