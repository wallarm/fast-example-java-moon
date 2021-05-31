package net.dvwa.tests;

import org.testng.annotations.AfterClass;
import net.dvwa.util.SharedWebDriver;

public abstract class AbstractTest {

  @AfterClass(alwaysRun = true)
  protected void shutdownDriver() {
    SharedWebDriver.stopWebDriver(SharedWebDriver.getCurrentDriver());
  }
}
