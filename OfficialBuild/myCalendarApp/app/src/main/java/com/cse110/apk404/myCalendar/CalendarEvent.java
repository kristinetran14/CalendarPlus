package com.cse110.apk404.myCalendar;

import java.util.Calendar;


public interface CalendarEvent extends CalendarObject {

	public abstract String getName();
	public abstract void setName(String name);
	public abstract Calendar getStartTime();
	public abstract void setStartTime(Calendar startTime);
	public abstract Calendar getEndTime();
	public abstract void setEndTime(Calendar endTime);
	public abstract boolean isStatic();
	public abstract void setStatic(boolean isStatic);
	public abstract String getDescription();
	public abstract void setDescription(String description);
	public abstract boolean isFinished();
	public abstract void setFinished(boolean isFinished);
	public abstract void setColor(String color);
	public abstract String getColor();
	public abstract String getLocation();
	public abstract long getId();

}


