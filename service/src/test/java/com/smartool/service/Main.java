package com.smartool.service;

import java.io.File;
import java.util.UUID;

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
	    	System.out.println(UUID.fromString("0f3d6550-8048-40a9-bf2c-1b730db0ca3c").hashCode());
	    }
}
