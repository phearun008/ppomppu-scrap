package com.ppomppu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ppomppu.repository.SelectorRepository;

import java.io.IOException;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PpomppuApplicationTests {

	public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
	@Autowired
	SelectorRepository repo;
	
	@Test
	public void contextLoads() throws IOException {
		System.out.println(new Random().nextInt(3)*1000);

		String url = "http://cafe.naver.com/ArticleList.nhn?search.clubid=15240504&search.menuid=62&search.boardtype=L&search.questionTab=A&search.totalCount=151&search.page=3";
		Document doc = Jsoup.connect(url).timeout(3000).userAgent(USER_AGENT).get();
		/*
		String previousUrl = "";
		Elements elements = doc.select("table.Nnavi a");
		int i = 0;
		do {
			if (previousUrl.contains(elements.get(i).attr("href"))) {
				System.out.println("again");
				break;
			}
			previousUrl += elements.get(i).attr("href");
			Thread.sleep(1000);
			System.out.println(elements.get(i).attr("href"));

			if (i == elements.size() - 1) {
				//doc = Jsoup.connect("http://cafe.naver.com" + elements.get(i).attr("href")).get();
				doc = Jsoup.connect("http://cafe.naver.com/ArticleList.nhn?search.clubid=15240504&search.menuid=62&search.boardtype=L&search.questionTab=A&search.totalCount=13540&search.page=901").get();
				elements = doc.select("table.Nnavi a");
				i = 1;
			}
			i++;
		} while (true);
		*/
		Elements elements = doc.select("form[name=ArticleList] tr[align=center]");

		System.out.println(elements.size());
		for (Element element : elements) {

			String title = element.select("span.aaa a").first().text();
			String subUrl = element.select("span.aaa a").first().attr("href");
			String id = element.select(".list-count").first().text();
			String date = element.select("td.view-count").first().text();
			String like = element.select("em.u_cnt").first().text();
			String writer = element.select("span.wordbreak").first().text();
			System.out.println(title);
			System.out.println(subUrl);
			System.out.println(id);
			System.out.println(date);
			System.out.println(writer);
			System.out.println(like);

			//String e = element.select("td.view-count").text();
			//System.out.println(e);
			//scrap content
			//doc = Jsoup.connect(prefixUrl + url).get();
			//String content = doc.select("div.tbody p").text();
			//System.out.println("C:" + content);
			//
		}
			//[How to get selector]: f12->selectorElement->rightClick->copySelector
	}

}
