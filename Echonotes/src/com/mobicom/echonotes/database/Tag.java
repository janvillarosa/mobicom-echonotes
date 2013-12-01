package com.mobicom.echonotes.database;

public class Tag {

	long tagId;
	String tagName;
	
	public Tag(){
		
	}
	
	public Tag(String tagName){
		this.tagName = tagName;
	}
	
	public Tag(long id, String tagName){
		this.tagId = id;
		this.tagName = tagName;
	}
	
	public void setId(long tagId){
		this.tagId = tagId;
	}
	
	public void setTagName(String tagName){
		this.tagName = tagName;
	}
	
	public long getTagId(){
		return this.tagId;
	}
	
	public String getTagName(){
		return this.tagName;
	}
	
}

