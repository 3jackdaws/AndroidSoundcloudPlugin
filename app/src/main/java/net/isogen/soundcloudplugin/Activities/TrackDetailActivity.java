package net.isogen.soundcloudplugin.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.isogen.soundcloudplugin.Database.TrackDatabase;
import net.isogen.soundcloudplugin.Fragments.TrackDetailFragment;
import net.isogen.soundcloudplugin.R;
import net.isogen.soundcloudplugin.Soundcloud.Track;
import net.isogen.soundcloudplugin.Utilities;

public class TrackDetailActivity extends AppCompatActivity {

    Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Track Detail");

        TrackDatabase database = new TrackDatabase(this);

        int trackId = getIntent().getIntExtra("TRACK_ID", 0);

        track = database.get(trackId);

        if(track != null){
            TrackDetailFragment detailFragemnt = (TrackDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
            if(detailFragemnt != null){
                detailFragemnt.setTrack(track);
            }
        }


    }
}
