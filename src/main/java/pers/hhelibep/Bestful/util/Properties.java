package pers.hhelibep.Bestful.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Properties {
	private static final String CONF_FILE_NAME = "conf.properties";
	private static ResourceBundle testProperties = getProperties(
			FileHelper.findFileInCurrentProject(CONF_FILE_NAME).getAbsolutePath());
	private static Logger logger = LoggerFactory.getLogger(Properties.class);

	/**
	 * this method will check System.getProperty(key) first, if it's not null,will
	 * return directly which means you can replace value in conf.properties by
	 * -D{key}=value in maven command or System.setProperty(key,value)
	 * 
	 * @param key
	 * @return value of the key or null if key doesn't exist
	 */
	public static String getValue(String key) {
		String sysValue = System.getProperty(key);
		if (null != sysValue) {
			return sysValue;
		}
		try {
			return testProperties.getString(key);
		} catch (MissingResourceException e) {
			logger.error("There is no such key {} in property files", key);
		}
		return null;
	}

	/**
	 * this method will check System.getProperty(key) first, if it's not null,will
	 * return directly which means you can replace value in conf.properties by
	 * -D{key}=value in maven command or System.setProperty(key,value)
	 * 
	 * @param key
	 * @param defaultValue
	 *            return this value if key doesn't exist
	 * @return value of the key or defaultValue
	 */
	public static String getValue(String key, String defaultValue) {
		String value = getValue(key);
		if (null != value) {
			return value;
		}
		return defaultValue;
	}

	public static ArrayList<String> getHosts() {
		ArrayList<String> urlsList = new ArrayList<>();
		for (String key : testProperties.keySet()) {
			String value = testProperties.getString(key);
			if (value.startsWith("https")) {
				urlsList.add(value);
			}
		}
		return urlsList;
	}

	private static ResourceBundle getProperties(String location) {
		PropertyResourceBundle bundle = null;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(location));
			bundle = new PropertyResourceBundle(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bundle;
	}

}
