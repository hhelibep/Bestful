package pers.hhelibep.Bestful.http;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Preconditions;

public class BasicResponse implements IResponse {
    private int statusCode = -1;
    private String statusReason = "";
    private HttpEntity entity;
    private HttpResponse response;
    private String rawString;
    private File file;

    private BasicResponse(HttpResponse response) {
        Preconditions.checkNotNull(response, "response could not be Null!");
        this.response = response;
        StatusLine statusLine = response.getStatusLine();
        this.statusCode = statusLine.getStatusCode();
        this.statusReason = statusLine.getReasonPhrase();
        this.entity = response.getEntity();

        if (entity == null) {
            logger.warn("Response Entity is Null!!");
        }
    }

    public static BasicResponse fromResponse(HttpResponse response) {
        if (null == response) {
            return null;
        }
        return new BasicResponse(response);
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

    /**
     * @param clazz
     *            which contains one constructor that only need JSON String as param
     * @return instance of this clazz
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getResponseByClass(Class<T> clazz) {
        if (null == rawString) {
            try {
                rawString = EntityUtils.toString(this.entity);
            } catch (Exception e) {
                logger.error("unable to convert response to String or Response is already consumed:{}", e);
                return null;
            }
        }
        try {
            Constructor<T> cons = clazz.getConstructor(String.class);
            T instance = cons.newInstance(rawString);
            return instance;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpResponse getRawResponse() {
        return response;
    }

    @Override
    public File saveResponseAsFile(File file) {
        if (null != this.file) {
            return this.file;
        }
        if (null != this.rawString && !this.entity.isRepeatable()) {
            logger.error("entity is consumed and not repeatable");
            return null;
        }
        String filePath = file.getAbsolutePath();
        if (!file.exists()) {
            try {
                String folderPath = filePath.substring(0, filePath.lastIndexOf('/'));
                File folderFile = new File(folderPath);
                folderFile.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        try (InputStream inputStream = this.entity.getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                OutputStream outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);) {
            String line = bufferedReader.readLine();
            while (null != line) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        this.file = file;
        return file;
    }

}
