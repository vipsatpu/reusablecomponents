package com.qerreference.utils;

import java.util.Comparator;
import java.util.Date;
import java.util.Map.Entry;

public class EntryObjectDateComparator implements Comparator<Entry<String, Object[]>> {

	@Override
	public int compare(Entry<String, Object[]> o1, Entry<String, Object[]> o2) {
		
		Object[] obj1 = o1.getValue();
		Object[] obj2 = o2.getValue();
		
		Date date1 = null;
		Date date2 = null;
		if(obj1[4] instanceof Date && obj2[4] instanceof Date ){
			
			date1 = (Date) obj1[4];
			date2 = (Date) obj2[4];
		}
		if(obj1[1] instanceof Date && obj2[1] instanceof Date){
			date1 = (Date) obj1[1];
			date2 = (Date) obj2[1];
		}
		if (date1 != null && date2 != null ) {
			int dateComp = date1.compareTo(date2);
			return dateComp;
		} else {
			return 0;
		}	
	}
}