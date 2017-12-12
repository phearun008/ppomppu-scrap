package com.ppomppu.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "board_list")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "board_id")
	@JsonProperty("board_id")
	private Integer id;
	
	@Column(name = "board_num")
	@JsonProperty("board_num")
	private String aId;
	
	@Column(name = "board_title")
	@JsonProperty("board_title")
	private String title;
	
	@Column(name = "board_writer")
	private String writer;
	
	@Column(name = "board_view")
	private String view;
	
	@Column(name = "board_like")
	private String like;
	
	@Column(name = "board_publish_date")
	private String publishedDate;
	
	@Column(name = "board_url")
	@JsonProperty("board_url")
	private String url;
	
	@Column(name = "board_content")
	@JsonProperty("board_content")
	private String content;
	
	@CreatedDate
	@Column(name = "created_date")
	private Date createdDate;
	
	@LastModifiedDate
	@Column(name = "modified_date")
	private Date modifiedDate;

}
