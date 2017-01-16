package com.philips.hackathon.customskill;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;


public class BabyInfoHandler extends SpeechletRequestStreamHandler {

	 private static final Set<String> supportedApplicationIds;
	    static {
	        supportedApplicationIds = new HashSet<String>();
	        supportedApplicationIds.add("amzn1.ask.skill.8bf47088-dbf5-457a-8e00-c9af5dac57ee");
	    }
	   
    public BabyInfoHandler(Speechlet speechlet,
			Set<String> supportedApplicationIds) {
		super(speechlet, supportedApplicationIds);
		// TODO Auto-generated constructor stub
	}

	

}
