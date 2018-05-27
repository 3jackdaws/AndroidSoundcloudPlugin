package net.isogen.soundcloudplugin;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class Utilities {
    public static void RunInUIThread(Runnable r){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(r);
    }

    public static void RunInUIThread(Runnable r, long delayMillis){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(r, delayMillis);
    }

    public static Bitmap downloadImage(String url){
        Bitmap img = null;
        try {
            InputStream in = new URL(url).openStream();
            img = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("SC", e.getMessage());
            e.printStackTrace();
        }
        return img;
    }
}
