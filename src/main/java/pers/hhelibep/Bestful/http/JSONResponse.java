package pers.hhelibep.Bestful.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONResponse implements IResponse {
	private int statusCode = -1;
	private String statusReason = "";
	private JSONObject responseJSONObject = null;
	private JSONArray responseJSONArray = null;
	private HttpEntity entity = null;

	private JSONResponse(HttpResponse response) {
		StatusLine statusLine = response.getStatusLine();
		this.statusCode = statusLine.getStatusCode();
		this.statusReason = statusLine.getReasonPhrase();
		HttpEntity entity = response.getEntity();
		this.entity = entity;
		if (entity == null) {
			logger.error("Response Entity is Null!!");
			responseJSONObject = new JSONObject();
		} else {
			String contentString = null;
			try {
				contentString = EntityUtils.toString(entity);
				if (contentString.startsWith("{")) {
					responseJSONObject = new JSONObject(contentString);
				} else {
					if (contentString.startsWith("[")) {
						responseJSONArray = new JSONArray(contentString);
					} else {
						responseJSONObject = new JSONObject().put("responseString", contentString);
					}
				}

			} catch (IOException e1) {
				logger.error("Error while transform response to String!");
				e1.printStackTrace();
			} catch (JSONException e) {
				logger.error("JSON Formattiong Error!Unable to format response content to JSON");
				logger.error(contentString);
				responseJSONObject = new JSONObject("{\"Error\" : \"The response is null\"}");
			}
		}
	}

	@Override
	public int getResponseStatusCode() {
		return this.statusCode;
	}

	@Override
	public String getReason() {
		return this.statusReason;
	}

	@Override
	public HttpEntity getResponseEntity() {
		return entity;
	}

	public JSONObject getResponseAsJSONObject() {
		return this.responseJSONObject;
	}

	public JSONArray getResponseAsJSONArray() {
		return this.responseJSONArray;
	}

	public static JSONResponse fromResponse(HttpResponse response) {
		if (null == response) {
			return null;
		}
		return new JSONResponse(response);
	}

}
