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

import com.ppomppu.model.Board;
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
	public List<Board> scrap(boolean isTest) throws IOException {
		List<Selector> selectors = selectorRepository.findAllSelector();
		try {
			return this.scrapMsanbuWithPagination(selectors, isTest);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	//TODO: Scrap recently updated articles
	private List<Board> recentUpdateScrap(Selector selector, String mainUrl) throws IOException{
		
		List<Board> articles = new ArrayList<>();
		
		Document doc = Jsoup.connect(mainUrl).timeout(10_000).userAgent(USER_AGENT).get();
		Elements elements = doc.select(selector.getRow());
		
		for (Element element : elements) {
			String title = element.select(selector.getTitle()).text();
			String url = element.select(selector.getUrl()).attr(selector.getUrlAttribute());
			String id = element.select(selector.getAId()).text();
			String date = element.select(selector.getPublishedDate()).text();
			String writer = element.select(selector.getWriter()).text();
			String viewCount = element.select(selector.getViewCount()).text();
			String like = element.select(selector.getLikeCount()).text();
			
			//TODO: Scrap content
			doc = Jsoup.connect(selector.getPrefixUrl() + url).get();
			String content = doc.select(selector.getContent()).text();
			logger.info(content);
			
			//TODO: Set article's field
			Board article = new Board();
			article.setTitle(title);
			article.setUrl(selector.getPrefixUrl() + url);
			article.setAId(id);
			article.setPublishedDate(date);
			article.setContent(content);
			article.setWriter(writer);
			article.setView(viewCount);
			article.setLike(like);
			
			//TODO:
			articles.add(article);
		}
		return articles;
	}
	
	//TODO: 
	private List<Board> scrapMsanbuWithPagination(List<Selector> selectors, boolean isTest) throws IOException, InterruptedException{
		List<Board> fList = new ArrayList<>();
		for(Selector selector: selectors){
			for(Link link: selector.getLinks()){
				logger.info("Scrap link: " + link.getHref());
				fList.addAll(scrapMsanbuWithPaging(selector, link.getHref(), isTest));
			}
		}
		return fList;
	}
		
	//TODO: scrap from MSANBU
	private List<Board> scrapMsanbuWithPaging(Selector selector, String mainUrl, boolean isTest) throws IOException, InterruptedException{
		
		List<Board> articles = new ArrayList<>();
		
		//get all pagination
		Document mainDoc = Jsoup.connect(mainUrl).timeout(10_000).userAgent(USER_AGENT).get();
		
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
			logger.info("Scrap Url: "+ selector.getPagingPrefixUrl() + elements.get(page).attr("href"));
			String eachPageUrl = elements.get(page).attr("href");
			
			Thread.sleep(new Random().nextInt(3)*1_000);
			
			Document pageDoc = Jsoup.connect(selector.getPrefixUrl() + eachPageUrl).timeout(10_000).userAgent(USER_AGENT).get();;
			Elements elementsPage = pageDoc.select(selector.getRow());
			
			for (Element element : elementsPage) {

				String title = element.select(selector.getTitle()).text();
				String url = element.select(selector.getUrl()).attr(selector.getUrlAttribute());
				String id = element.select(selector.getAId()).text();
				String date = element.select(selector.getPublishedDate()).text();
				String writer = element.select(selector.getWriter()).text();
				String viewCount = element.select(selector.getViewCount()).text();
				String likeCount = element.select(selector.getLikeCount()).text();
				
				//TODO: Scrap content
				pageDoc = Jsoup.connect(selector.getPrefixUrl() + url).get();
				String content = pageDoc.select(selector.getContent()).text();
				logger.info(content);
				
				//TODO: Set article content
				Board article = new Board();
				article.setTitle(title);
				article.setUrl(selector.getPrefixUrl() + url);
				article.setAId(id);
				article.setPublishedDate(date);
				article.setContent(content);
				article.setWriter(writer);
				article.setView(viewCount);
				article.setLike(likeCount);
				
				articles.add(article);
			}
			if (page == elements.size() - 1) {
				mainDoc = Jsoup.connect(selector.getPagingPrefixUrl() + elements.get(page).attr("href")).get();
				elements = mainDoc.select(selector.getPaging());
				page = 1;
			}

			if(page==1 && isTest) break;

			page++;
			
		} while (true);
		
		return articles;
	}

	@Override
	public List<Board> scrapByWebsite(Integer id, boolean isTest) throws IOException {
		List<Board> articles = new ArrayList<>();
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

	//TODO: Scrap all data, only one time scrap.
	private void oneTimeScrapWithPagination(Selector selector, String mainUrl) throws IOException, InterruptedException{
		
		List<Board> articles = new ArrayList<>();
		
		//TODO: Get all pagination
		Document mainDoc = Jsoup.connect(mainUrl).timeout(10_000).userAgent(USER_AGENT).get();
		
		String previousUrl = "";
		Elements elements = mainDoc.select(selector.getPaging());
		
		int page = 0;
		do {
			if (previousUrl.contains(elements.get(page).attr("href"))) 
				break; 
			previousUrl += elements.get(page).attr("href");
			
			//TODO: Get next page url
			logger.info("Scraping Url: "+ selector.getPagingPrefixUrl() + elements.get(page).attr("href"));
			String eachPageUrl = elements.get(page).attr("href");
			
			Thread.sleep(new Random().nextInt(3)*1_000);
			
			Document pageDoc = Jsoup.connect(selector.getPrefixUrl() + eachPageUrl).timeout(10_000).userAgent(USER_AGENT).get();
			Elements elementsPage = pageDoc.select(selector.getRow());
			
			for (Element element : elementsPage) {
				
				//TODO:
				String title = element.select(selector.getTitle()).text();
				String url = element.select(selector.getUrl()).attr(selector.getUrlAttribute());
				String id = element.select(selector.getAId()).text();
				String date = element.select(selector.getPublishedDate()).text();
				String writer = element.select(selector.getWriter()).text();
				String viewCount = element.select(selector.getViewCount()).text();
				String likeCount = element.select(selector.getLikeCount()).text();
				
				//TODO: Scrap content
				pageDoc = Jsoup.connect(selector.getPrefixUrl() + url).get();
				String content = pageDoc.select(selector.getContent()).text();
				logger.info(content);
				
				//TODO: Set article content
				Board article = new Board();
				article.setTitle(title);
				article.setUrl(selector.getPrefixUrl() + url);
				article.setAId(id);
				article.setPublishedDate(date);
				article.setWriter(writer);
				article.setView(viewCount);
				article.setLike(likeCount);
				article.setContent(content);
				
				//TODO: 
				articles.add(article);
			}
			//TODO:
			if (page == elements.size() - 1) {
				mainDoc = Jsoup.connect(selector.getPagingPrefixUrl() + elements.get(page).attr("href")).get();
				elements = mainDoc.select(selector.getPaging());
				page = 1;
			}
			page++;
		} while (true);
	}
}
