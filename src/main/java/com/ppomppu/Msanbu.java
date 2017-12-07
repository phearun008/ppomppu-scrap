package com.ppomppu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Msanbu {
  public static void main(String[] args) throws Exception{
	//임신출산용품 후기
	  Document doc = Jsoup.connect("http://cafe.naver.com/ArticleList.nhn?search.clubid=15240504&search.menuid=62&search.boardtype=L&search.questionTab=A&search.totalCount=151&search.page=3").get();
	 // System.out.println(doc);
	  
	 Elements links = doc.select("form[name=ArticleList] tr[align=center]");
	  for(int i=4;i<links.size();i++){
		  
		  String id=(links.get(i).select("span").first().text());
		  String title=links.get(i).select(".aaa").text();
		  String author=links.get(i).select(".wordbreak").text();
		  Elements count=links.get(i).select(".view-count");
		  String time=count.get(0).text();
		  String search=count.get(1).text();
		  String like=count.get(2).text();
		  
		  System.out.println(id);
		  System.out.println(title);
		  System.out.println(author);
		  System.out.println(time);
		  System.out.println(search);
		  System.out.println(like);
		  
		  String url="http://cafe.naver.com"+links.get(i).select(".aaa a").first().attr("href");
		  
		  //article detail
		  Document doc_detail = Jsoup.connect(url).get();
		  
		  Elements article=doc_detail.select("#post_"+id);
		  String date=article.select(".date").text();
		  String text=(article.select("#tbody").text());
		  String html=(article.select("#tbody").html());
		  String comment_json = Jsoup.connect("http://cafe.naver.com/CommentView.nhn?search.clubid=15240504&search.menuid=62&search.articleid="+id+"&search.lastpageview=true&lcs=Y").get().select("body").text();
		  
		  System.out.println(date);
		  System.out.println(text);
		  System.out.println(html);
		  
		  System.out.println(comment_json);
		  
		  
		  /*JSONObject jsonObj = new JSONObject(comment_json);
		  JSONObject jsonObj1 =jsonObj.getJSONObject("result");
		  JSONArray commentlist=jsonObj1.getJSONArray("list");
		  for(int k=0;k<commentlist.length();k++){
			  System.out.println(commentlist.get(k));
		  }
		  break;*/
	  }
  }
  
}

