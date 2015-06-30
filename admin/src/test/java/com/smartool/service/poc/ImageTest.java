package com.smartool.service.poc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.testng.annotations.Test;


public class ImageTest {
	
	@Test
	public void testGraphics() throws Exception {
		int imageWidth = 128;//图片的宽度  
        int imageHeight = 64;//图片的高度  
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);  
        Graphics graphics = image.getGraphics();   
        try  
        {    
            Font font=new Font("新宋体",Font.PLAIN,12);  
            graphics.setFont(font);  
            graphics.fillRect(0, 0, imageWidth, imageHeight);  
            graphics.setColor(new Color(0,0,0));//设置黑色字体,同样可以graphics.setColor(Color.black);  
            graphics.drawString("产品：深圳雅辉呼叫器", 0, 10);  
            graphics.drawString("网址:www.szsyhaf.com", 0, 36);    
            ImageIO.write(image, "PNG", new File("/Users/zhefang/Downloads/abc.png"));//生成图片方法一     
            //ImageIO,可以生成不同格式的图片，比如JPG,PNG,GIF.....  
        }  
        catch(Exception ex)  
        {  
            ex.printStackTrace();  
        }   
        //生成图片方法二开始,只知道生成jpg格式的图片,这个方法其他格式的还是不知道怎么弄。  
        /*try { 
            FileOutputStream fos = new FileOutputStream("D:\\abc.jpg"); 
            BufferedOutputStream bos = new BufferedOutputStream(fos); 
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos); 
            encoder.encode(image); 
            bos.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        }*/  
        //生成图片方法二结束  
        graphics.dispose();//释放资源  
	}
	
	
}
