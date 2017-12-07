package com.ppomppu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ppomppu.repository.SelectorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PpomppuApplicationTests {

	@Autowired
	SelectorRepository repo;
	
	@Test
	public void contextLoads() {
		repo.findAllSelector().forEach(s->{
			System.out.println("S->" + s);
			System.out.println("S->" + s.getLinks());
		});
	}

}
