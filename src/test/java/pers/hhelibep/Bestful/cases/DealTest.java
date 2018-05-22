package pers.hhelibep.Bestful.cases;

import java.util.ArrayList;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import pers.hhelibep.Bestful.BaseTest;
import pers.hhelibep.Bestful.api.Deal;
import pers.hhelibep.Bestful.core.ApiProvider;
import pers.hhelibep.Bestful.data.DealDataProvider;
import pers.hhelibep.Bestful.http.IResponse;
import pers.hhelibep.Bestful.http.JSONResponse;

// examples of api's CRUD 
public class DealTest extends BaseTest {
//	Deal deal = ApiProvider.getApi(Deal.class); //if using single thread, no need to get Api instance in each method
	@Test(groups = "deal.test", dataProvider = "createDealDataProvider", dataProviderClass = DealDataProvider.class)
	public void testCreateDealExpect401(JSONObject body) {
		// set headers for next createDeal() method
		setTemporaryHeaders(new ArrayList<>());
		// get api instance
		Deal deal = ApiProvider.getApi(Deal.class);
		Assert.assertTrue(401 == deal.createDeal(body).getResponseStatusCode()); //returns 401 if header is invalid
	}

	@Test(groups = "deal.test", dataProvider = "createDealDataProvider", dataProviderClass = DealDataProvider.class)
	public void testCreateDealExpect200(JSONObject body) {
		Deal deal = ApiProvider.getApi(Deal.class);
		Assert.assertTrue(200 == deal.createDeal(body).getResponseStatusCode());//returns 200 if creation succeeds
	}

	@Test(groups = "deal.test", dataProvider = "getDealId", dataProviderClass = DealDataProvider.class)
	public void testGetDeal(long id, boolean isValid) {
		Deal deal = ApiProvider.getApi(Deal.class);
		JSONResponse response = (JSONResponse) deal.getDealById(id);
		if (isValid) { // decide if this is a positive case
			Assert.assertTrue(200 == response.getResponseStatusCode());
			Assert.assertEquals(response.getResponseAsJSONObject().getLong("id"), id,
					response.getResponseAsJSONObject().toString());
		} else {
			Assert.assertTrue(204 == response.getResponseStatusCode());//no id mapped, returns 204 or something else
			logger.info(response.getResponseAsJSONObject().toString());// debug
		}
	}

	@Test(groups = "deal.test", dataProvider = "updateDealDataProvider", dataProviderClass = DealDataProvider.class)
	public void testPartialUpdateDeal(JSONObject body, boolean isValid) {
		Deal deal = ApiProvider.getApi(Deal.class);
		JSONResponse response = (JSONResponse) deal.updateDeal(body);
		if (isValid) {
			Assert.assertTrue(200 == response.getResponseStatusCode()); // update succeedes and returns 200
		} else {
			Assert.assertTrue(404 == response.getResponseStatusCode());// id is invalid and returns 404
			logger.info(response.getResponseAsJSONObject().toString());// debug
		}
	}

	@Test(groups = "deal.test", dataProvider = "deleteDealDataProvider", dataProviderClass = DealDataProvider.class)
	public void testDeleteDeal(long id, boolean isValid) {
		Deal deal = ApiProvider.getApi(Deal.class);
		IResponse response = deal.deleteDealById(id);

		if (isValid) {
			// before delete, cache the deal that will be deleted
			JSONObject dealWillBeDeleted = ((JSONResponse) deal.getDealById(id)).getResponseAsJSONObject();
			Assert.assertTrue(response.getResponseStatusCode() == 200, response.getResponseStatusCode() + "");//returns 200 if delete succeeds
			// after delete, restore the deal
			logger.info("{}", deal.createDeal(dealWillBeDeleted).getResponseStatusCode());
		} else {
			Assert.assertTrue(response.getResponseStatusCode() == 404, response.getResponseStatusCode() + "");//returns 404 if id is invalid
		}

	}

}
