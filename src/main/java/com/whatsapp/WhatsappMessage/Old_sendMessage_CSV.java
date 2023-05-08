package com.whatsapp.WhatsappMessage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Old_sendMessage_CSV {
	
	static WebDriverWait wait;
	static String csvFile = "Resources//Message.csv";
	static String line = "";
	static String csvSplitBy = ",";
	public static String[] contactInfo = null;

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
		System.setProperty("webdriver.http.factory", "jdk-http-client");
		System.setProperty("webdriver.chrome.driver", "Resources//chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.setExperimentalOption("debuggerAddress","localhost:9014");
		 
		WebDriver driver= new ChromeDriver(options);
		driver.manage().timeouts().getImplicitWaitTimeout();
		
		driver.findElement(By.xpath("//span[@data-testid='chat']")).click();
		
		Thread.sleep(2000);
		
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) 
		 {
		 	while ((line = br.readLine()) != null) 
			{
		    	contactInfo = line.split(csvSplitBy);
		   	   	int i = 0;
		   	   	int j = 1;
				driver.findElement(By.xpath("//div[@title='Search input textbox']/p")).sendKeys(contactInfo[i]);
				Thread.sleep(2000);
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ENTER).build().perform();
				driver.findElement(By.xpath("//div[@title='Type a message']/p")).sendKeys(contactInfo[j]);
				Thread.sleep(2000);
				action.sendKeys(Keys.ENTER).build().perform();
		        i++;
		        j++;	            	
			}
		
		 }
	}

}
