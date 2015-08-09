package com.smartool.service.poc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.testng.annotations.Test;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class QCodeTest {
	
	@Test
	public void testCreateQCode() throws Exception {
		/*File file1 = new File("/Users/zhefang/Downloads/test1.jpg");
		file1.createNewFile();
		OutputStream out = new FileOutputStream(file1);
		QRCode.from("https://www.google.com").withSize(400, 400).to(ImageType.JPG).writeTo(out);*/
	}
}
