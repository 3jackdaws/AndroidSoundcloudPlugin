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
import net.isogen.soundcloudplugin.R;
import net.isogen.soundcloudplugin.Soundcloud.Track;
import net.isogen.soundcloudplugin.Utilities;

public class TrackDetailActivity extends AppCompatActivity {

    Track track;
    ImageView artwork;
    TextView trackArtist;
    TextView trackTitle;
    TextView viewOnSCButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Track Detail");

        artwork = (ImageView) findViewById(R.id.album_art);
        trackArtist = (TextView) findViewById(R.id.artist_text);
        trackTitle = (TextView) findViewById(R.id.title_text);
        viewOnSCButton = (TextView) findViewById(R.id.view_on_sc);
        int trackId = getIntent().getIntExtra("TRACK_ID", -1);
        TrackDatabase database = new TrackDatabase(this);
        track = database.get(trackId);
        if(track != null){
            trackArtist.setText(track.getArtist());
            trackTitle.setText(track.getTitle());
            Utilities.RunInThread(()->{
                Bitmap img = Utilities.downloadImage(track.getAlbumArtURL());
                Utilities.RunInUIThread(()->artwork.setImageBitmap(img));
            });

            viewOnSCButton.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(track.getSoundcloudURL()));
                startActivity(browserIntent);
            });
        }else{

        }



    }
}
