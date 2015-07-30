package com.smartool.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	 public static String calCurrentTimeSeg() throws Exception
	  {
	    SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date=d.parse("2015-07-30 11:13:42");
	    System.out.println(date.getTime());
	    Long cur = Long.valueOf((date.getTime()));
	    String temporary = d.format(new Timestamp(cur.longValue()));
	    System.out.println(temporary);
	    String[] clock = temporary.split(" ")[1].split("\\:");
	    Integer timeSeg = Integer.valueOf((int)(((Integer.parseInt(clock[0]) * 60 + 
	      Integer.parseInt(clock[1])) * 60 + Integer.parseInt(clock[2])) * 1.0D / 1800.0D));
	    String formattedSeg = timeSeg.toString();
	    return formattedSeg;
	  }
	 
	 public static void main(String [] args)  throws Exception {
		 System.out.println(calCurrentTimeSeg());
	 }
}
