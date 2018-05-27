package net.isogen.soundcloudplugin.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.isogen.soundcloudplugin.Database.TrackDatabase;
import net.isogen.soundcloudplugin.R;
import net.isogen.soundcloudplugin.Soundcloud.Track;
import net.isogen.soundcloudplugin.Utilities;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackDetailFragment extends Fragment {

    Track track;
    ImageView artwork;
    TextView trackArtist;
    TextView trackTitle;
    TextView viewOnSCButton;
    TrackDatabase database;

    public TrackDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_track_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        artwork = (ImageView) view.findViewById(R.id.album_art);
        trackArtist = (TextView) view.findViewById(R.id.artist_text);
        trackTitle = (TextView) view.findViewById(R.id.title_text);
        viewOnSCButton = (TextView) view.findViewById(R.id.view_on_sc);

        database = new TrackDatabase(getActivity());
    }

    public void setTrack(Track selectedTrack){
        track = selectedTrack;
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
