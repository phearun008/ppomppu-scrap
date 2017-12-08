package com.ppomppu.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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
	public Future<String> scrapByWebsite(@RequestParam("id") Integer id) throws InterruptedException{
		CompletableFuture<String> future = new CompletableFuture<>();
		//future.complete("not actually in the background");
		
		System.out.println("Scraping Id: " + id);
		selectorRepository.updateScrapStatus(id, "1");
		Thread.sleep(5_000);
		System.out.println("Scraping Done!");
		
		return future;
	}
}
