package net.dvwa.tests;

import org.testng.annotations.Test;
import net.dvwa.pages.LoginPage;
import net.dvwa.pages.MainPage;
import net.dvwa.pages.SqliPage;

import static org.assertj.core.api.Assertions.assertThat;

@Test()
public class DvwaTest extends AbstractTest {

  public void openLoginPage() {
    LoginPage loginPage = new LoginPage().open();
    assertThat(loginPage.getCurrentPageUrl()).contains(loginPage.getBaseUrl());
    assertThat(loginPage.getTitle()).contains("Login :: Damn Vulnerable Web Application");
    assertThat(loginPage.getPageSource()).contains("href=\"http://www.dvwa.co.uk/\"");
  }

  @Test(dependsOnMethods = { "openLoginPage" })
  public void submitUserIdOnSqliPage() {
    MainPage mainPage = new LoginPage().login();
    SqliPage sqliPage = mainPage.goToSqliPage();
    sqliPage.submitUserId("5");
    assertThat(sqliPage.getTitle()).contains("Vulnerability: SQL Injection");
    assertThat(mainPage.getPageSource()).contains("<pre>ID: 5<br>First name: Bob<br>Surname: Smith</pre>");
  }
}