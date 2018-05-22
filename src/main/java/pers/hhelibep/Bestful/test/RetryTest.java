package pers.hhelibep.Bestful.test;

import java.util.Set;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryTest implements IRetryAnalyzer {
	private int maxRetryTimes = 0;
	private int currentTryTimes = 1;

	public RetryTest(int maxRetryTimes) {
		this.maxRetryTimes = maxRetryTimes;
	}

	@Override
	public boolean retry(ITestResult result) {
		if (!result.isSuccess()) {
			Set<String> names = result.getAttributeNames();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("(");
			for (String name : names) {
				stringBuffer.append(result.getAttribute(name));
			}
			stringBuffer.append(")");
			System.out.println(result.getMethod().getMethodName() + stringBuffer.toString()
					+ " get failed and the reason is \"" + result.getThrowable().getMessage() + "\".");

			if (currentTryTimes <= maxRetryTimes) {
				System.out.println("Method \"" + result.getMethod().getMethodName()
						+ "\" is failed , start to retry, has run " + currentTryTimes + " times");
				currentTryTimes++;
				return true;
			}
		}
		return false;
	}
}