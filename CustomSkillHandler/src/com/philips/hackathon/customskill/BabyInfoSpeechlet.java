package com.philips.hackathon.customskill;

import org.apache.log4j.Logger;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class BabyInfoSpeechlet implements Speechlet {

	// Logger
		static final Logger log = Logger.getLogger(BabyInfoSpeechlet.class);
		
	// Intents
    private static final String IS_MY_BABY_INTENT = "IsMyBabyIntent";
    private static final String WHEM_DID_INTENT = "WhenDidMyBabyIntent";
    private static final String HOW_MUCH_DID_MY_BABY_INTENT = "HowMuchDidMyBabyIntent";
    private static final String WHEN_DOES_INTENT = "WhenDoesMyBabyIntent";
    
    // Slots
    private static final String ACTITITY_SLOT = "activity";
    private static final String TIME_SLOT = "temperature";

	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session)
			throws SpeechletException {
        log.info("onLaunch requestId: " + request.getRequestId() + ", sessionId: " +
                session.getSessionId());

	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session)
			throws SpeechletException {
		log.info("onLaunch requestId: " + request.getRequestId() + ", sessionId: " +
                session.getSessionId());
        return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session)
			throws SpeechletException {
		Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        
        switch (intentName) {
	        case IS_MY_BABY_INTENT:
	        	return isMyIntentHandler(session, intent);
	        
	   /*     case WHEM_DID_INTENT:
	        	return whenDidIntentHandler(session, intent);
        	
	        case HOW_MUCH_DID_MY_BABY_INTENT:
	        	return howMuchIntentHandler(session, intent);*/
	       /* case BYE_INTENT:
	        	return byeIntentHandler(session);	*/
	        	
	        case WHEN_DOES_INTENT:
                return whenDoesIntentHAndler(session, intent);
	        default:
	        	throw new SpeechletException("Invalid Intent");
        }
	}

	private SpeechletResponse whenDoesIntentHAndler(Session session, Intent intent) {
        String activity = intent.getSlot(ACTITITY_SLOT).getValue();

        switch(activity) {
            case "need to go to sleep":
                String babyOfSixMonthsStretchesBetweenNaps = "This is different for every baby, but a baby of 6 months can have stretches of 2 to 4 hours between naps. Your baby is awake for 3 and a half hour.";
                return getSpeechletBabyNeedsToGoToSleep(babyOfSixMonthsStretchesBetweenNaps);
                          
        }

        return getWelcomeResponse();
    }
	
	private SpeechletResponse getSpeechletBabyNeedsToGoToSleep(String answer) {
        String speechText = answer;
        String repromptText = speechText;
        
        return getSpeechletResponse(speechText, repromptText, true);
    }
	
	private SpeechletResponse isMyIntentHandler(Session session, Intent intent) {
		String activity = intent.getSlot(ACTITITY_SLOT).getValue();
		String time = intent.getSlot(TIME_SLOT).getValue();
		if(null!= activity){
			
		}
		return null;
	}

	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session)
			throws SpeechletException {
		// TODO Auto-generated method stub

	}
	
	 private SpeechletResponse getWelcomeResponse() {
	        String speechText =
	                "Welcome to the baby monitor. Track you baby activities with Alexa " ;
	        String repromptText = speechText;
	        return getSpeechletResponse(speechText, repromptText, true);
	    }
	 
	 private SpeechletResponse getSpeechletResponse(String speechText, String repromptText,
	            boolean isAskResponse) {
	        SimpleCard card = new SimpleCard();
	        card.setTitle("Baby Monitor");
	        card.setContent(speechText);

	        // Create the plain text output.
	        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	        speech.setText(speechText);

	        if (isAskResponse) {
	            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
	            repromptSpeech.setText(repromptText);
	            Reprompt reprompt = new Reprompt();
	            reprompt.setOutputSpeech(repromptSpeech);

	            return SpeechletResponse.newAskResponse(speech, reprompt, card);

	        } else {
	            return SpeechletResponse.newTellResponse(speech, card);
	        }
	    }

}
