package com.ppomppu.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ppomppu.model.Article;
import com.ppomppu.service.ScrapService;

@RestController
public class ScrapController {

	@Autowired
	private ScrapService scrapService;
	
	@GetMapping("/scrap/websites")
	public List<Article> scrapByWebsite(@RequestParam("websiteId") Integer websiteId) throws IOException{
		System.out.println("Website: " + websiteId);
		if(websiteId == 0 || websiteId == null)
			return scrapService.scrap();
		else
			return scrapService.scrapByWebsite(websiteId);
	}
	
	
}
