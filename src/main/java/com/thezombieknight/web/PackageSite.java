package com.thezombieknight.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.thezombieknight.web.models.*;

public class PackageSite
{
	public static void main(String[] args)
	{
		new PackageSite(new SiteData()).pack();
	}

	private URL url;
	private DesiredCapabilities capabilities;
	private WebDriver driver;

	private SiteData data;
	private String starturl;
	private String selenium;
	private String filename;
	private String stopDate;
	private boolean stopAtDate;
	private int counter;
	private int maxpages;

	public PackageSite (SiteData data)
	{
		this.data     = data;
		this.starturl = (data.hasProperty("startPage")) ? data.getProperty("startPage") : SiteData.START_PAGE;
		this.selenium = (data.hasProperty("seleniumNode"))  ? data.getProperty("seleniumNode")  : SiteData.SELENIUM_SERVER;
		this.filename = (data.hasProperty("fileName"))  ? data.getProperty("fileName")  : SiteData.FILE_NAME;
		this.maxpages = (data.hasProperty("maxPages"))  ? Integer.parseInt(data.getProperty("maxPages")) : SiteData.MAX_PAGES;
		this.counter  = 0;

		if (data.hasProperty("stopDate"))
		{
			this.stopAtDate = true;
			this.stopDate = data.getProperty("stopDate");
		}

		capabilities = new DesiredCapabilities(
			DesiredCapabilities.chrome().getBrowserName(),
			DesiredCapabilities.chrome().getVersion(),
			Platform.WINDOWS
		);
		
		try
		{
			url = new URL(this.selenium);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}

	public void pack ()
	{
		this.loopAllPages();
		this.cleanup();
	}

	private void loopAllPages ()
	{
		driver = new RemoteWebDriver(url, capabilities);
		BasePage page = getStartPage();

		while (page != null)
		{
			this.data.setProperty("startPage", page.Url);
			this.slurp(page);
			page = this.getNextPage(page);
		}
	}

	private void slurp(BasePage page)
	{
		if (page.isContentPage())
		{
			String content = page.getContent();
			if (content != null)
			{
				this.save(content);
			}
		}
		else
		{
			System.out.println(page.getTitle() + " [" + page.Url + "]");
		}
	}

	private void cleanup ()
	{
		data.save();

        if (driver != null)
        {
            driver.quit();
        }
	}

	private void save (String page)
	{
		if (page.length() > 0)
		{
			try
			{
			    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(this.getFilePath(), true)));
			    writer.println(page + System.lineSeparator());
				writer.close();
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage() + e.getStackTrace());
			}
		}
	}

	private String getFilePath ()
	{
    	  return System.getProperty("user.dir") + File.separator + this.filename;
	}

	private BasePage getStartPage ()
	{
		try
		{
			Wait<WebDriver> wait;
	        driver.get(starturl);
	
	        wait = new WebDriverWait(driver, SiteData.TIMEOUT_WAIT);
	        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("BlogArchive1")));
	        BasePage start = new BasePage(driver);
	
			//--> We always want to get the page after the page we last slurped, unless we are starting from scratch
			if (!start.Url.equals(SiteData.START_PAGE))
			{
				start = this.getNextPage(start);
			}

	        return start;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private BasePage getNextPage (BasePage page)
	{
		this.counter++;
		if (this.counter < this.maxpages)
		{
			try
			{
				page = page.getNextPage();

				if (this.stopAtDate)
				{
					if (page.getDate().endsWith(this.stopDate))
					{
						return null;
					}
				}

				return page;
			}
			catch (Exception e)
			{
				return null;
			}
		}

		return null;
	}
}
