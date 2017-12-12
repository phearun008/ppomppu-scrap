package com.ppomppu.service;

import java.io.IOException;
import java.util.List;

import com.ppomppu.model.Board;

public interface ScrapService {
	
	public List<Board> scrap(boolean isTest) throws IOException;
	public List<Board> scrapByWebsite(Integer id, boolean isTest) throws IOException;
	
	//public List<Board> oneTimeScrapWithPagination();
	
}
