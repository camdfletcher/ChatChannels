package com.codenameflip.chatchannels.utils.updater;

import com.codenameflip.chatchannels.ChatChannels;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * ChatChannels
 *
 * @author Cameron Fletcher
 * @since 06/03/2018
 */
public class UpdateWatcher {

    @Getter
    private String resourceID;

    private String VERSION_URL;
    private String DESCRIPTION_URL;

    public UpdateWatcher(String resourceID, String userAgent) {
        this.resourceID = resourceID;

        this.VERSION_URL = "https://api.spiget.org/v2/resources/" + resourceID + "/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=" + userAgent;
        this.DESCRIPTION_URL = "https://api.spiget.org/v2/resources/" + resourceID + "/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=" + userAgent;
    }

    /**
     * Queries the SpiGet servers and retrieves the latest release information (if current plugin version is outdated.)
     *
     * @return The information for the latest update (if the latest build is newer than what is currently running)
     */
    public Optional<TrackedUpdate> getLatestUpdate() {
        try {
            JSONArray versions = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
            String latestVersion = ((JSONObject) versions.get(versions.size() - 1)).get("name").toString();

            int remoteVersionNumber = Integer.parseInt(latestVersion.replaceAll("\\D+", ""));
            int localVersionNumber = Integer.parseInt(ChatChannels.getInstance().getDescription().getVersion().replaceAll("\\D+", ""));

            if (remoteVersionNumber > localVersionNumber) {
                JSONArray updates = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(DESCRIPTION_URL)));
                String latestUpdate = ((JSONObject) updates.get(updates.size() - 1)).get("title").toString();

                return Optional.of(new TrackedUpdate(latestVersion, latestUpdate));
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}

