package com.ppomppu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "website_article")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "a_id")
	private Integer id;
	
	@Column(name = "a_no")
	private String aId;
	
	@Column(name = "a_title")
	private String title;
	
	@Column(name = "a_writer")
	private String writer;
	
	@Column(name = "a_view")
	private String view;
	
	@Column(name = "a_like")
	private String like;
	
	@Column(name = "a_publish_date")
	private String publishedDate;
	
	@Column(name = "a_url")
	private String url;
	
	@Column(name = "a_content")
	private String content;

}
