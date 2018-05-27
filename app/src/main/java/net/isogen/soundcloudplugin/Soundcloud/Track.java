package net.isogen.soundcloudplugin.Soundcloud;

import org.json.JSONException;
import org.json.JSONObject;

public class Track implements SoundcloudObject{

    SoundcloudAPI api;
    JSONObject track;

    public Track(JSONObject object, SoundcloudAPI client){
        api = client;
        track = object;
        cleanAttributes();
    }

    protected void cleanAttributes(){
        try {
            String username = ((JSONObject) getAttribute("user")).getString("username");
            String title = (String)getAttribute("title");
            if(title.contains(" - ")){
                String parts[] = title.split(" - ");
                track.put("artist", parts[0].trim());
                track.put("title", parts[1].trim());
            }else{
                track.put("artist", username);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getArtist(){
        return (String)getAttribute("artist");
    }

    public String getTitle(){
        return (String)getAttribute("title");
    }

    public Object getAttribute(String key){
        try {
            return track.get(key);
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getAlbumArtURL(){
        String smallArt = (String)getAttribute("artwork_url");
        if(smallArt != null){
            return smallArt.replace("large", "t300x300");
        }
        return null;
    }

    public String getDownloadURL(){
        return String.format(api.ISO_DOWNLOAD_URL, (Integer)getAttribute("id"));
    }



}
