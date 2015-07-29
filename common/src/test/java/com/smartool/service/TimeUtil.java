package com.smartool.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {
	
	 public static String calCurrentTimeSeg()
	  {
	    SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Long cur = Long.valueOf(System.currentTimeMillis());
	    String temporary = d.format(new Timestamp(cur.longValue()));
	    System.out.println(temporary);
	    String[] clock = temporary.split(" ")[1].split("\\:");
	    Integer timeSeg = Integer.valueOf((int)(((Integer.parseInt(clock[0]) * 60 + 
	      Integer.parseInt(clock[1])) * 60 + Integer.parseInt(clock[2])) * 1.0D / 1800.0D));
	    String formattedSeg = timeSeg.toString();
	    return formattedSeg;
	  }
	 
	 public static void main(String [] args) {
		 System.out.println(calCurrentTimeSeg());
	 }
}
