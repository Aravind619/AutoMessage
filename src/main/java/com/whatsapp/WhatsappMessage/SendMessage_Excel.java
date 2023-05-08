package com.whatsapp.WhatsappMessage;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SendMessage_Excel {

	static WebDriverWait wait;
	static Actions action;
	static String excelFile = "Resources//Message.xlsx";
	static String imageFilePath = "C:\\Users\\welcome\\eclipse-workspace\\AutoMessage\\Resources\\Image.jpeg";
	static String baseURL = "https://web.whatsapp.com/";
	static String newMessage = "//span[@data-testid='chat']";
	static String searchContact = "//div[@title='Search input textbox']/p";
	static String chatBox = "//div[@title='Type a message']/p";
	static String attach = "//div[@title='Attach']/span";
	static String imageAttach = "//span[@data-testid='attach-image']";
	static String threeDots = "(//span[@data-testid='menu'])[2]";
	static String closeChat = "//div[@role='button'][contains(text(),'Close chat')]";
	static String backButton = "(//span[@data-testid='back'])[1]";
	static String contact = "";
	static String message = "";
	static String imageResponse = "";
	static int counter = 1;
	static Robot robot;
	static XSSFWorkbook wbO;
	static XSSFSheet sheetO;
	static XSSFRow rowO;
	static SimpleDateFormat sdf;
	static String date = "";
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException{
		System.out.println("Do you have an image file to attach? Type 'Y' or 'N'");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		imageResponse = br.readLine();
		robot = new Robot();
		  
		wbO = new XSSFWorkbook();
		sheetO = wbO.createSheet("Status");
		rowO = sheetO.createRow(0);
		rowO.createCell(0).setCellValue("Name");
		rowO.createCell(1).setCellValue("Status");
		sdf = new SimpleDateFormat("dd-MMM-yyyy hhmm aaa");
		Date datef = new Date();
		date = sdf.format(datef);
		
		System.setProperty("webdriver.chrome.driver", "Resources//chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("user-data-dir=C:\\Users\\welcome\\AppData\\Local\\Google\\Chrome\\User Data");
		options.addArguments("profile-directory=Profile 3");
		options.addArguments("start-maximized");

		WebDriver driver = new ChromeDriver(options);
		driver.get(baseURL);
		wait = new WebDriverWait(driver,Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(newMessage)));

		Thread.sleep(4000);
		
		  FileInputStream fis = new FileInputStream(excelFile);
		  XSSFWorkbook wb = new XSSFWorkbook(fis);
		  XSSFSheet mySheet = wb.getSheetAt(0);
		  Iterator<Row> rowIterator = mySheet.iterator();
		  rowIterator.next();
		
		  while(rowIterator.hasNext()) {
			  try{
			  Row row = rowIterator.next();
			  contact = row.getCell(0).toString();
			  message = row.getCell(1).toString();
			  rowO = sheetO.createRow(counter);
			  rowO.createCell(0).setCellValue(contact);
			  driver.findElement(By.xpath(newMessage)).click();
			  driver.findElement(By.xpath(searchContact)).sendKeys(contact); 
			  Thread.sleep(2000); 
			  action = new Actions(driver); 
			  action.sendKeys(Keys.ENTER).build().perform();
			  
			  try{
			  driver.findElement(By.xpath(chatBox)).isEnabled();
			  driver.findElement(By.xpath(chatBox)).sendKeys(message); 
			  }catch(Exception e) {
				  System.out.println("Message failed for "+contact);
				  System.out.println("Error message: Contact not found");
				  rowO.createCell(1).setCellValue("Not sent");
				  counter++;
				  driver.findElement(By.xpath(backButton)).click();
				  continue;
			  }
			  Thread.sleep(2000);
			  if(imageResponse.equalsIgnoreCase("Y")){
				  driver.findElement(By.xpath(attach)).click();
				  wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(imageAttach)));
				  driver.findElement(By.xpath(imageAttach)).click();
				  Thread.sleep(2000);
				  Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(imageFilePath), null);
				  robot.keyPress(KeyEvent.VK_CONTROL);
				  robot.keyPress(KeyEvent.VK_V);
				  robot.keyRelease(KeyEvent.VK_CONTROL);
				  robot.keyRelease(KeyEvent.VK_V);
				  robot.keyPress(KeyEvent.VK_ENTER);
				  robot.keyRelease(KeyEvent.VK_ENTER);
			  }
			  Thread.sleep(2000);
		  	  action.sendKeys(Keys.ENTER).build().perform();
		  	  System.out.println("Message sent to "+contact);
			  wait.until(ExpectedConditions.elementToBeClickable(By.xpath(threeDots)));
			  driver.findElement(By.xpath(threeDots)).click();
			  wait.until(ExpectedConditions.elementToBeClickable(By.xpath(closeChat)));
			  driver.findElement(By.xpath(closeChat)).click();
			  rowO.createCell(1).setCellValue("Sent");
			  counter++;
		  	} catch(Exception e) {
				  System.out.println("Message failed for "+contact);
				  System.out.println("Error message:\n"+e.getMessage());
				  rowO.createCell(1).setCellValue("Not sent");
				  counter++;
				  continue;
			  }		 
		  }
		  FileOutputStream fos = new FileOutputStream("Results//"+date+" Report.xlsx");
	  	  wbO.write(fos);
	  	  fos.close();
	  	  wbO.close();
	  	  System.out.println("Message sent to all contacts");
		  driver.quit();
	}

}
