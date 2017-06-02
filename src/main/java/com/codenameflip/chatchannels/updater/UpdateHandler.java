package com.codenameflip.chatchannels.updater;

import com.codenameflip.chatchannels.ChatChannels;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public final class UpdateHandler {

	private static final String UPDATE_TITLE_NAME = "title";
	private static final String UPDATE_NAME_NAME = "name";
	private static final Pattern VERSION_STRIPPER = Pattern.compile("\\D+");
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	public static final class Builder implements org.apache.commons.lang3.builder.Builder<UpdateHandler>
	{
		
		private String resourceId;
		private String descriptionUrl;
		private String versionUrl;
		
		Builder()
		{
			
		}
		
		@Override
		public UpdateHandler build()
		{
			Objects.requireNonNull(this.resourceId, "resourceId");
			Objects.requireNonNull(this.descriptionUrl, "descriptionUrl");
			Objects.requireNonNull(this.versionUrl, "versionUrl");
			
			return new UpdateHandler(this.resourceId, this.descriptionUrl, this.versionUrl);
		}
		
		public Builder setResourceId(String resourceId)
		{
			this.resourceId = resourceId;
			return this;
		}
		
		public Builder setDescriptionUrl(String descriptionUrl)
		{
			this.descriptionUrl = descriptionUrl;
			return this;
		}
		
		public Builder setVersionUrl(String versionUrl)
		{
			this.versionUrl = versionUrl;
			return this;
		}
		
	}
	
	private final String resourceID;
	private final String descriptionURL;
	private final String versionURL;
	
	UpdateHandler(String resourceID, String descriptionURL, String versionURL)
	{
		this.resourceID = resourceID;
		this.descriptionURL = descriptionURL;
		this.versionURL = versionURL;
	}
	
	public String getResourceID()
	{
		return this.resourceID;
	}
	
	public String getDescriptionURL()
	{
		return this.descriptionURL;
	}
	
	public String getVersionURL()
	{
		return this.versionURL;
	}

	private String substituteResourceID(String baseURL)
	{
		return baseURL.replaceAll("%ID%", this.resourceID);
	}

	private JSONArray getVersions()
	{
		try
		{
			return (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(substituteResourceID(versionURL)))));
		}
		catch (ParseException | IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private int getVersionNumber(String versionId)
	{
		return Integer.parseInt(versionId.replaceAll(UpdateHandler.VERSION_STRIPPER.pattern(), ""));
	}

	private String getLatestVersionName()
	{
		JSONArray versions = this.getVersions();
		
		if (versions == null)
		{
			return null;
		}
		
		return ((JSONObject) versions.get(versions.size() - 1)).get(UpdateHandler.UPDATE_NAME_NAME).toString();
	}

	private boolean isNewVersionAvailable()
	{
		return this.getVersionNumber(this.getLatestVersionName()) > this.getVersionNumber(ChatChannels.get().getDescription().getVersion());
	}

	private JSONArray getUpdates()
	{
		try
		{
			return (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(this.substituteResourceID(this.descriptionURL))));
		}
		catch (ParseException | IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private String getLatestUpdateTitle()
	{
		JSONArray updates = this.getUpdates();
		
		if (updates == null)
		{
			return null;
		}
		
		return ((JSONObject) updates.get(updates.size() - 1)).get(UpdateHandler.UPDATE_TITLE_NAME).toString();
	}

	/**
	 * Queries the SpiGet servers and retrieves the latest release information (if current plugin version is outdated.)
	 *
	 * @return The updated version number [0], and update title [1] of the resource update
	 */
	public Optional<Object[]> retrieveUpdateInformationIfAvailable()
	{
		if (this.isNewVersionAvailable())
		{
			return Optional.of(new Object[]
					{
							this.getLatestVersionName(),
							this.getLatestUpdateTitle()
					});
		}

		return Optional.empty();
	}

}