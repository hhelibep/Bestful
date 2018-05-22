package pers.hhelibep.Bestful.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.base.Preconditions;

public class RequestExecutor {

	private static final CloseableHttpClient CLIENT = HttpHelper.getClient();

	public static IResponse post(String url, HttpEntity postEntity, Header... headers) {
		Preconditions.checkNotNull(postEntity, "Entity should not be null");
		HttpPost post = new HttpPost(url);
		for (Header header : headers) {
			post.addHeader(header);
		}
		post.setEntity(postEntity);
		HttpResponse response = null;
		try {
			response = CLIENT.execute(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		post.releaseConnection();
		return JSONResponse.fromResponse(response);
	}

	public static IResponse put(String url, HttpEntity putEntity, Header... headers) {
		Preconditions.checkNotNull(putEntity, "Entity should not be null");
		HttpPut put = new HttpPut(url);
		for (Header header : headers) {
			put.addHeader(header);
		}
		put.setEntity(putEntity);
		HttpResponse response = null;
		try {
			response = CLIENT.execute(put);
		} catch (IOException e) {
			e.printStackTrace();
		}
		put.releaseConnection();
		return JSONResponse.fromResponse(response);
	}

	public static IResponse get(String url, Header... headers) {
		HttpGet get = new HttpGet(url);
		for (Header header : headers) {
			get.addHeader(header);
		}
		HttpResponse response = null;
		try {
			response = CLIENT.execute(get);
		} catch (IOException e) {
			e.printStackTrace();
		}
		get.releaseConnection();
		return JSONResponse.fromResponse(response);
	}

	public static IResponse delete(String url, Header... headers) {
		HttpDelete delete = new HttpDelete(url);
		for (Header header : headers) {
			delete.addHeader(header);
		}
		HttpResponse response = null;
		try {
			response = CLIENT.execute(delete);
		} catch (IOException e) {
			e.printStackTrace();
		}
		delete.releaseConnection();
		return JSONResponse.fromResponse(response);
	}

}
