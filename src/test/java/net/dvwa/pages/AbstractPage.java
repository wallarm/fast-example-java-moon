package net.dvwa.pages;

import net.dvwa.util.Config;
import net.dvwa.util.SharedWebDriver;

public class AbstractPage {
  protected static final String TARGET_APP_BASE_URL;
  static {
    if (Boolean.parseBoolean(Config.get("fast_proxy.mode.is_reverse"))) {
      TARGET_APP_BASE_URL = Config.get("fast_proxy.base_url");
    } else {
      TARGET_APP_BASE_URL = Config.get("target_app.base_url");
    }
  }

  protected final SharedWebDriver driver = SharedWebDriver.getSharedWebDriver();

  public <T extends AbstractPage> T newPage(Class<T> clazz) {
    T page;
    try {
      page = clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return page;
  }

  public String getBaseUrl() {
    return TARGET_APP_BASE_URL;
  }

  public String getCurrentPageUrl() {
    return driver.getCurrentUrl();
  }

  public void refreshPage() {
    driver.navigate().refresh();
    driver.waitForPageToLoad();
  }

  public void waitForPageToLoad() {
    driver.waitForPageToLoad();
  }

  public String getTitle() {
    return driver.getTitle();
  }

  public String getPageSource() {
    return driver.getPageSource();
  }
}
