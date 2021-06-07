package net.dvwa.util;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.SECONDS;

public class SharedWebDriver extends RemoteWebDriver {
  private static final ThreadLocal<SharedWebDriver> WEB_DRIVER_HOLDER = new ThreadLocal<>();
  private static final String SELENIUM_SERVER_URL_DEFAULT = "http://minikube:4444/wd/hub";
  private static final String SELENIUM_SERVER_URL = (String) Config.getConfig().getOrDefault(
          "selenium.server_url", SELENIUM_SERVER_URL_DEFAULT);
  private static final long FIND_ELEMENT_DEFAULT_TIMEOUT = 10;
  private static final long SCRIPT_DEFAULT_TIMEOUT = 30;
  private static final long PAGE_LOAD_DEFAULT_TIMEOUT = 60;

  private SharedWebDriver(URL addressOfRemoteServer, Capabilities capabilities) {
    super(addressOfRemoteServer, capabilities);

    manage().timeouts().implicitlyWait(FIND_ELEMENT_DEFAULT_TIMEOUT, SECONDS);
    manage().timeouts().setScriptTimeout(SCRIPT_DEFAULT_TIMEOUT, SECONDS);
    manage().timeouts().pageLoadTimeout(PAGE_LOAD_DEFAULT_TIMEOUT, SECONDS);
  }

  public static SharedWebDriver getSharedWebDriver() {
    if (WEB_DRIVER_HOLDER.get() == null) {
      final URL seleniumServerUrl;
      try {
        seleniumServerUrl = new URL(SELENIUM_SERVER_URL);
      } catch (MalformedURLException e) {
        throw new RuntimeException("Failed to build url", e);
      }
      SharedWebDriver driver = new SharedWebDriver(seleniumServerUrl, getDefaultDesiredCapabilities());
      WEB_DRIVER_HOLDER.set(driver);
      return driver;
    }
    return WEB_DRIVER_HOLDER.get();
  }

  public static SharedWebDriver getCurrentDriver() {
    return WEB_DRIVER_HOLDER.get();
  }

  private static DesiredCapabilities getDefaultDesiredCapabilities() {
    DesiredCapabilities capabilities = new DesiredCapabilities();

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--dom.max_chrome_script_run_time=150");
    options.addArguments("--dom.max_script_run_time=150");
    options.addArguments("--start-maximized");
    options.addArguments("--disable-gpu");
    options.addArguments("--disable-notifications");
    capabilities.setCapability(ChromeOptions.CAPABILITY, options);

    return capabilities;
  }

  public <T> T waitForCondition(Function<? super WebDriver, T> isTrue, long timeoutInSeconds, String msg) {
    WebDriverWait wait = new WebDriverWait(this, timeoutInSeconds);
    return wait.withMessage(msg).until(isTrue);
  }

  public <T> T waitForCondition(Function<? super WebDriver, T> isTrue, long timeoutInSeconds) {
    return waitForCondition(isTrue, timeoutInSeconds, null);
  }

  public final void waitForPageToLoad(final long timeout) {
    waitForCondition(new Function<SearchContext, Boolean>() {
      private Object result;

      @Override
      public Boolean apply(SearchContext searchContext) {
        final String script = "return document.readyState && document.readyState == 'complete'";
        result = ((JavascriptExecutor) searchContext).executeScript(script);
        return result != null && result instanceof Boolean && ((Boolean) result);
      }

      @Override
      public String toString() {
        return String.format("page wasn't fully loaded in %s seconds, last value of document.readyState - %s", timeout, result);
      }
    }, timeout);
  }

  public final void waitForPageToLoad() {
    waitForPageToLoad(PAGE_LOAD_DEFAULT_TIMEOUT);
  }

  public static void stopWebDriver(WebDriver driver) {
    if (driver != null) {
      WEB_DRIVER_HOLDER.remove();
      driver.close();
      driver.quit();
    }
  }

  public WebElement getWhenVisible(By locator, long timeout) {
    WebDriverWait wait = new WebDriverWait(getCurrentDriver(), timeout);
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public WebElement getWhenVisible(By locator) {
    return getWhenVisible(locator, FIND_ELEMENT_DEFAULT_TIMEOUT);
  }

  public List<WebElement> getVisibleElements(By locator) {
    WebDriverWait wait = new WebDriverWait(getCurrentDriver(), FIND_ELEMENT_DEFAULT_TIMEOUT);
    return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
  }

  public List<WebElement> getPresentElements(By locator) {
    WebDriverWait wait = new WebDriverWait(getCurrentDriver(), FIND_ELEMENT_DEFAULT_TIMEOUT);
    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
  }

  public List<WebElement> getElements(By locator, int moreThan) {
    WebDriverWait wait = new WebDriverWait(getCurrentDriver(), FIND_ELEMENT_DEFAULT_TIMEOUT);
    return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, moreThan));
  }

  public void clickWhenReady(By locator, long timeout) {
    WebDriverWait wait = new WebDriverWait(getCurrentDriver(), timeout);
    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    element.click();
  }

  public void clickWhenReady(By locator) {
    clickWhenReady(locator, FIND_ELEMENT_DEFAULT_TIMEOUT);
  }

  public void clickAndWaitForPageToLoad(By locator) {
    clickWhenReady(locator);
    waitForPageToLoad();
  }

  public void scrollToElement(WebElement element) {
    new Actions(getCurrentDriver()).moveToElement(element);
  }
}
