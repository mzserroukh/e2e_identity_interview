import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class Locators {

    public boolean isElementDisplayed(WebDriver driver, By loc) {
        {
            try {

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                wait.until(ExpectedConditions.visibilityOfElementLocated(loc));

                WebElement element = driver.findElement(loc);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: \"center\"})", element);

                return element.isDisplayed();
            } catch (NoSuchElementException | TimeoutException e) {
                return false;
            }
        }
    }

    public void waitElement(WebDriver driver, By loc, int waitTime) {
        {
            try {

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));

                wait.until(ExpectedConditions.or(
                        ExpectedConditions.presenceOfElementLocated(loc),
                        ExpectedConditions.visibilityOfElementLocated(loc)
                        )
                );

                WebElement element = driver.findElement(loc);
                element.isDisplayed();
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("No such element was found:" + loc);
            } catch (TimeoutException e) {
                throw new TimeoutException(e);
            }
        }
    }

    public void clickElement(WebDriver driver, By loc) {
        try {
            Thread.sleep(2000);
            isElementDisplayed(driver, loc);
            WebElement element = driver.findElement(loc);
            if (element.isDisplayed() && element.isEnabled()) {
                driver.findElement(loc).click();
            }
        } catch (NoSuchElementException | TimeoutException | InterruptedException e) {
            throw new Error(e);
        }
    }

    public void insertText(WebDriver driver, By loc, String text) {
        try {
            assert isElementDisplayed(driver, loc);

            WebElement field = driver.findElement(loc);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: \"center\"})", field);

            field.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END));
            field.sendKeys(Keys.DELETE);

            field.clear();
            field.sendKeys(text);
        } catch (NoSuchElementException | TimeoutException e) {
            throw new Error(e);
        }
    }

    public String getText(WebDriver driver, By loc) {
        isElementDisplayed(driver, loc);
        WebElement element = driver.findElement(loc);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: \"center\"})", element);
        return element.getText();
    }
}
