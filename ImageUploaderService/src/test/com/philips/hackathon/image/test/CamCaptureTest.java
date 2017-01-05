package com.philips.hackathon.image.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.philips.hackathon.image.CamCapture;

public class CamCaptureTest {
	
	 @Test(expected = IllegalArgumentException.class)
	    public void shouldThrowIllegalArgumentExceptionForInvalidNumber() throws InterruptedException {
	        CamCapture camCapture = new CamCapture();
	        //camCapture.run();	        
	        ExecutorService service = Executors.newSingleThreadExecutor();
	        service.execute(camCapture);

	        service.shutdown();
	        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
	    }

}
