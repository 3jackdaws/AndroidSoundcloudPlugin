package net.isogen.soundcloudplugin.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.isogen.soundcloudplugin.R;

public class FragmentRecentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_recent);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!= null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Recent Tracks");
        }


    }
}
