package ebayUIAutomation;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class EbayUIAutomationTest {
	public static void main(String args[]) throws InterruptedException
	{
		WebDriverManager.chromedriver().setup();
		
		//Open browser
		WebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		driver.manage().deleteAllCookies();
		//Navigate to ebay.com
		driver.get("https://www.ebay.com/");
		//Search for ‘book’
		WebElement searchBoxElm=driver.findElement(By.xpath("//input[contains(@title,'Search')]"));
		searchBoxElm.sendKeys("book");
		WebElement searchBtnElm=driver.findElement(By.xpath("//button[contains(@value,'Search')]"));
		searchBtnElm.click();
		//getting List of searched books
	    List<WebElement> bookList=driver.findElements(By.xpath("//li[contains(@id,'item')]//child::div[2]//child::a"));
		WebElement elm=bookList.get(0);
		elm.click();
		String parentWindow=driver.getWindowHandle();
		//Switching to the product Page window
		Set<String> windows=driver.getWindowHandles();
		for(String handle:windows)
		{
			driver.switchTo().window(handle);
		}
		WebElement addToCartBtnElm=driver.findElement(By.xpath("//a[contains(@id,'atcBtn_btn_1')]"));
		addToCartBtnElm.click();
		//Navigating in product pagee windows
		Set<String> cartWindows=driver.getWindowHandles();
		for(String handle:cartWindows)
		{
			driver.switchTo().window(handle);
			Thread.sleep(10);
		}
		
		WebElement seeInCartBtn=driver.findElement(By.xpath("//div[@class='ux-section__item']//a[@class='ux-call-to-action fake-btn fake-btn--primary']"));
		seeInCartBtn.click();
		driver.close();//closing the product page windows
		
		driver.switchTo().window(parentWindow);//Switching to the Parent Window 
		driver.navigate().refresh();         //Refreshing the Window
		Actions act=new Actions(driver);
		WebElement CartElement=driver.findElement(By.xpath("//span[@class='gh-cart__icon']//*[name()='svg']"));
		act.moveToElement(CartElement).build().perform();//Mouse over to the Cart
		Thread.sleep(10);
		//Verifying the Cart Item Count
		WebElement cartItemCount=driver.findElement(By.xpath("//div[@class='gh-info__row--quantity']//span[contains(text(),'1')]"));
		Assert.assertEquals(cartItemCount.getText(), "1","Verify Item count added to Cart");
		driver.quit();
		
	}
}
