package com.philips.hackathon.customskill;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;


public class BabyInfoHandler extends SpeechletRequestStreamHandler {

    public BabyInfoHandler(Speechlet speechlet,
			Set<String> supportedApplicationIds) {
		super(speechlet, supportedApplicationIds);
		// TODO Auto-generated constructor stub
	}

	

}
