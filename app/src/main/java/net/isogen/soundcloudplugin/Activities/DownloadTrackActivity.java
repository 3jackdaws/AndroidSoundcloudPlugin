package net.isogen.soundcloudplugin.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import net.isogen.soundcloudplugin.Database.TrackDatabase;
import net.isogen.soundcloudplugin.R;
import net.isogen.soundcloudplugin.Soundcloud.SoundcloudAPI;
import net.isogen.soundcloudplugin.Soundcloud.SoundcloudObject;
import net.isogen.soundcloudplugin.Soundcloud.Track;
import net.isogen.soundcloudplugin.Utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadTrackActivity extends AppCompatActivity {



    ActionBar actionBar;
    Context context;
    ImageView artwork;
    TextView trackArtist;
    TextView trackTitle;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        context = this;
        artwork = (ImageView) findViewById(R.id.album_art);
        trackArtist = (TextView) findViewById(R.id.artist_text);
        trackTitle = (TextView) findViewById(R.id.title_text);
        spinner = (ProgressBar) findViewById(R.id.spinner);


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();



        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    protected void handleSendText(Intent intent) {
        String regex = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.i("SC", "GOT: " + text);
        if (text != null) {
            Pattern idPattern = Pattern.compile(regex);
            Matcher m = idPattern.matcher(text);

            if (m.find()) text = m.group();

            final SoundcloudAPI api = new SoundcloudAPI();
            final String finalText = text;
            Utilities.RunInThread(() -> {
                api.fetchCredentials();
                log("Fetched credentials");
                SoundcloudObject obj = api.resolve(finalText);
                if (obj instanceof Track) {
                    log("Resolved track");
                    Track track = (Track) obj;
                    Utilities.RunInUIThread(() -> {
                        trackArtist.setText(track.getArtist());
                        trackTitle.setText(track.getTitle());
                        spinner.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
                    });
                    final Bitmap image = Utilities.downloadImage(track.getAlbumArtURL());
                    Utilities.RunInUIThread(() -> artwork.setImageBitmap(image));
                    final File downloadedTrack = downloadTrack(track);
                    TrackDatabase db = new TrackDatabase(context);
                    db.put(track);
                    Utilities.RunInUIThread(() -> {
                        Toast.makeText(context, "Download Finished", Toast.LENGTH_LONG).show();
                        spinner.setVisibility(View.INVISIBLE);
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(downloadedTrack);
                        mediaScanIntent.setData(contentUri);
                        this.sendBroadcast(mediaScanIntent);

                    });
                    Utilities.RunInUIThread(() -> ((Activity) context).finishAndRemoveTask(), 3000);
                }
            });
        } else {
            Log.e("SC", "Could not find url in input text: " + text);
        }
    }


    public File downloadTrack(Track track){
        try {
            URL url = new URL(track.getDownloadURL());
            URLConnection conection = url.openConnection();
            conection.connect();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write
            String filename = String.format("%s - %s.mp3", track.getArtist(), track.getTitle());

            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Soundcloud");

            if(!storageDir.exists()) storageDir.mkdirs();

            File mp3File = new File(storageDir, filename);

            if(!mp3File.exists())mp3File.createNewFile();


            mp3File.setReadable(true, false);
            OutputStream output = new FileOutputStream(mp3File);

            byte data[] = new byte[1024];

            long total = 0;
            int count;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            return mp3File;

        } catch (Exception e) {
            Log.e("SC", "Error in Download File");
            e.printStackTrace();
            return null;
        }
    }

    public void log(String stuff){
        Log.d("SC", stuff);
    }

}
