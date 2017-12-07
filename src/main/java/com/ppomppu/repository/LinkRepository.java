package com.ppomppu.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.ppomppu.model.Link;

@Transactional
@Repository
public class LinkRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public void updateLink(Link link){
		em.merge(link);
	}
	
	@Modifying
	public void removeLinkBySelectorId(Integer id){
		em.createQuery("delete from Link l where l.selector.id=:id").setParameter("id", id).executeUpdate();
	}
	
}
