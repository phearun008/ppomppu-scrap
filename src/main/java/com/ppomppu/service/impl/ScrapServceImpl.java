package com.ppomppu.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppomppu.model.Article;
import com.ppomppu.model.Link;
import com.ppomppu.model.Selector;
import com.ppomppu.repository.SelectorRepository;
import com.ppomppu.service.ScrapService;

@Service
public class ScrapServceImpl implements ScrapService{
	
	public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private SelectorRepository selectorRepository;
	
	@Override
	public List<Article> scrap() throws IOException {
		List<Selector> selectors = selectorRepository.findAllSelector();
		try {
			return this.scrapMsanbuWithPagination(selectors);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	//TODO: 
	/*private List<Article> scrapMsanbu(List<Selector> selectors) throws IOException{
		List<Article> fList = new ArrayList<>();
		for(Selector selector: selectors){
			for(Link link: selector.getLinks()){
				logger.info("Scrap link: " + link.getHref());
				fList.addAll(scrapMsanbu(selector, link.getHref()));				
			}
		}
		return fList;
	}*/
	
	//TODO: scrap from MSANBU
	/*private List<Article> scrapMsanbu(Selector selector, String mainUrl) throws IOException{
		
		List<Article> articles = new ArrayList<>();
		
		Document doc = Jsoup.connect(mainUrl).get();
		Elements elements = doc.select(selector.getRow());
		
		for (Element element : elements) {
			String title = element.select(selector.getTitle()).first().text();
			String url = element.select(selector.getUrl()).first().attr(selector.getUrlAttribute());
			String id = element.select(selector.getAId()).first().text();
			String date = element.select(selector.getPublishedDate()).first().text();
			String writer = element.select(selector.getWriter()).first().text();
			
			//scrap content
			doc = Jsoup.connect(selector.getPrefixUrl() + url).get();
			String content = doc.select(selector.getContent()).text();
			logger.info(content);
			
			//set article content
			Article article = new Article();
			article.setTitle(title);
			article.setUrl(selector.getPrefixUrl() + url);
			article.setAId(id);
			article.setPublishedDate(date);
			article.setContent(content);
			article.setWriter(writer);
			
			articles.add(article);
		}
		return articles;
	}*/
	
	//TODO: 
	private List<Article> scrapMsanbuWithPagination(List<Selector> selectors) throws IOException, InterruptedException{
		List<Article> fList = new ArrayList<>();
		for(Selector selector: selectors){
			for(Link link: selector.getLinks()){
				logger.info("Scrap link: " + link.getHref());
				fList.addAll(scrapMsanbuWithPaging(selector, link.getHref()));				
			}
		}
		return fList;
	}
		
	//TODO: scrap from MSANBU
	private List<Article> scrapMsanbuWithPaging(Selector selector, String mainUrl) throws IOException, InterruptedException{
		
		List<Article> articles = new ArrayList<>();
		
		//get all pagination
		Document mainDoc = Jsoup.connect(mainUrl).timeout(10_000).userAgent(USER_AGENT).get();;
		
		String previousUrl = "";
		Elements elements = mainDoc.select(selector.getPaging());
		int i = 0;
		do {
			if (previousUrl.contains(elements.get(i).attr("href"))) {
				System.out.println("Again!!!");
				break;
			}
			previousUrl += elements.get(i).attr("href");
			
			//get next page url
			logger.info("Scrap Url: "+ elements.get(i).attr("href"));
			String eachPageUrl = elements.get(i).attr("href");
			
			Thread.sleep(new Random().nextInt(3)*1_000);
			
			Document pageDoc = Jsoup.connect(selector.getPrefixUrl() + eachPageUrl).timeout(10_000).userAgent(USER_AGENT).get();;
			Elements elementsPage = pageDoc.select(selector.getRow());
			
			for (Element element : elementsPage) {
				String title = element.select(selector.getTitle()).first().text();
				String url = element.select(selector.getUrl()).first().attr(selector.getUrlAttribute());
				String id = element.select(selector.getAId()).first().text();
				String date = element.select(selector.getPublishedDate()).first().text();
				String writer = element.select(selector.getWriter()).first().text();
				
				//scrap content
				pageDoc = Jsoup.connect(selector.getPrefixUrl() + url).get();
				String content = pageDoc.select(selector.getContent()).text();
				logger.info(content);
				
				//set article content
				Article article = new Article();
				article.setTitle(title);
				article.setUrl(selector.getPrefixUrl() + url);
				article.setAId(id);
				article.setPublishedDate(date);
				article.setContent(content);
				article.setWriter(writer);
				
				articles.add(article);
			}

			if (i == elements.size() - 1) {
				mainDoc = Jsoup.connect(selector.getPagingPrefixUrl() + elements.get(i).attr("href")).get();
				elements = mainDoc.select(selector.getPaging());
				i = 1;
			}
			i++;
		} while (true);
		
		return articles;
	}

	@Override
	public List<Article> scrapByWebsite(Integer id) throws IOException {
		List<Article> articles = new ArrayList<>();
		Selector selector = selectorRepository.findByWebsite(id);
		for(Link link: selector.getLinks()){
			try {
				articles.addAll(this.scrapMsanbuWithPaging(selector, link.getHref()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return articles;
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println(new Random().nextInt(3)*1000);
		
		
		/*String url = "http://cafe.naver.com/ArticleList.nhn?search.clubid=15240504&search.menuid=62&search.boardtype=L&search.questionTab=A&search.totalCount=151&search.page=3";
		Document doc = Jsoup.connect(url).timeout(3000).userAgent(USER_AGENT).get();
		
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
		} while (true);*/
		
		/*Elements elements = doc.select("form[name=ArticleList] tr[align=center]");
		
		System.out.println(elements.size());
		for (Element element : elements) {
			
			
			String title = element.select("span.aaa a").first().text();
			String url = element.select("span.aaa a").first().attr("href");
			String id = element.select(".list-count").first().text();
			String date = element.select("td.view-count").first().text();
			String like = element.select("em.u_cnt").first().text();
			String writer = element.select("span.wordbreak").first().text();
			System.out.println(writer);*/
			
			
			//String e = element.select("td.view-count").text();
			//System.out.println(e);
			
			//scrap content
			//doc = Jsoup.connect(prefixUrl + url).get();
//			String content = doc.select("div.tbody p").text();
			//System.out.println("C:" + content);
		//}
	}
}
