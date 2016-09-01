package cn.datapark.crawler.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class PropertiesUtil {

	private static Properties properties;
	private static Map<String, String> propertyMap = new HashMap<String, String>();
	private static Map<String, String> patternMap = new HashMap<String, String>();
	private static List<String[]> proxyList = new ArrayList<String[]>();

	static {
		properties = new Properties();
	}

	public static Map<String, String> getPropertyMap() {
		// File file = new File("./SpiderConfigFiles/config.properties");
		// try {
		// InputStream is = new FileInputStream(file);
		// properties.load(is);
		// propertyMap.put(Config.REDIS_SERVER,
		// properties.getProperty(Config.REDIS_SERVER));
		// propertyMap.put(Config.KAFKA_TOPIC,
		// properties.getProperty(Config.KAFKA_TOPIC));
		// propertyMap.put(Config.THREAD_SIZE,
		// properties.getProperty(Config.THREAD_SIZE));
		// propertyMap.put(Config.DRIVER_CAN_USE_TIME,
		// properties.getProperty(Config.DRIVER_CAN_USE_TIME));
		// propertyMap.put(Config.KAFKA_BROKER,
		// properties.getProperty(Config.KAFKA_BROKER));
		// propertyMap.put(Config.PROXY, properties.getProperty(Config.PROXY));
		// propertyMap.put(Config.CONTENT_LENGTH,
		// properties.getProperty(Config.CONTENT_LENGTH));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return propertyMap;

		File file = new File("./SpiderConfigFiles/config.properties");

		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String[] pro = lineTxt.trim().split("@");
				propertyMap.put(pro[0], pro[1]);
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propertyMap;

	}

	public static Map<String, String> getPatternMap(String patternFilePath) {
		Map<String, String> patternMap = new HashMap<String, String>();
		File file = new File(patternFilePath);
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// log.error("file not found Exception: filepath=" +
			// patternFilePath);
		}

		InputStreamReader is = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(is);
		String conf = "";
		try {
			conf = br.readLine();
			while (conf != null && !"".equals(conf)) {
				String key = "";
				String value = "";
				String[] temp = conf.split("\\$");
				if (temp.length == 2) {
					key = temp[0];
					value = temp[1];
					patternMap.put(key, value);
				}
				conf = br.readLine();
			}
		} catch (IOException e) {
			// log.error("IOException,when read from file " + patternFilePath);
		}

		return patternMap;
		// if (patternFilePath != null) {
		// File file = new File(patternFilePath);
		// try {
		// InputStream is = new FileInputStream(file);
		// properties.load(is);
		// patternMap.put(Config.Pattern.ENTRANCE_PATTERN,
		// properties.getProperty(Config.Pattern.ENTRANCE_PATTERN));
		// patternMap.put(Config.Pattern.LIST_PATTERN,
		// properties.getProperty(Config.Pattern.LIST_PATTERN));
		// patternMap.put(Config.Pattern.ITEM_PATTERN,
		// properties.getProperty(Config.Pattern.ITEM_PATTERN));
		// patternMap.put(Config.Pattern.COMMENTS_PATTERN,
		// properties.getProperty(Config.Pattern.COMMENTS_PATTERN));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// return patternMap;
	}

	public static List<String[]> getProxyList() {
		File file = new File("./SpiderConfigFiles/SSLPrivateProxy-MyProxyList.txt");
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String[] pro = lineTxt.trim().split(":");

				String[] proxy = new String[] { pro[0], pro[1] };
				proxyList.add(proxy);
			}
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return proxyList;
	}

	public static String getProxyRandom() {
		int max = 50;
		int min = 0;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		if (proxyList.size() == 0) {
			getProxyList();
		}
		return proxyList.get(s)[0] + ":" + proxyList.get(s)[1];
	}

	public static void main(String[] args) {
		getProxyRandom();
	}

}