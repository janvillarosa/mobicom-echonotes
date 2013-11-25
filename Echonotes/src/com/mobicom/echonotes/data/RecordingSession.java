package com.mobicom.echonotes.data;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

public class RecordingSession {
	
	private ArrayList<String> listOfTextAnnotations;
	private ArrayList<String> listOfPicturePathAnnotations;
	private ArrayList<Long> annotationCounter;
	private String recordingFilePath;
	private String name;
	private Date dateModified;
	private String category;
	
	
	public RecordingSession() {
		listOfTextAnnotations = new ArrayList();
		listOfPicturePathAnnotations = new ArrayList();
		annotationCounter = new ArrayList();
		
	}


	public ArrayList<String> getListOfTextAnnotations() {
		return listOfTextAnnotations;
	}


	public void setListOfTextAnnotations(ArrayList<String> listOfTextAnnotations) {
		this.listOfTextAnnotations = listOfTextAnnotations;
	}


	public ArrayList<String> getListOfPicturePathAnnotations() {
		return listOfPicturePathAnnotations;
	}


	public void setListOfPicturePathAnnotations(
			ArrayList<String> listOfPicturePathAnnotations) {
		this.listOfPicturePathAnnotations = listOfPicturePathAnnotations;
	}


	public String getRecordingFilePath() {
		return recordingFilePath;
	}


	public void setRecordingFilePath(String recordingFilePath) {
		this.recordingFilePath = recordingFilePath;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDateModified() {
		return dateModified;
	}


	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}


	public String getCategory() {
		return category;
	}

	public ArrayList<Long> getAnnotationCounter() {
		return annotationCounter;
	}


	public void setAnnotationCounter(ArrayList<Long> annotationCounter) {
		this.annotationCounter = annotationCounter;
	}


	public void setCategory(String category) {
		this.category = category;
	}
	
	public void writeMetadata(){
		//MIKEE'S CODE
	}

}
