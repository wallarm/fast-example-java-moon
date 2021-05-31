package net.dvwa.pages;

import org.openqa.selenium.By;

public class MainPage extends AbstractPage {
    public SqliPage goToSqliPage() {
        driver.clickAndWaitForPageToLoad(By.linkText("SQL Injection"));
        return new SqliPage();
    }
}
