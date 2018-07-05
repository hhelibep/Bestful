package pers.hhelibep.Bestful.http;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IResponse {
    Logger logger = LoggerFactory.getLogger(IResponse.class);

    int getResponseStatusCode();

    String getReason();

    HttpResponse getRawResponse();

    HttpEntity getResponseEntity();

    <T> T getResponseByClass(Class<T> clazz);

    File saveResponseAsFile(File file);
}
