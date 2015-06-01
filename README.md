![](https://raw.github.com/scottoffen/selenium-tzk/master/tzk.jpg)

A Java/Selenium project to scrape blogspot data from the dark serial [The Zombie Knight](http://thezombieknight.blogspot.com).

**\src\main\resources\config.properties:**


    startPage=http\://thezombieknight.blogspot.com/2013/04/page-1.html
    seleniumNode=http\://localhost:5556/wd/hub
    fileName=tzk.txt

You can edit the properties file at the path above, and change the following values:

- *startPage* : the fully qualified domain name and page to start scraping
- *seleniumNode* : the url to the selenium node to send the task to
- *fileName* : the name of the file to save the data to

Any values not provided will default to the ones you see above.

By design, the output file will show up in the same folder as the jar file.


###Set Up Selenium on Windows###
In addition to having a [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Apache Maven](http://maven.apache.org/download.cgi) installed, you will also need access to a Selenium Grid or a Selenium Grid Node.  Setting one up locally can be easy.

- Download the [standalone Selenium server](http://docs.seleniumhq.org/download/) (your version may differ from mine)
- Download the [Chrome WebDriver](https://sites.google.com/a/chromium.org/chromedriver/).  You can use any driver you want, including the built-in Firefox driver, but the project is configured to use a Chrome driver. If you want to change the driver being used, you'll want to edit the code.
- Put both files in same directory
- Open a command-line in that directory and run:

    java -jar selenium-server-standalone-2.39.0.jar -role node -register false -Dwebdriver.chrome.driver=chromedriver.exe

Your node should be up and running!
