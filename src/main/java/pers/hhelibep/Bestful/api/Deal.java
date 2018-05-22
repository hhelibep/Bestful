package pers.hhelibep.Bestful.api;

import org.json.JSONObject;

import pers.hhelibep.Bestful.core.ApiMethodInterceptor;
import pers.hhelibep.Bestful.core.HttpMethod;
import pers.hhelibep.Bestful.core.Meta;
import pers.hhelibep.Bestful.http.IResponse;
import pers.hhelibep.Bestful.util.Properties;

public class Deal implements IApi {
	// this is mandatory, you must set the root path for this api
	private static final String ROOT_PATH = Properties.getValue("defaultHost") + "/path/to/api";

	/**
	 * this is a standard request definition. what you need to provide:
	 * 
	 * @Meta :method and path is mandatory, variables in url should be surrounded
	 *       with {}
	 * @param size
	 *            notice it's as exact same as size
	 * @param cursor
	 *            notice it's as exact same as size
	 * @return response of this api
	 * 
	 * @see ApiMethodInterceptor for more details
	 */
	@Meta(method = HttpMethod.GET, path = "?page_size={size}&cursor={cursor}")
	public IResponse getDealList(int size, String cursor) {
		return null;
	}

	@Meta(method = HttpMethod.GET, path = "/{id}")
	public IResponse getDealById(long id) {
		return null;
	}

	@Meta(method = HttpMethod.POST, path = "")
	public IResponse createDeal(JSONObject body) {
		return null;
	}

	@Meta(method = HttpMethod.DELETE, path = "/{id}")
	public IResponse deleteDealById(long id) {
		return null;
	}

	@Meta(method = HttpMethod.PUT, path = "/partial_update")
	public IResponse updateDeal(JSONObject body) {
		return null;
	}

	@Override
	public String getRootPath() {
		return ROOT_PATH;
	}
}
