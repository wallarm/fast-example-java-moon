package net.dvwa.pages;

import org.openqa.selenium.By;

public class LoginPage extends AbstractPage {

  public LoginPage open() {
    driver.get(BASE_URL);
    driver.waitForPageToLoad();
    return this;
  }

  public MainPage login() {
    driver.getWhenVisible(By.name("username")).sendKeys("admin");
    driver.getWhenVisible(By.name("password")).sendKeys("password");
    driver.clickAndWaitForPageToLoad(By.name("Login"));
    return new MainPage();
  }
}
