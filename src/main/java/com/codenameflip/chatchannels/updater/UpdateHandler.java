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
    private String VERSION_URL;
    private String DESCRIPTION_URL;

    public UpdateHandler(String resourceID)
    {
        this.resourceID = resourceID;

        VERSION_URL = "https://api.spiget.org/v2/resources/" + resourceID + "/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";
        DESCRIPTION_URL = "https://api.spiget.org/v2/resources/" + resourceID + "/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=ChatChannels";
    }

    public String getResourceID()
    {
        return resourceID;
    }

    private JSONArray getVersions()
    {
        try
        {
            return (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
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
        return ((JSONObject) getVersions().get(getVersions().size() - 1)).get("name").toString();
    }

    private boolean isNewVersionAvailable()
    {
        return getVersionNumber(getLatestVersionName()) > getVersionNumber(ChatChannels.get().getDescription().getVersion());
    }

    private JSONArray getUpdates()
    {
        try
        {
            return (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(DESCRIPTION_URL)));
        } catch (ParseException | IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private String getLatestUpdateTitle()
    {
        return ((JSONObject) getUpdates().get(getUpdates().size() - 1)).get("title").toString();
    }

    /**
     * Queries the SpiGet servers and retrieves the latest release information (if current plugin version is outdated.)
     *
     * @return The updated version number [0], and update title [1] of the resource update
     */
    public Object[] retrieveUpdateInformationIfAvailable()
    {
        if (isNewVersionAvailable())
            return new Object[]{getLatestVersionName(), getLatestUpdateTitle()};

        return null;
    }

}