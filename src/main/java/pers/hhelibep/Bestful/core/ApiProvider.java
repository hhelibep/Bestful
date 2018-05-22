package pers.hhelibep.Bestful.core;

import java.util.HashMap;

import net.sf.cglib.proxy.Enhancer;
import pers.hhelibep.Bestful.api.IApi;

public class ApiProvider {
	private static final ThreadLocal<HashMap<String, IApi>> API_CONTAINER_LOCAL = new ThreadLocal<>();

	/**
	 * 
	 * @param class
	 *            of the api you need
	 * @return instance of this api class with proxy in current thread
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IApi> T getApi(Class<T> clz) {
		HashMap<String, IApi> apiMap = API_CONTAINER_LOCAL.get();
		if (null == apiMap) {
			apiMap = new HashMap<>();
			API_CONTAINER_LOCAL.set(apiMap);
		}
		apiMap = API_CONTAINER_LOCAL.get();
		IApi api = apiMap.get(clz.getName());
		if (null != api) {
			return (T) api;
		} else {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(clz);
			enhancer.setCallback(new ApiMethodInterceptor());
			T api2 = null;
			try {
				api2 = (T) enhancer.create();
			} catch (IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
			apiMap.put(clz.getName(), api2);
			return (T) api2;
		}
	}
}
