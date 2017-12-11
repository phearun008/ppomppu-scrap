package com.ppomppu.service;

import java.io.IOException;
import java.util.List;

import com.ppomppu.model.Article;

public interface ScrapService {
	
	public List<Article> scrap(boolean isTest) throws IOException;
	public List<Article> scrapByWebsite(Integer id, boolean isTest) throws IOException;
	
}
