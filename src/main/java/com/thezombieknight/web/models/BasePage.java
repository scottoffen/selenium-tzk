package com.thezombieknight.web.models;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage
{
	public static final By contentLocator    = By.className("post-body");
	public static final By nextLinkLocator   = By.xpath(".//*[@id='Blog1_blog-pager-newer-link']");
	public static final By dateStringLocator = By.xpath(".//*[@id='Blog1']/div[1]/div/h2/span");

	protected WebDriver driver;
	protected String pageUrl;
	public String Url;

 	public BasePage (WebDriver driver)
	{
		this.driver  = driver;
		this.pageUrl = driver.getCurrentUrl();
		this.Url = driver.getCurrentUrl();
	}
 
	public boolean isContentPage ()
	{
		return (this.getTitle().startsWith("The Zombie Knight: Page")) ? true : false;
	}

	public String getContent ()
	{
		try
		{
			return this.clean(this.tidy(this.getText(BasePage.contentLocator)));
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public BasePage getNextPage ()
	{
		try
		{
			Wait<WebDriver> wait;
			String title = this.getTitle();
	
			this.click(BasePage.nextLinkLocator);
	
	        wait = new WebDriverWait(driver, SiteData.TIMEOUT_WAIT, SiteData.TIMEOUT_SLEEP);
	        wait.until(ExpectedConditions.not(ExpectedConditions.titleIs(title)));
	
	        return new BasePage(driver);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public String getDate ()
	{
		return this.getText(dateStringLocator);
	}

	protected String tidy (String content)
	{
		content = content.replace("Click to display entire chapter at once -- (mobile link)\n", "");
		content = content.replace("--donation bonus .+\n", "");
		content = content.replace("“", "\"");
		content = content.replace("”", "\"");
		content = content.replace("‘", "'");
		content = content.replace("’", "'");
		return content;
	}

	protected String clean (String content)
	{
		// remove/replace foul language, temporarily removed
		return content;
	}

  	protected void click (By locator) throws IllegalStateException
	{
		try
		{
			WebElement el = driver.findElement(locator);
			el.click();
		}
		catch (NoSuchElementException e)
		{
			throw new IllegalStateException(this.pageUrl + " : Cannot find element by locator : " + locator.toString());
		}
	}

	protected void submit (By locator) throws IllegalStateException
	{
		try
		{
			WebElement el = driver.findElement(locator);
			el.submit();
		}
		catch (NoSuchElementException e)
		{
			throw new IllegalStateException(this.pageUrl + " : Cannot find element by locator : " + locator.toString());
		}
	}

	protected void type (By locator, String value) throws IllegalStateException
	{
		try
		{
			WebElement el = driver.findElement(locator);
			el.sendKeys(value);
		}
		catch (NoSuchElementException e)
		{
			throw new IllegalStateException(this.pageUrl + " : Cannot find element by locator : " + locator.toString());
		}
	}

	protected String getText (By locator) throws IllegalStateException
	{
		try
		{
			WebElement el = driver.findElement(locator);
			return el.getText();
		}
		catch (NoSuchElementException e)
		{
			throw new IllegalStateException(this.pageUrl + " : Cannot find element by locator : " + locator.toString());
		}
	}

	protected String getAttribute (By locator, String attr) throws IllegalStateException
	{
		try
		{
			WebElement el = driver.findElement(locator);
			return el.getAttribute(attr);
		}
		catch (NoSuchElementException e)
		{
			throw new IllegalStateException(this.pageUrl + " : Cannot find element by locator : " + locator.toString());
		}
	}

	public String getTitle ()
	{
		return driver.getTitle();
	}
}
