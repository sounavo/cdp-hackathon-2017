package com.philips.hackathon.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class CamCapture implements Runnable {
   
    public CamCapture() {
        //canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void run() {
    	Webcam webcam = Webcam.getDefault();
    	webcam.open();
    	//webcam.setViewSize(new Dimension(1024, 768));
    	while (true) {
    		BufferedImage image = webcam.getImage();
        	try {
    			ImageIO.write(image, "JPG", new File("./images/"+System.currentTimeMillis()+".jpg"));
    			TimeUnit.SECONDS.sleep(5);
    		} catch (IOException | InterruptedException e) {
    			e.printStackTrace();
    		}  
        	
    	}
    	
    }
    
    public static void main(String args[]) {
        (new Thread(new CamCapture())).start();
    }
}