package net.isogen.soundcloudplugin;

import net.isogen.soundcloudplugin.Soundcloud.SoundcloudAPI;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SoundcloudAPIUnitTest {
    @Test
    public void clientId_isFound(){
        SoundcloudAPI api = new SoundcloudAPI();

        api.fetchCredentials();

        assertNotEquals(api.getClientId(), null);
    }
}