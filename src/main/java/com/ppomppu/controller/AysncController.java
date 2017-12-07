package com.ppomppu.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AysncController {


	@ResponseBody @GetMapping("/rest/async")
	public Future<String> testingAsync() throws InterruptedException{
		CompletableFuture<String> future = new CompletableFuture<>();
		future.complete("not actually in the background");
		asyncMethod();
		
		return future;
	}
	
	@Async
	public void asyncMethod() throws InterruptedException{
		Thread.sleep(5000);
		System.out.println("Task finished!");
	}
	
}
