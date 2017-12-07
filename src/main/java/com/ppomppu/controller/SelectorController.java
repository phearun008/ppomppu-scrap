package com.ppomppu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ppomppu.model.Selector;

@RestController
public class SelectorController {

	@PostMapping("/selector")
	public ResponseEntity<?> saveSelector(@RequestBody Selector selector){
		System.out.println(selector);
		return ResponseEntity.ok(true);
	}
	
}
