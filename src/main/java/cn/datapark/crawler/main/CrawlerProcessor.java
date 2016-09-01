package cn.datapark.crawler.main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class CrawlerProcessor implements PageProcessor {

	private static Logger log = Logger.getLogger(CrawlerProcessor.class);
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

	private static String url = "http://sclub.jd.com/productpage/p-1190367252-s-0-t-3-p-0.html";
	private static String tmURL = "https://rate.tmall.com/list_detail_rate.htm?order=3&append=0&content=1&itemId=523999488662&sellerId=197232874&currentPage=1";

	private static int pageNum = 10;

	static {
		PropertyConfigurator.configure("./config/log4j.properties");
	}

	public Site getSite() {
		return site;
	}

	public void process(Page page) {
		// processTmall(page);
		// processJD(page);
		// processTmallHome(page);
		// processNanzhuang(page);
		processTmallCategory(page);
	}

	/**
	 * 京东商品评价爬取（动态替换itemId，url中p-后面的数字串）
	 * 
	 * @param page
	 */
	private static void processJD(Page page) {
		boolean isFirst = true;
		StringBuilder sb = new StringBuilder(url);
		page.addTargetRequest(url);
		String comment = page.getJson().toString().trim();
		if (isFirst) {
			List<String> urls = new ArrayList<String>();
			JSONObject data = JSONObject.fromObject(comment);
			JSONObject productCommentSummary = data.getJSONObject("productCommentSummary");
			long commentCount = productCommentSummary.getLong("commentCount");
			long pageCount = commentCount / 10;
			for (int i = 0; i < pageCount; i++) {
				int start = sb.lastIndexOf("-");
				int end = sb.lastIndexOf(".");
				sb.replace(start + 1, end, String.valueOf(i));
				urls.add(sb.toString());
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}
		System.out.println(comment);
	}

	/**
	 * 天猫商品评价爬取（需要动态替换参数itemId及sellerId）
	 * 
	 * @param page
	 */
	private static void processTmall(Page page) {
		boolean isFirst = true;
		List<String> urls = new ArrayList<String>();
		page.addTargetRequest(tmURL);
		String string = page.getJson().toString().trim();
		StringBuilder comment = new StringBuilder(string);
		comment.insert(0, "{").append("}");
		if (isFirst) {
			JSONObject data = JSONObject.fromObject(comment.toString());
			JSONObject rateDetail = data.getJSONObject("rateDetail");
			JSONObject paginator = rateDetail.getJSONObject("paginator");
			pageNum = paginator.getInt("lastPage");
			StringBuilder sb1 = new StringBuilder(tmURL);
			for (int i = 1; i <= pageNum; i++) {
				sb1.replace(tmURL.lastIndexOf("=") + 1, sb1.length(), String.valueOf(i));
				urls.add(sb1.toString());
			}
			page.addTargetRequests(urls);
			isFirst = false;
		}
		System.out.println(comment.toString());
		persistentToFile(comment.toString());
	}

	public static void processTmallHome(Page page) {
		List<String> list = page.getHtml().xpath("//*[@id=\"content\"]/div[2]/div[1]/div[3]/div/ul/li/a/@href").all();
		List<String> all = page.getHtml().xpath("//*[@id=\"content\"]/div[2]/div[1]/div[3]/div/ul/li/a/text()").all();
		for (int i = 0; i < all.size(); i++) {
			String category = all.get(i);
			String url = list.get(i).replaceAll("//www.tmall.com/", "");
			System.out.println(category + ":" + url);
		}
	}
	
	public static void processNanzhuang(Page page) {
		List<String> urls = page.getHtml().xpath("//*[@id=\"J_TmFushiNavCate\"]/ul/li/ul/li/a/@href").all();
		List<String> categorys = page.getHtml().xpath("//*[@id=\"J_TmFushiNavCate\"]/ul/li/ul/li/a/text()").all();
		for (int i = 0; i < categorys.size(); i++) {
			String category = categorys.get(i);
			String url = urls.get(i).replaceAll("//nanzhuang.tmall.com/", "");
			System.out.println(category + ":" + url);
		}
	}
	
	
	public static void processTmallCategory(Page page){
		Selectable selectable = page.getHtml().css("div.productImg-wrap");
		if(selectable != null){
			List<String> list = selectable.links().all();
			for(String url : list){
				System.out.println(url);
			}
		}
	}
	
	

	/**
	 * 持久化到磁盘
	 * 
	 * @param content
	 */
	private static void persistentToFile(String content) {
		try {
			File file = new File("d:\\1.txt");
			FileWriter writer = new FileWriter(file, true);
			writer.write(content + "\n");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Spider spider = Spider.create(new CrawlerProcessor())
		//.addUrl(tmURL).thread(1);
		// .addUrl("https://nanzhuang.tmall.com/?acm=lb-zebra-148799-667863.1003.8.708026&scm=1003.8.lb-zebra-148799-667863.ITEM_14561677576501_708026").thread(1);
		.addUrl("https://list.tmall.com/search_product.htm?cat=50105599").thread(1);
		spider.run();
	}

}
