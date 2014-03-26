package com.thezombieknight.web.models;

import java.io.FileOutputStream;
import java.util.Properties;

public class SiteData
{
	public static final String SELENIUM_SERVER = "http://localhost:5556/wd/hub";
	public static final String BASE_URL        = "http://thezombieknight.blogspot.com";
	public static final String START_PAGE      = BASE_URL + "/2013/04/page-1.html";
	public static final String FILE_NAME       = "tzk.txt";
	public static final int MAX_PAGES          = 999;
	public static final int TIMEOUT_WAIT       = 10;
	public static final int TIMEOUT_SLEEP      = 1000;

	private String filename = "/config.properties";
	private Properties prop;

	public SiteData()
	{
		this.prop = new Properties();
		try
		{
			this.prop.load(SiteData.class.getResourceAsStream(this.filename));
		}
		catch (Exception e)
		{
			this.prop.setProperty("seleniumNode", SiteData.SELENIUM_SERVER);
			this.prop.setProperty("startPage", SiteData.START_PAGE);
			this.prop.setProperty("fileName", SiteData.FILE_NAME);
			this.save();
		}
	}

	public String getProperty(String key)
	{
		if (this.prop.containsKey(key))
		{
			return this.prop.getProperty(key);
		}

		return null;
	}

	public boolean hasProperty(String key)
	{
		return this.prop.containsKey(key);
	}

	public void setProperty(String key, String value)
	{
		this.prop.setProperty(key, value);
	}

	public void save ()
	{
		try
		{
			this.prop.store(new FileOutputStream(SiteData.class.getResource(this.filename).getPath()), null);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}
}
