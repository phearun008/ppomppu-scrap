package com.ppomppu.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "website_selector")
public class Selector {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String websiteName;
	private String websiteUrl;
	
	//List
	private String row;
	private String aId;
	private String title;
	private String writer;
	private String publishedDate;
	private String url;
	private String thumbnail;
	private String urlAttribute = "href";
	
	private String viewCount;
	private String likeCount;
	
	private String paging;
	private String pagingPrefixUrl;
	
	private String prefixUrl;
	
	@Column(name="scrap_status", length = 1)
	private String scrapStatus = "0";
	
	//Content
	private String content;
	
	@OneToMany(mappedBy = "selector",  cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Link> links = new ArrayList<>();

	public void setLinks(List<Link> links){
		this.links = new ArrayList<>();
		links.forEach(link->{
			if(link!=null){
				this.links.add(link);
				link.setSelector(this);
			}
		});
	}

	@Override
	public String toString() {
		return "Selector [id=" + id + ", websiteName=" + websiteName + ", websiteUrl=" + websiteUrl + ", row=" + row
				+ ", aId=" + aId + ", title=" + title + ", writer=" + writer + ", publishedDate=" + publishedDate
				+ ", viewCount=" + viewCount + ", url=" + url + ", thumbnail=" + thumbnail + ", urlAttribute=" + urlAttribute
				+ ", prefixUrl=" + prefixUrl + ", content=" + content + "]";
	}
	
}
