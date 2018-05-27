package net.isogen.soundcloudplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import net.isogen.soundcloudplugin.Soundcloud.SoundcloudAPI;
import net.isogen.soundcloudplugin.Soundcloud.SoundcloudObject;
import net.isogen.soundcloudplugin.Soundcloud.Track;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTrackActivity extends AppCompatActivity {

    ActionBar actionBar;

    Context context;

    ImageView artwork;
    TextView trackArtist;
    TextView trackTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        context = this;
        artwork = (ImageView) findViewById(R.id.album_art);
        trackArtist = (TextView) findViewById(R.id.artist_text);
        trackTitle = (TextView) findViewById(R.id.title_text);


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        actionBar = getSupportActionBar();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    protected void handleSendText(Intent intent){
        final String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.i("SC", "GOT: " + text);
        if(text != null){
            log("Recieved Intent text: " + text);
            final SoundcloudAPI api = new SoundcloudAPI();

            new Thread(()->{
                api.fetchCredentials();
                log("Fetched credentials");
                SoundcloudObject obj = api.resolve(text);
                if(obj instanceof Track){
                    log("Resolved track");
                    Track track = (Track)obj;
                    Utilities.RunInUIThread(()->{
                        trackArtist.setText(track.getArtist());
                        trackTitle.setText(track.getTitle());
                    });
                    final Bitmap image = Utilities.downloadImage(track.getAlbumArtURL());
                    Utilities.RunInUIThread(()-> artwork.setImageBitmap(image));
                    downloadTrack(track);
                    Utilities.RunInUIThread(()->Toast.makeText(context, "Download Finished", Toast.LENGTH_LONG).show());
                    Utilities.RunInUIThread(()->((Activity)context).finishAndRemoveTask(), 3000);
                }
            }).start();
        }
    }



    public boolean downloadTrack(Track track){
        try {
            URL url = new URL(track.getDownloadURL());
            URLConnection conection = url.openConnection();
            conection.connect();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write
            String filename = String.format("%s - %s.mp3", track.getArtist(), track.getTitle());

            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

            File mp3File = new File(storageDir, filename);

            Log.i("SC", mp3File.getAbsolutePath());

            if(!mp3File.createNewFile()) return false;
            log("Created file");
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
            return true;

        } catch (Exception e) {
            Log.i("SC", "Error in Download File");
            e.printStackTrace();
            return false;
        }
    }

    public void log(String stuff){
        Log.d("SC", stuff);
    }




}
