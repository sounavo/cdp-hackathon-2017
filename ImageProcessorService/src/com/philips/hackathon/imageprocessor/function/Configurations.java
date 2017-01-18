package com.philips.hackathon.imageprocessor.function;

public class Configurations {

	public static final String ACCESS_KEY = System.getenv("ACCESS_KEY"); 
	public static final String SECRET_KEY = System.getenv("SECRET_KEY");
	public static final String REGISTRATION_BUCKET = "registration-image-bucket";
	public static final String IMAGE_TABLE_NAME = "ImageAnalyticsDB";
	public static final String DYNAMO_DB_ENDPOINT = "dynamodb.us-west-2.amazonaws.com";
	public static final String REKOGNITION_ENDPOINT = "rekognition.us-west-2.amazonaws.com";
}
