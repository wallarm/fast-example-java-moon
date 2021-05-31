package net.dvwa.pages;

import org.openqa.selenium.By;

public class SqliPage extends AbstractPage {
    public SqliPage submitUserId(String userId) {
        driver.getWhenVisible(By.name("id")).sendKeys(userId);
        driver.clickAndWaitForPageToLoad(By.name("Submit"));
        return this;
    }
}
