package pers.hhelibep.Bestful.cases;

import java.util.ArrayList;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import pers.hhelibep.Bestful.BaseTest;
import pers.hhelibep.Bestful.api.Deal;
import pers.hhelibep.Bestful.core.ApiProvider;
import pers.hhelibep.Bestful.data.DealDataProvider;
import pers.hhelibep.Bestful.http.BasicResponse;
import pers.hhelibep.Bestful.http.HttpHelper;
import pers.hhelibep.Bestful.http.IResponse;

// examples of api's CRUD 
public class DealTest extends BaseTest {
    // Deal deal = ApiProvider.getApi(Deal.class); //if using single thread, no need
    // to get Api instance in each method
    @Test(groups = "deal.test", dataProvider = "createDealDataProvider", dataProviderClass = DealDataProvider.class)
    public void testCreateDealExpect401(JSONObject body) {
        // set headers for next createDeal() method
        setTemporaryHeaders(new ArrayList<>());
        // get api instance
        Deal deal = ApiProvider.getApi(Deal.class);

        Assert.assertTrue(401 == deal.createDeal(HttpHelper.parseStringToEntity(body.toString())).getResponseStatusCode()); // returns 401 if header is invalid
    }

    @Test(groups = "deal.test", dataProvider = "createDealDataProvider", dataProviderClass = DealDataProvider.class)
    public void testCreateDealExpect200(JSONObject body) {
        Deal deal = ApiProvider.getApi(Deal.class);
        Assert.assertTrue(200 == deal.createDeal(HttpHelper.parseStringToEntity(body.toString())).getResponseStatusCode());// returns 200 if creation succeeds
    }

    @Test(groups = "deal.test", dataProvider = "getDealId", dataProviderClass = DealDataProvider.class)
    public void testGetDeal(long id, boolean isValid) {
        Deal deal = ApiProvider.getApi(Deal.class);
        BasicResponse response = (BasicResponse) deal.getDealById(id);
        if (isValid) { // decide if this is a positive case
            Assert.assertTrue(200 == response.getResponseStatusCode());
            Assert.assertEquals(response.getResponseByClass(JSONObject.class).getLong("id"), id,
                    response.getResponseByClass(JSONObject.class).toString());
        } else {
            Assert.assertTrue(204 == response.getResponseStatusCode());// no id mapped, returns 204 or something else
            logger.info(response.getResponseByClass(JSONObject.class).toString());// debug
        }
    }

    @Test(groups = "deal.test", dataProvider = "updateDealDataProvider", dataProviderClass = DealDataProvider.class)
    public void testPartialUpdateDeal(JSONObject body, boolean isValid) {
        Deal deal = ApiProvider.getApi(Deal.class);
        IResponse response = deal.updateDeal(HttpHelper.parseStringToEntity(body.toString()));
        if (isValid) {
            Assert.assertTrue(200 == response.getResponseStatusCode()); // update succeedes and returns 200
        } else {
            Assert.assertTrue(404 == response.getResponseStatusCode());// id is invalid and returns 404
            logger.info(response.getResponseByClass(JSONObject.class).toString());// debug
        }
    }

    @Test(groups = "deal.test", dataProvider = "deleteDealDataProvider", dataProviderClass = DealDataProvider.class)
    public void testDeleteDeal(long id, boolean isValid) {
        Deal deal = ApiProvider.getApi(Deal.class);
        IResponse response = deal.deleteDealById(id);

        if (isValid) {
            // before delete, cache the deal that will be deleted
            JSONObject dealWillBeDeleted = deal.getDealById(id).getResponseByClass(JSONObject.class);
            Assert.assertTrue(response.getResponseStatusCode() == 200, response.getResponseStatusCode() + "");// returns
                                                                                                              // 200
                                                                                                              // if
                                                                                                              // delete
                                                                                                              // succeeds
            // after delete, restore the deal
            logger.info("{}", deal.createDeal(HttpHelper.parseStringToEntity(dealWillBeDeleted.toString())).getResponseStatusCode());
        } else {
            Assert.assertTrue(response.getResponseStatusCode() == 404, response.getResponseStatusCode() + "");// returns
                                                                                                              // 404
                                                                                                              // if id
                                                                                                              // is
                                                                                                              // invalid
        }

    }
}
