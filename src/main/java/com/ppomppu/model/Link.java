package com.ppomppu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="website_url")
public class Link {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "href")
	private String href;
	
	@ManyToOne
	@JoinColumn(name = "selector_id")
	private Selector selector;

	@Override
	public String toString() {
		return "Link [id=" + id + ", href=" + href + "]";
	}
	
}
