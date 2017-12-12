package com.ppomppu.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.ppomppu.model.Selector;

@Transactional
@Repository
public class SelectorRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<Selector> findAllSelector(){
		return em.createQuery("from Selector s join fetch s.links", Selector.class).getResultList();
	}
	
	public Selector findByWebsite(Integer id){
		return em.find(Selector.class, id);
	}
	
	public List<Selector> findAllWebsites(){
		return em.createQuery("from Selector s", Selector.class).getResultList();
	}
	
	public void saveSelector(Selector selector){
		em.persist(selector);
	}
	
	public void updateSelector(Selector selector){
		em.merge(selector);
	}
	
	public void removeSelector(Integer id){
		em.remove(this.findByWebsite(id));
	}
	
	@Modifying
	public void updateScrapStatus(Integer id, String status){
		em.createQuery("update Selector s set s.scrapStatus=:status where s.id=:id")
			.setParameter("id", id)
			.setParameter("status", status)
			.executeUpdate();
	}
	
	/*
	public void removeUpdateSelector(Selector selector){
		Selector s = em.find(Selector.class, selector.getId());
		s.setLinks(null);
		s.setLinks(selector.getLinks());
	}
	*/
	
	
}
