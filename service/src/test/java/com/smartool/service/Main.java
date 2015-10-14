package com.smartool.service;

import java.io.File;

import org.apache.catalina.startup.Tomcat;

public class Main {
	 int port = 8080;
	 private void startTomcat() throws Exception {
	        Tomcat tomcat = new Tomcat();
	        tomcat.setPort(port);
	        tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
	        tomcat.start();
	        tomcat.getServer().await();
	    }

	    /**
	     * @param args
	     */
	    public static void main(String[] args) throws Exception {
	    	Main main = new Main();

	        main.startTomcat();
	    }
}
