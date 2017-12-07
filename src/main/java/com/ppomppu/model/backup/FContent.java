package com.ppomppu.model.backup;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ppomppu_content")
@Data
public class FContent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "coment_id")
	private Integer id;
	
	@Column(name = "content_text")
	private String content;
	
	@OneToMany(mappedBy = "content")
	private List<FComment> comments;
	
}
