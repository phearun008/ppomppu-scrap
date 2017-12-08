package com.ppomppu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppomppu.model.Selector;
import com.ppomppu.repository.LinkRepository;
import com.ppomppu.repository.SelectorRepository;

@Controller
public class HomeController {

	@Autowired
	private SelectorRepository selectorRepository;
	
	@Autowired
	private LinkRepository linkRepository;
	

	@RequestMapping({ "/", "home" })
	public String home(Model model) {
		model.addAttribute("websites", selectorRepository.findAllWebsites());
		return "index";
	}

	@GetMapping("/website/list")
	public String listWebsitePage(Model model) {
		model.addAttribute("websites", selectorRepository.findAllWebsites());
		return "website";
	}

	@GetMapping("/website/add")
	public String addWebsitePage(Model model, Selector selector) {
		model.addAttribute("selector", selector);
		model.addAttribute("addStatus", true);
		return "add-website";
	}

	@GetMapping("/website/edit")
	public String editWebsitePage(Model model, @RequestParam Integer id) {
		model.addAttribute("selector", selectorRepository.findByWebsite(id));
		model.addAttribute("addStatus", false);
		return "add-website";
	}

	@PostMapping("/website/update")
	public String updateWebsite(Model model, Selector selector) {
		System.out.println(selector);
		
		//remove links before update
		linkRepository.removeLinkBySelectorId(selector.getId());
		
		selector.setLinks(selector.getLinks());
		selectorRepository.updateSelector(selector);
		
		return "redirect:/website/list";
	}
	
	@PostMapping("/website/create")
	public String addWebsite(Selector selector, Model model) {
		System.out.println(selector);
		System.out.println(selector.getLinks().size());
		selector.getLinks().forEach(System.out::println);

		selector.setLinks(selector.getLinks());
		selectorRepository.saveSelector(selector);
		return "redirect:/website/list";
	}

}
