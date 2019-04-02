package com.dx.util;




import org.apache.log4j.Logger;

import com.dx.testScripts.TestRegister;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;

public class Action {
	static Logger log = Logger.getLogger(Action.class.getName());
	
	private static AndroidDriver<AndroidElement> driver;
	
	public Action(AndroidDriver<AndroidElement> driver) {
		this.driver = driver;
	}
	/*
	 * 实现单击操作，data没有意义，是为了维护脚本的完整性
	 */
	public void click(MobileElement mobileElement,String data) {
		try {
			mobileElement.click();
		} catch (Exception e) {
			try {
				Thread.sleep(Constants.测试控件.elementInspectInterval);
			} catch (InterruptedException e1) {
				TestRegister.testResult = false;
				e1.printStackTrace();
			}
			mobileElement.click();
		}
	}
	
	/*
	 * 单元框单击，radio按钮是否选中，可以获取状态，当需要选中时，目前状态为选中不执行操作，若为未选中状态则执行单击选中操作
	 */
	public void click_radio(MobileElement mobileElement,String data) {
		try {
			if(data.toLowerCase().equals("yes")) {
				if(!mobileElement.isSelected())
					mobileElement.click();
			}
		} catch (Exception e) {
			TestRegister.testResult = false;
			e.printStackTrace();
		}
	}
	
	/*
	 * 清空编辑框的数据，然后进行输入
	 */
	public void input(MobileElement mobileElement,String data) {
		try {
			this.click(mobileElement, data);
			mobileElement.clear();
			mobileElement.sendKeys(data);
		} catch (Exception e) {
			TestRegister.testResult = false;
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 后退操作
	 */
	public void back(MobileElement mobileElement,String data) {
		driver.pressKeyCode(AndroidKeyCode.BACK);
	}
	
	/*
	 * 对输入数据进行验证
	 */
	public void verify(MobileElement mobileElement,String data) {
		String actualResult;
		try {
			actualResult = mobileElement.getAttribute("text");
			if(!actualResult.equals(data)) {
				TestRegister.testResult  = false;
			}
		} catch (Exception e) {
			TestRegister.testResult = false;
		}
	}
	
	/*
	 * 等待Activity跳转
	 */
	
	public static void waitForLoadingActivity(MobileElement mobileElement,String data)throws InterruptedException {
		Thread.sleep(3000);
		log.info(driver.currentActivity());
		int activityInspectCount,activityInspectInterval;
		activityInspectCount = Constants.测试控件.activityInspectCount;
		activityInspectInterval = Constants.测试控件.activityInspectInterval;
	int i = 0;
	Thread.sleep(activityInspectInterval);
	while(i<activityInspectCount) {
		try {
			if(data.contains(driver.currentActivity())) {
				log.info(data + "出现！");
				break;
			}
			else {
				log.info(data + "未出现! Waiting..........ls");
				Thread.sleep(activityInspectInterval);
				i++;
			}
		} catch (Exception e) {
			i++;
			log.info("尝试"+activityInspectCount+"次,"+data+",未出现！");
			TestRegister.testResult = false;
		}
	}
		
	}
}
