package com.codenameflip.chatchannels.updater;

import com.codenameflip.chatchannels.ChatChannels;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * @author codenameflip
 * @since 4/8/17
 */

public class UpdateHandler {

    private String resourceID;

    public UpdateHandler(String resourceID)
    {
        this.resourceID = resourceID;
    }


    private final String updateTitle = ((JSONObject) getUpdates().get(getUpdates().size() - 1)).get("title").toString();
    private final String updateVersionName = ((JSONObject) getVersions().get(getVersions().size() - 1)).get("name").toString();
    private final String descriptionURL = "https://api.spiget.org/v2/resources/%ID%/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";
    private final String versionURL = "https://api.spiget.org/v2/resources/%ID%/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";

    public String getResourceID()
    {
        return resourceID;
    }

    private String substituteResourceID(String baseURL) {
        return baseURL.replaceAll("%ID%", resourceID);
    }

    private JSONArray getVersions()
    {
        try
        {
            return (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(substituteResourceID(versionURL)))));
        } catch (ParseException | IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private int getVersionNumber(String versionId)
    {
        return Integer.parseInt(versionId.replaceAll("\\D+", ""));
    }

    private String getLatestVersionName()
    {
        return updateVersionName;
    }

    private boolean isNewVersionAvailable()
    {
        return getVersionNumber(getLatestVersionName()) > getVersionNumber(ChatChannels.get().getDescription().getVersion());
    }

    private JSONArray getUpdates()
    {
        try
        {
            return (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(substituteResourceID(descriptionURL))));
        } catch (ParseException | IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private String getLatestUpdateTitle()
    {
        return updateTitle;
    }

    /**
     * Queries the SpiGet servers and retrieves the latest release information (if current plugin version is outdated.)
     *
     * @return The updated version number [0], and update title [1] of the resource update
     */
    public Optional<Object[]> retrieveUpdateInformationIfAvailable()
    {
        if (this.isNewVersionAvailable())
            return Optional.of(new Object[]{getLatestVersionName(), getLatestUpdateTitle()});

        return Optional.empty();
    }

}