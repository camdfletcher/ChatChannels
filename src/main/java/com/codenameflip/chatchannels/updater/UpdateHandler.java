package com.codenameflip.chatchannels.updater;

import com.codenameflip.chatchannels.ChatChannels;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

/**
 * @author codenameflip
 * @since 4/8/17
 */

public class UpdateHandler {

    private String resourceID;

    public UpdateHandler(String resourceID) {
        this.resourceID = resourceID;
    }

    private final String VERSION_URL = "https://api.spiget.org/v2/resources/" + resourceID + "/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";
    private final String DESCRIPTION_URL = "https://api.spiget.org/v2/resources/" + resourceID + "/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";

    public String getResourceID() {
        return resourceID;
    }

    /**
     * Queries the SpiGet servers and retrieves the latest release information (if current plugin version is outdated.)
     * @return The updated version number [0], and update title [1] of the resource update
     */
    public Object[] getLatestUpdate() {
        try {
            JSONArray versions = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
            String latestVersion = ((JSONObject) versions.get(versions.size() -1)).get("name").toString();

            if (Integer.parseInt(latestVersion.replaceAll("\\.", "")) > Integer.parseInt(ChatChannels.get().getDescription().getVersion().replaceAll("\\.", ""))) {
                JSONArray updates = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(DESCRIPTION_URL)));
                String latestUpdate = ((JSONObject) updates.get(updates.size() - 1)).get("title").toString();

                return new Object[]{ latestVersion, latestUpdate };
            } else
                return null;

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
