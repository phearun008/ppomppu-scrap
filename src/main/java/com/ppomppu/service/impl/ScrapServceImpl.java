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
	public List<Article> scrap(boolean isTest) throws IOException {
		List<Selector> selectors = selectorRepository.findAllSelector();
		try {
			return this.scrapMsanbuWithPagination(selectors, isTest);
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
	private List<Article> scrapMsanbu(Selector selector, String mainUrl) throws IOException{
		
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
	}
	
	//TODO: 
	private List<Article> scrapMsanbuWithPagination(List<Selector> selectors, boolean isTest) throws IOException, InterruptedException{
		List<Article> fList = new ArrayList<>();
		for(Selector selector: selectors){
			for(Link link: selector.getLinks()){
				logger.info("Scrap link: " + link.getHref());
				fList.addAll(scrapMsanbuWithPaging(selector, link.getHref(), isTest));
			}
		}
		return fList;
	}
		
	//TODO: scrap from MSANBU
	private List<Article> scrapMsanbuWithPaging(Selector selector, String mainUrl, boolean isTest) throws IOException, InterruptedException{
		
		List<Article> articles = new ArrayList<>();
		
		//get all pagination
		Document mainDoc = Jsoup.connect(mainUrl).timeout(10_000).userAgent(USER_AGENT).get();;
		
		String previousUrl = "";
		Elements elements = mainDoc.select(selector.getPaging());
		int page = 0;
		do {
			if (previousUrl.contains(elements.get(page).attr("href"))) {
				System.out.println("Again!!!");
				break;
			}
			previousUrl += elements.get(page).attr("href");
			
			//get next page url
			logger.info("Scrap Url: "+ elements.get(page).attr("href"));
			String eachPageUrl = elements.get(page).attr("href");
			
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

			if (page == elements.size() - 1) {
				mainDoc = Jsoup.connect(selector.getPagingPrefixUrl() + elements.get(page).attr("href")).get();
				elements = mainDoc.select(selector.getPaging());
				page = 1;
			}

			if(page==2 && isTest) break;

			page++;
		} while (true);
		
		return articles;
	}

	@Override
	public List<Article> scrapByWebsite(Integer id, boolean isTest) throws IOException {
		List<Article> articles = new ArrayList<>();
		Selector selector = selectorRepository.findByWebsite(id);
		for(Link link: selector.getLinks()){
			try {
				articles.addAll(this.scrapMsanbuWithPaging(selector, link.getHref(), isTest));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return articles;
	}

	public List<Article> scrapByWebsiteTest(Integer id) throws IOException {
		List<Article> articles = new ArrayList<>();
		Selector selector = selectorRepository.findByWebsite(id);
		for(Link link: selector.getLinks()){
			articles.addAll(this.scrapMsanbu(selector, link.getHref()));
		}
		return articles;
	}

	public static void main(String[] args) throws IOException {
		String url = "http://www.ppomppu.co.kr/zboard/zboard.php?id=phone";
		Document document = Jsoup.connect(url).timeout(100000).userAgent(USER_AGENT).get();

		Elements elements = document.select("tr.list0, tr.list1");
		for (Element element: elements){
			String num2 = element.select("td.list_vspace > font > font").text();
			System.out.println("n: " + num2);

		}

	}
}
