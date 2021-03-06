package com.smartool.wapservice;

import java.io.File;

import org.apache.catalina.startup.Tomcat;

public class Main {
	 int port = 1099;
	 private void startTomcat() throws Exception {
	        Tomcat tomcat = new Tomcat();
	        tomcat.setPort(port);
	        tomcat.addWebapp("/wap", new File("src/main/webapp").getAbsolutePath());
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
