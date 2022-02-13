import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebsiteHandler {
    FirefoxDriver driver;
    WebElement currentGame;

    public WebsiteHandler(boolean watchProgress) {
        driver = new FirefoxDriver(setup(watchProgress));
        navigateToNonograms();
        Login();
        currentGame = driver.findElement(By.id("game"));
    }

    private FirefoxOptions setup(boolean watchProgress) {
        System.setProperty("webdriver.gecko.driver", "C:/Users/joshu/Documents/Programming/Binaries/geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        if (!watchProgress) {
            options.addArguments("--headless"); //run headless
        }
        return options;
    }

    private void Login() {
        driver.findElement(By.id("menu-button-user")).click();
        driver.findElement(By.name("email")).sendKeys("j.perry20011@gmail.com");
        driver.findElement(By.name("password")).sendKeys("BotPassword" + Keys.ENTER);
    }

    private void navigateToNonograms() {
        driver.get("https://www.puzzle-nonograms.com/"); //open nonogram website
        driver.findElement(By.className("css-47sehv")).click(); //accept cookies
    }

    public WebElement getColumnConstraints() {
        return driver.findElement(By.id("taskTop"));
    }

    public WebElement getRowConstraints() {
        return driver.findElement(By.id("taskLeft"));
    }

    public int getSize() {
        return driver.findElement(By.className("row")).findElements(By.className("cell")).size();
    }

    public void solvePuzzle(int[][] solution) { //Input the solution to the website
        //pixels are left click
        //crosses are right click
        //hit enter to finish
    }

    public void newPuzzle() { //get new puzzle on site

    }

    public boolean checkIfRight() {
        //check if submitted solution (solvePuzzle()) is correct
        return false;
    }


    public void closeBrowser() {
        driver.quit();
    }
}
