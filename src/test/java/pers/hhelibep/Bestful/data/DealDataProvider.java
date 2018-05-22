package pers.hhelibep.Bestful.data;

import org.json.JSONObject;
import org.testng.annotations.DataProvider;

public class DealDataProvider {

	@DataProvider(name = "createDealDataProvider")
	public static Object[][] getCreateDealData() {
		// temporary, should restore in file
		return new Object[][] { { new JSONObject(
				"{\"name\": \"John Doe\",\"id\": 12345,\"score\": 99}") } };
	}

	@DataProvider(name = "dealIdDataProvider")
	public static Object[][] getDealId() {
		// temporary, should restore in file
		return new Object[][] { { 12345l, true }, { 98765l, false } };
	}

	@DataProvider(name = "updateDealDataProvider")
	public static Object[][] getUpdateDealData() {
		// temporary, should restore in file
		return new Object[][] { { new JSONObject("{\"id\":12345,\"score\":98}"), true },
				{ new JSONObject("{\"id\":2,\"score\":999}"), false } };
	}

	@DataProvider(name = "deleteDealDataProvider")
	public static Object[][] deleteDealData() {
		// temporary, should restore in file
		return new Object[][] { { 123l, false }, { 12345l, true } };
	}
}
