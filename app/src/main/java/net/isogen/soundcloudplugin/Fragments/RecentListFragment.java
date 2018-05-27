package net.isogen.soundcloudplugin.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.isogen.soundcloudplugin.Activities.FragmentRecentActivity;
import net.isogen.soundcloudplugin.Activities.TrackDetailActivity;
import net.isogen.soundcloudplugin.Database.TrackDatabase;
import net.isogen.soundcloudplugin.R;
import net.isogen.soundcloudplugin.Soundcloud.Track;

import java.util.List;

public class RecentListFragment extends Fragment {
    ListView recentList;
    List<Track> recentTracks;
    Context context;
    TrackDetailFragment detailFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recentList = (ListView)view.findViewById(R.id.recent_list);


        recentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(detailFragment == null){
                    detailFragment = (TrackDetailFragment) ((FragmentRecentActivity)getActivity()).getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
                }

                Track selectedTrack = recentTracks.get(position);



                if(detailFragment != null && detailFragment.isVisible()){
                    detailFragment.setTrack(selectedTrack);
                }else{
                    Intent i = new Intent(getActivity(), TrackDetailActivity.class);
                    i.putExtra("TRACK_ID", selectedTrack.getId());
                    startActivity(i);
                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecents();
    }

    void loadRecents(){
        TrackDatabase database = new TrackDatabase(getActivity());

        recentTracks = database.getRecentTracks(50);

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_text_view);

        adapter.addAll(recentTracks);

        recentList.setAdapter(adapter);
    }
}
