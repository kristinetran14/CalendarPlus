package com.cse110.apk404.myCalendar;

import java.util.Calendar;


public class DynamicEvent implements CalendarEvent {

	private static final long serialVersionUID = 1L;

	private Long Id; 
	private String dateKey; //dateKey is the date of the event 12 Feb 2016
	private String name;  //name of the event
	private Calendar startTime; //startTime as a Calendar object
	private Calendar endTime; //endTime as a Calendar object
	private boolean isStatic; //if the event is static
	private boolean isFinished; //if the event is finished
	private String location = ""; //location of event
	private String description = ""; //description of event
	private String color = "";
	private Calendar deadline;
	private int estimatedLength; // estimated time to complete
	private int updatedlength;



	public int getUpdatedlength() {
		return updatedlength;
	}

	public void setUpdatedlength(int updatedlength) {
		this.updatedlength = updatedlength;
	}

	public DynamicEvent(String name, boolean isStatic, String location, String description, String color,
			Calendar deadline, int estimatedLength, boolean isFinished) throws CalendarError{

		setUpdatedlength(estimatedLength);
		setName(name);
		setStatic(isStatic);
		setStartTime(null);
		setEndTime(null);
		setLocation(location);
		setColor(color);
		setFinished(false);
		setDeadline(deadline);
		setDescription(description);
		setEstimatedLength(estimatedLength);
		setFinished(false);
	}

	public Calendar getDeadline() {	
		return deadline;
	}


	public int getEstimatedLength() {
		return estimatedLength;
	}

	public void setDeadline(Calendar deadline) {
		Calendar temp = Calendar.getInstance();
		if(deadline != null)
			temp.setTime(deadline.getTime());
		this.deadline = temp;
	}

	public void setEstimatedLength(int estimatedLength) {
		this.estimatedLength = estimatedLength;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isStatic() {
		return this.isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFinished() {
		return this.isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public long getId() {
		return Id;
	}

	public void setId(Long id) {
		this.Id = id;
	}

	public String getDateKey() {
		return dateKey;
	}

	public void setDateKey(String dateKey) {
		this.dateKey = dateKey;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		Calendar temp = Calendar.getInstance();
		if(startTime != null)
			temp.setTime(startTime.getTime());
		this.startTime = temp;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		Calendar temp = Calendar.getInstance();
		if(endTime != null)
			temp.setTime(endTime.getTime());
		this.endTime = temp;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


}