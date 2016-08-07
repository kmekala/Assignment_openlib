****_`# Assignment_openlib

Preparation:

  ```
  Install Eclipse (or any other IDE)
  Install TestNG plugin (for Eclipse)
  Install m2e (or similar for any other IDE)
  Import Maven project into IDE
  Update project (Eclipse) or similar
  ```

To execute Selenium Testng tests:
   
   ```
   On Mac:
   
   cd to the project folder
   mvn clean test
   
   For windows:
   
   change line 93 in OpenLibSeleniumTestNg.java under src/test/java : 
   System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/ChromeDriver/chromedriver");
   to 
   System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/ChromeDriver/chromedriver.exe");
  
  cd to the project folder
  mvn clean test
   
   ```
 

  







`_****
