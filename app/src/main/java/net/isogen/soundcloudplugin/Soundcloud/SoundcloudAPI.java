package net.isogen.soundcloudplugin.Soundcloud;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SoundcloudAPI {

    protected String FETCH_URL = "https://soundcloud.com/hermei/dreams";
    protected String RESOLVE_URL = "https://api-v2.soundcloud.com/resolve?url=%s&client_id=%s";
    protected String ISO_RESOLVE_URL = "https://isogen.net/api/soundcloud/resolve?url=%s";
    protected String ISO_DOWNLOAD_URL = "https://isogen.net/api/soundcloud/tracks/%s";
    private String clientId;

    public SoundcloudAPI(){

    }

    public SoundcloudObject resolve(String url){
        if(url.contains("m.soundcloud")){
            url = url.replace("m.soundcloud", "soundcloud");
        }
        try{
            JSONObject object = getWebpageObject(String.format(RESOLVE_URL, url, clientId));
            if(object != null && object.get("kind").equals("track")) {
                return new Track(object, this);
            }


        }catch (JSONException e){

        }
        return null;
    }





    public void fetchCredentials() {

        String html = getWebpageText(FETCH_URL);
        Document document = Jsoup.parse(html);
        Elements scripts = document.select("script[src]");
        for(final Element script : scripts){
            final String src = script.attr("src");
            String parts[] = src.split("/");
            String scriptName = parts[parts.length - 1];
            if(scriptName.contains("app")){
                Pattern idPattern = Pattern.compile("client_id:\"([a-zA-Z0-9]+)\"");
                String scriptText = getWebpageText(src);
                Matcher m = idPattern.matcher(scriptText);
                if(m.find()){
                    clientId = m.group(1);
                    return;
                }
            }
        }
    }

    public byte[] getWebpageBytes(String url){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Win98; en-US; rv:1.7.2) Gecko/20040803");
            conn.connect();
            is = conn.getInputStream();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace ();
            // Perform any other exception handling that's appropriate.
        }

        return baos.toByteArray();
    }

    public String getWebpageText(String url){
        byte webpageBytes[] = getWebpageBytes(url);
        try {
            return new String(webpageBytes, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getWebpageObject(String url){
        try {
            return new JSONObject(getWebpageText(url));
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getClientId() {
        return clientId;
    }
}
