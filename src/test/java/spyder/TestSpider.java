package spyder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestSpider {

	@Test
	public void testJD() {
		String url = "http://sclub.jd.com/productpage/p-1856585-s-0-t-3-p-10.html";
		StringBuilder sb = new StringBuilder();
		String prefix = url.substring(0, url.indexOf("-") + 1);
		String middle = (String) url.subSequence(url.indexOf("-s"), url.lastIndexOf("-") + 1);

		sb.append(prefix);
		sb.append(1856585);
		sb.append(middle);
		sb.append(10);
		sb.append(".html");

		System.out.println(sb.toString());

		int start = sb.lastIndexOf("-");
		int end = sb.lastIndexOf(".");
		sb.replace(start + 1, end, "11");

		System.out.println(sb.toString());

	}

	@Test
	public void testTMall() {

		String json = "Shop.Setup({name:\"zhangsan\",age:23});";
		String data = (String) json.subSequence(json.indexOf("(") + 1, json.lastIndexOf(")"));
		System.out.println(data);

		String url = "https://rate.tmall.com/list_detail_rate.htm?order=3&append=0&content=1&itemId=523999488662&sellerId=197232874&currentPage=1";

		StringBuilder sb = new StringBuilder(url);
		for (int i = 1; i < 21; i++) {
			sb.replace(url.lastIndexOf("=") + 1, sb.length(), String.valueOf(i));
			System.out.println(sb.toString());
		}
		// System.out.println(url);
		// System.out.println(sb.toString());

	}

	@Test
	public void testReplace() {
		String detail = "https://detail.tmall.com/item.htm?id=536786163817&skuId=3205063938245&cat_id=50105579&rn=b3e6735ac2f3a09bc08a4152cf4adef8&user_id=2855912144&is_b=1";
		String url = "https://rate.tmall.com/list_detail_rate.htm?order=3&append=0&content=1&itemId={id}&sellerId={user_id}&currentPage=1";

		Matcher matcher = Pattern.compile("\\{\\w+\\}").matcher(url);
		while (matcher.find()) {
			String group = matcher.group();
			Matcher matcher2 = Pattern.compile("\\w+").matcher(group);
			if (matcher2.find()) {
				String group2 = matcher2.group();
				Matcher detailMatcher = Pattern.compile(group2 + "=\\d+").matcher(detail);
				if(detailMatcher.find()){
					String param = detailMatcher.group();
					System.out.println(param);
				}
				// System.out.println(group2);
			}
			// System.out.println(group);
		}
		// String after = url.replace("{itemId}", "55551").replace("{sellerId}",
		// "895415651").replace("{pageNum}", "2314454");
		// System.out.println(url);
		// System.out.println(after);
	}

	@Test
	public void testReg() {
		String url = "https://rate.tmall.com/list_detail_rate.htm?order=3&append=0&content=1&itemId=5613215123023&sellerId=23156311&currentPage={pageNum}";

		Matcher matcher = Pattern.compile("sellerId=\\d+").matcher(url);
		if (matcher.find()) {
			String group = matcher.group();
			String group0 = matcher.group(0);

			System.out.println(group);
			System.out.println(group0);
		}

	}

	@Test
	public void testPage() {
		String url = "http://sclub.jd.com/productpage/p-1856585-s-0-t-3-p-1.html";
		String tmurl = "https://rate.tmall.com/list_detail_rate.htm?order=3&append=0&content=1&itemId=5613215123023&sellerId=23156311&currentPage=1";
		Pattern pattern = Pattern.compile("p-1.html|currentPage=1");

		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			System.out.println("第一页");
		} else {
			System.out.println("非首页");
		}
	}

	@Test
	public void testJDReg() {
		String url = "http://sclub.jd.com/productpage/p-1308307399-s-0-t-3-p-99.html";
		String reg = "http://sclub.jd.com/productpage/p-\\d+-s-\\d+-t-\\d+-p-\\d+.html";
		Matcher matcher = Pattern.compile(reg).matcher(url);

		if (matcher.find()) {
			System.out.println("匹配");
		}
	}

	@Test
	public void testdd() {
		String SUNING = "http://review.suning.com/ajax/review_lists/style-000000000{skuid}-{categoryid}-total-{pageNum}-default-{commentCount}-----reviewList.htm?callback=reviewList";

		String reg = "http://review.suning.com/ajax/review_lists/style-";

		Matcher matcher = Pattern.compile(reg).matcher(SUNING);
		if (matcher.find()) {
			System.out.println("匹配....");
		}
	}

	@Test
	public void testBoolean() {
		Boolean valueOf = Boolean.valueOf("aa");
		System.out.println(valueOf);
	}

}
