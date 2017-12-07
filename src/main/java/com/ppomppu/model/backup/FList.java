package com.ppomppu.model.backup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ppomppu_list")
@Data
public class FList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "board_id")
	private Integer id;
	
	@Column(name = "board_num")
	private Integer num;
	
	@Column(name = "board_title")
	private String title;
	
	@Column(name = "board_writer")
	private String writer;
	
	@Column(name = "board_view")
	private Integer view;
	
	@Column(name = "board_recommend")
	private Integer recommend;
	
	@Column(name = "board_url")
	private String url;
	
	@Column(name = "category")
	private Integer category;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "publish_date")
	private Date publishedDate;
	
	@OneToOne
	@JoinColumn(name = "content_id")
	private FContent content;
}
