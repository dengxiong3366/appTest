package com.dx.base;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.dx.util.Constants;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class BaseActivity {
	public static Logger logger = Logger.getLogger(BaseActivity.class.getName());
	public static AndroidDriver<AndroidElement> driver;
	@BeforeClass
	public void SetUp() throws Exception {
		PropertyConfigurator.configure("config/log4j.properties");
		logger.info("----------------测试用例执行开始-------------------");
		File apk = new File(System.getProperty("user.dir")+File.separator+"apps"+File.separator+Constants.测试手机_dx.apkName);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", Constants.测试手机_dx.deviceName);
		capabilities.setCapability("platformVersion", Constants.测试手机_dx.platformVersion);
		capabilities.setCapability("platformName", Constants.测试手机_dx.platformName);
		capabilities.setCapability("app", apk);
		capabilities.setCapability("appActivity", Constants.测试手机_dx.appActivity);
		capabilities.setCapability("unicodeKeyboard", Constants.测试手机_dx.unicodeKeyboard);
		capabilities.setCapability("noSign", Constants.测试手机_dx.noSign);
		driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	@AfterClass
	public void endTest() {
		logger.info("----------------测试用例执行结束-------------------");
		driver.quit();
	}
	
	public AndroidDriver<AndroidElement> getDriver(){
		return driver;
	}
}
