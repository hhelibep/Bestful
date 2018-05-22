package pers.hhelibep.Bestful.http;

import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IResponse {
	Logger logger = LoggerFactory.getLogger(IResponse.class);

	int getResponseStatusCode();

	String getReason();

	HttpEntity getResponseEntity();

}
