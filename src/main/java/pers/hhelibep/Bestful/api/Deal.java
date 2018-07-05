package pers.hhelibep.Bestful.api;

import org.apache.http.HttpEntity;

import pers.hhelibep.Bestful.core.*;
import pers.hhelibep.Bestful.core.annotations.IntegerAttributeLimitation;
import pers.hhelibep.Bestful.core.annotations.Meta;
import pers.hhelibep.Bestful.http.IResponse;
import pers.hhelibep.Bestful.util.Properties;

public class Deal implements IApi {
    private static final String API_PATH = "/path/to/api";
    // this is mandatory, you must set the root path for this api
    private static final String ROOT_PATH = Properties.getValue("defaultHost") + API_PATH;

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

    @Meta(method = HttpMethod.POST)
    public IResponse createDeal(HttpEntity entity) {
        return null;
    }

    @Meta(method = HttpMethod.DELETE, path = "/{id}")
    public IResponse deleteDealById(long id) {
        return null;
    }

    @Meta(method = HttpMethod.PUT, path = "/partial_update")
    public IResponse updateDeal(HttpEntity entity) {
        return null;
    }

    @Override
    public String getRootPath() {
        return ROOT_PATH;
    }
}
