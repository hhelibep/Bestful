package pers.hhelibep.Bestful.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import pers.hhelibep.Bestful.api.IApi;
import pers.hhelibep.Bestful.http.HttpHelper;
import pers.hhelibep.Bestful.http.IResponse;
import pers.hhelibep.Bestful.http.RequestExecutor;

public class ApiMethodInterceptor implements MethodInterceptor {
	private static Pattern pattern = Pattern.compile("\\{\\w+\\}");
	private static final String BODY = "body";
	private static final Logger logger = LoggerFactory.getLogger(ApiMethodInterceptor.class);
	private static final ThreadLocal<List<Header>> PERPETUAL_HEADERS = new ThreadLocal<>();
	private static final ThreadLocal<List<Header>> TEMPORARY_HEADERS = new ThreadLocal<>();

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		if (IApi.class.isAssignableFrom(obj.getClass())) {
			Meta meta = method.getAnnotation(Meta.class);
			String classFullName = obj.getClass().getName().split("\\$")[0];
			String[] parametersName = getMethodParametersName(Class.forName(classFullName), method);
			if (null != meta) {
				HttpMethod requestMethod = meta.method();
				String targetUrl = ((IApi) obj).getRootPath() + meta.path();
				Parameter[] parameters = method.getParameters();
				if (targetUrl.contains("{")) {
					Matcher matcher = pattern.matcher(targetUrl);
					while (matcher.find()) {
						String target = matcher.group(0);
						for (int i = 0; i < parameters.length; ++i) {
							String name = parametersName[i];
							if (("{" + name + "}").equals(target)) {
								targetUrl = targetUrl.replace(target, args[i].toString());
							}
						}
					}
				}
				HttpEntity entity = null;
				for (int i = 0; i < parameters.length; ++i) {
					Parameter p = parameters[i];
					if (parametersName[i].equals(BODY)) {
						if (JSONObject.class.isAssignableFrom(p.getType())) {
							entity = new StringEntity(args[i].toString());
							((StringEntity) entity).setContentType(ContentType.APPLICATION_JSON.getMimeType());
						}
						/*
						 * need to add more entity check in the future, like File
						 */
					}

				}
				List<Header> headers = null;
				if (null == PERPETUAL_HEADERS.get()) {
					ArrayList<Header> defaultHeaders = new ArrayList<>();
					defaultHeaders.add(HttpHelper.getAuthHeader(meta.authType()));
					PERPETUAL_HEADERS.set(defaultHeaders);
				}
				headers = PERPETUAL_HEADERS.get();
				if (null != TEMPORARY_HEADERS.get()) {
					headers = TEMPORARY_HEADERS.get();
				}
				IResponse response = null;
				switch (requestMethod) {
				case POST:
					response = RequestExecutor.post(targetUrl, entity, headers.toArray(new Header[headers.size()]));
					TEMPORARY_HEADERS.remove();
					return response;
				case PUT:
					response = RequestExecutor.put(targetUrl, entity, headers.toArray(new Header[headers.size()]));
					TEMPORARY_HEADERS.remove();
					return response;
				case GET:
					response = RequestExecutor.get(targetUrl, headers.toArray(new Header[headers.size()]));
					TEMPORARY_HEADERS.remove();
					return response;
				case DELETE:
					response = RequestExecutor.delete(targetUrl, headers.toArray(new Header[headers.size()]));
					TEMPORARY_HEADERS.remove();
					return response;
				default:
					break;
				}
			} else {
				return proxy.invokeSuper(obj, args);
			}
		} else {
			return proxy.invokeSuper(obj, args);
		}
		return null;
	}

	/**
	 * 
	 * @param clazz
	 *            target Class
	 * @param method
	 *            target Method
	 * @return parameters' name of this Method
	 */
	private String[] getMethodParametersName(Class<?> clazz, Method method) {
		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(clazz.getName());
			CtMethod cm = cc.getDeclaredMethod(method.getName());

			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			if (attr == null) {
				return new String[] {};
			}
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++)
				paramNames[i] = attr.variableName(i + pos);
			// paramNames即参数名
			return paramNames;

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return new String[] {};
	}

	public static void setPerpetualHeaders(List<Header> headers) {
		PERPETUAL_HEADERS.set(headers);
	}

	public static void setTemporaryHeaders(List<Header> headers) {
		TEMPORARY_HEADERS.set(headers);
	}

	public static List<Header> getPerpetualHeaders() {
		return PERPETUAL_HEADERS.get();
	}
}
