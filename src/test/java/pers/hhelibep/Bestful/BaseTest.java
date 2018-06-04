package pers.hhelibep.Bestful;

import java.util.List;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.xml.XmlTest;

import pers.hhelibep.Bestful.core.ApiMethodInterceptor;

public class BaseTest {
	protected Logger logger = LoggerFactory.getLogger(BaseTest.class);

	@BeforeSuite
	public void beforeSuite(ITestContext context) {
		XmlTest xmlTest = new XmlTest();
		// context.getSuite().getXmlSuite().addTest(xmlTest);
		// add retry analyzer for each test method
		for (ITestNGMethod method : context.getAllTestMethods()) {
			// method.setRetryAnalyzer(new
			// RetryTest(Integer.parseInt(Properties.getValue("retryCount"))));
		}
	}

	/**
	 * perpetual headers mean will impact all methods in current thread
	 * 
	 * @param headers
	 */
	protected void setPerpetualHeaders(List<Header> headers) {
		ApiMethodInterceptor.setPerpetualHeaders(headers);
	}

	/**
	 * temporary headers mean only impact next one method
	 * 
	 * @param headers
	 */
	protected void setTemporaryHeaders(List<Header> headers) {
		ApiMethodInterceptor.setTemporaryHeaders(headers);
	}

	protected List<Header> getPerpetualHeaders() {
		return ApiMethodInterceptor.getPerpetualHeaders();
	}
}
