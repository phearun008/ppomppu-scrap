package com.ppomppu.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.ppomppu.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ppomppu.model.Selector;
import com.ppomppu.repository.SelectorRepository;

@RestController
public class SelectorController {

	@Autowired
	private ScrapService scrapService;

	@PostMapping("/selector")
	public ResponseEntity<?> saveSelector(@RequestBody Selector selector){
		System.out.println(selector);
		return ResponseEntity.ok(true);
	}
	
	@Autowired
	SelectorRepository selectorRepository;
	
	@DeleteMapping("/selector")
	public ResponseEntity<?> removeSelector(@RequestParam("id") Integer id){
		System.out.println("Removing Id: " + id);
		selectorRepository.removeSelector(id);
		return ResponseEntity.ok(true);
	}
	
	@Async
	@PostMapping("/selector/scrap")
	public Future<String> scrapByWebsite(@RequestParam("id") Integer id) throws InterruptedException, IOException {
		CompletableFuture<String> future = new CompletableFuture<>();
		//future.complete("not actually in the background");
		
		System.out.println("Scraping Id: " + id);

		//update scrap status to scraping!
		selectorRepository.updateScrapStatus(id, "1");
		//begin scraping
		scrapService.scrap(false);
		System.out.println("=> Scraping Done!");
		//update scrap status to done!
		selectorRepository.updateScrapStatus(id, "2");

		return future;
	}
}
