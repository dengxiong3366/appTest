package com.dx.testScripts;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dx.base.BaseActivity;
import com.dx.util.Action;
import com.dx.util.Constants;
import com.dx.util.DataproviderFromExcel;
import com.dx.util.FindElement;

import io.appium.java_client.MobileElement;

public class TestRegister extends BaseActivity{
	
		Logger log = Logger.getLogger(TestRegister.class.getName());
		public static Method method[];
		//识别关键字
		public static String inspector;
		//数据
		public static String data;
		//操作
		public static String actionstep;
		//定义类
		public static Action action;
		public static MobileElement mobileElement;
		public static boolean testResult;
		public static String filePath;
	
		
		@Test
		public void test_register_sucess() throws Exception{
			action = new Action(getDriver());
			//获得action类的所有公有方法
			method = action.getClass().getMethods();
			//定义excel文件路径
			String filePath = Constants.测试Excel文件.filepath;
			DataproviderFromExcel.getExcel(filePath);
			String fileSheet = Constants.测试调度文件.suite_sheet;
			//获取测试集合中测试用例的总数
			int testSuiteAllNum = DataproviderFromExcel.getAllRowNum(fileSheet);
			//循环测试调度文件
			for(int testSuiteNum = 1;testSuiteNum <= testSuiteAllNum;testSuiteNum++) {
				//获取测试用例名，直接关联待测试用例所在的sheet名
				String testCaseName = DataproviderFromExcel.getCellData
						(Constants.测试调度文件.suite_sheet,testSuiteNum,Constants.测试调度文件.suite_testCaseName).trim();
				//判断测试用例是否运行,用例中isRun是否打勾
				String isRun = DataproviderFromExcel.getCellData
						(Constants.测试调度文件.suite_sheet, testSuiteNum, Constants.测试调度文件.suite_isRun).trim();
				//获取测试用例详细的描述，只用来输出日志
				String testCaseDetail = DataproviderFromExcel.getCellData
						(Constants.测试调度文件.suite_sheet, testSuiteNum, Constants.测试调度文件.suite_testCaseDetail).trim();
				//如果isRun的值为“√”，则执行指定sheet页的测试步骤，sheet名与testCaseName相同
				if(isRun.equals("√")) {
					//测试执行结果默认为失败
					log.info("运行测试用例：测试用例名称："+testCaseName+";测试用例详细描述："+testCaseDetail);
					testResult = true;
					int testCaseAllNum = DataproviderFromExcel.getAllRowNum(testCaseName);
					log.info("测试步骤数：" + testCaseAllNum);
					for(int testCaseNum = 1 ;testCaseNum <= testCaseAllNum;testCaseNum++) {
						//获取识别方式(控件)
						inspector = DataproviderFromExcel.getCellData
								(testCaseName, testCaseNum, Constants.测试用例文件.Col_inspector).trim();
						//获取操作数据
						data = DataproviderFromExcel.getCellData
								(testCaseName, testCaseNum, Constants.测试用例文件.Col_data).trim();
						//获取操作方式
						actionstep = DataproviderFromExcel.getCellData
								(testCaseName, testCaseNum, Constants.测试用例文件.Col_actionStep);
						//识别元素
						mobileElement = null;
						if(!inspector.isEmpty()) {
							mobileElement = FindElement.findElement(driver, inspector);
						}
						log.info("执行测试步骤：识别方式：" + inspector + ";操作:" + actionstep + ";测试数据：" + data);
						//执行操作
						execute_Actions(testCaseNum,testCaseName);
						
						if(testResult == false) {
							log.info("测试用例执行结果为false");
							DataproviderFromExcel.setCellData
							(testSuiteNum, Constants.测试调度文件.suite_result, true, fileSheet, filePath);
							Assert.fail("测试步骤有失败，整个测试用例执行失败");
							break;
						}
						if(testResult == true) {
							log.info("测试用例执行结果为true");
							DataproviderFromExcel.setCellData
							(testSuiteAllNum, Constants.测试调度文件.suite_result, false, fileSheet, filePath);
							Assert.assertTrue(true,"测试用例执行成功");
						}
					}
				}
			}
		}
	
		public void execute_Actions(int testcaseNum,String testCaseName)throws Exception{
			
				for(int i=0;i<method.length;i++) {
					if(method[i].getName().equals(actionstep)) {
						method[i].invoke(action, mobileElement,data);
						if(testResult == true) {
							log.info("测试步骤执行结果为true");
							DataproviderFromExcel.setCellData
							(testcaseNum, Constants.测试用例文件.Col_result, true, testCaseName, Constants.测试Excel文件.filepath);
							break;
						}
						else {
							log.info("测试步骤执行结果为false");
							DataproviderFromExcel.setCellData
							(testcaseNum, Constants.测试用例文件.Col_result, false, testCaseName, Constants.测试Excel文件.filepath);
						}
					}
				}
			
		}


}