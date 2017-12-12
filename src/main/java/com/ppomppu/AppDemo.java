package com.ppomppu;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AppDemo {
	
	public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String mainUrl = "http://cafe.naver.com/ArticleList.nhn?search.clubid=15240504&search.menuid=62&search.boardtype=L&search.questionTab=A&search.totalCount=151&search.page=3";
		//get all pagination
		Document mainDoc = Jsoup.connect(mainUrl).timeout(10_000).userAgent(USER_AGENT).get();
		
		String previousUrl = "";
		Elements elements = mainDoc.select("table.Nnavi a");
		
		System.out.println("Emements: " + elements);
		
		int page = 0;
		do {
			
			if (previousUrl.contains(elements.get(page).attr("href"))) {
				System.out.println("Again!!!");
				break;
			}
			previousUrl += elements.get(page).attr("href");
			
			//get next page url
			System.out.println("Scrap Url: " + elements.get(page).attr("href"));
			
			
			String eachPageUrl = elements.get(page).attr("href");
			
			Thread.sleep(new Random().nextInt(3)*1_000);
			
			Document pageDoc = Jsoup.connect("http://cafe.naver.com" + eachPageUrl).timeout(10_000).userAgent(USER_AGENT).get();;
			Elements elementsPage = pageDoc.select("form[name=ArticleList] tr[align=center]");
			
			for (Element element : elementsPage) {
				
				Elements e = element.select("td:nth-child(5)");
				System.out.println("E: "  +e);
				
				//Elements e = element.select("em.u_cnt");
				//System.out.println(e);
				
				//System.out.println(element);
				
				/*String title = element.select("a.icon_txt_n, a.icon_pic_n").text();
				String url = element.select("a.icon_txt_n, a.icon_pic_n").attr("href");
				String id = element.select("td.t_notice").text();
				String date = element.select("td.t_date").text();
				String writer = element.select("span.user_nick_nm").text();
				String view = element.select("td.t_hits").first().text();
				System.out.println(String.format("%s, %s, %s, %s, %s, %s", id, title, url, date, writer, view));*/
				
				//scrap content
				//pageDoc = Jsoup.connect("http://gall.dcinside.com" + url).get();
				//String content = pageDoc.select("div.s_write p").text();
			}

			if (page == elements.size() - 1) {
				mainDoc = Jsoup.connect("http://cafe.naver.com" + elements.get(page).attr("href")).timeout(10_000).userAgent(USER_AGENT).get();;
				elements = mainDoc.select("table.Nnavi a");
				page = 1;
			}

			if(page==1) break;

			page++;
			
		} while (true);
	}
}
