package com.zhiweijia.nice;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.AbstractCollection;

public class CalendarObjectListInputStream {

	private ObjectInputStream objIn;
	
	public CalendarObjectListInputStream(String filename, Context context) throws IOException {
		FileInputStream fi = context.openFileInput(filename);
		this.objIn = new ObjectInputStream(fi);     
	}
	
	public CalendarObjectList<? extends AbstractCollection<? extends CalendarObject>, ? extends CalendarObject> readList() throws CalendarError {
		CalendarObjectList<? extends AbstractCollection<? extends CalendarObject>, ? extends CalendarObject> result = null; 
		try {
			Object res = this.objIn.readObject();
			if (res instanceof CalendarObjectList<?, ?>)
				result = (CalendarObjectList<? extends AbstractCollection<? extends CalendarObject>, ? extends CalendarObject>) res;
			else
				throw new CalendarError("Invalid Input List");
		} catch (ClassNotFoundException | ClassCastException | IOException e) {
			e.printStackTrace();
			throw new CalendarError("Invalid Input List");
		}
		
	    return result;
	}
	
	public void close() throws IOException {
		this.objIn.close();
	}
}