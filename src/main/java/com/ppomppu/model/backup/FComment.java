package com.ppomppu.model.backup;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "board_comment")
@Data
public class FComment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "comment_id")
	private Integer id;
	
	@Column(name = "comment_writer")
	private String writer;
	
	@Column(name = "comment_text")
	private String text;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	@ManyToOne
	@JoinColumn(name="reply_id")
	private FComment replyComment;
	
	@OneToMany(mappedBy = "replyComment")
	private List<FComment> replyComments;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "published_date")
	private Date publishedDate;
	
	@ManyToOne
	@JoinColumn(name = "board_num")
	private FContent content;
}
