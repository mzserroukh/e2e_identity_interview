import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InterviewTest {

    private final Helpers helper = new Helpers();

    // Regular expression to match UK number plates
    private static final String UK_NUMBER_PLATE_REGEX = "([A-Z]{2}[\\ ]?[0-9]{2}[\\ ]?[A-Z]{3})";

    private static final String fileNameCarInput = "car_input - V5.txt";
    private static final String fileNameCarOutput = "car_output - V5.txt";
    private static WebDriver driver;
    private final Locators locator = new Locators();

    @BeforeAll
    public static void beforeAll() {
        driver = WebDriverManager.chromedriver().create();
    }

    @BeforeEach
    public void beforeTest() throws Exception {
        driver.get("https://motorway.co.uk/");
        driver.manage().window().maximize();
        assertTrue(driver.getTitle().contains("Sell My Car | Fast, Free"));
        locator.waitElement(driver, By.id("main"), 5);
    }

    @Test
    void test() {
        List<String> numberPlates = helper.getAllNumberPlates(UK_NUMBER_PLATE_REGEX, fileNameCarInput);
        System.out.println(numberPlates);

        numberPlates.forEach(plate -> {
            try {
                String formattedNumberPlate = helper.formatPatternNumberPlate(plate);
                System.out.println(formattedNumberPlate);

                String carInfo = helper.returnOutputSentenceBasedOnNumberPlates(plate, fileNameCarOutput);
                System.out.println(carInfo);

                if (carInfo == null) throw new NullPointerException(String.format("%s Number plate is not matched in output", formattedNumberPlate));

                locator.insertText(driver, By.id("vrm-input"), formattedNumberPlate);
                locator.clickElement(driver, By.xpath("//form[@data-cy='vrmInputForm']//button/*[text()='Value your car']"));
                locator.waitElement(driver, By.xpath("//*[contains(@class, 'HeroVehicle__component')]"), 20);
                assertAll(
                        () -> assertEquals(locator.getText(driver, By.xpath("//*[contains(@class, 'VRM-module__regular')]")), formattedNumberPlate),
                        () -> assertTrue(carInfo.contains(locator.getText(driver, By.xpath("//*[contains(@data-cy, 'vehicleMakeAndModel')]")))),
                        () -> assertTrue(carInfo.contains(locator.getText(driver, By.xpath("//*[contains(@data-cy, 'vehicleSpecifics')]/li[1]"))))
                );

            } catch (Exception e) {
                throw new Error("test failed, read logs", e);
            } finally {
                // return to home page
                locator.clickElement(driver, By.xpath("//a[@aria-label='sell my car']"));
                locator.waitElement(driver, By.id("main"), 5);
            }
        });
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
    }
}
