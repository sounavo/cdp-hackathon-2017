package com.philips.hackathon.imageprocessor.function;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Scanner;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class ImageHandlerLambda implements RequestHandler<S3Event, Object> {

	@Override
	public Object handleRequest(S3Event s3Event, Context context) {
		try{
			LambdaLogger logger = context.getLogger();
			S3EventNotificationRecord record = s3Event.getRecords().get(0);

			String srcBucket = record.getS3().getBucket().getName();

			logger.log(" source bucket name: "+srcBucket);
			System.out.println(" source bucket name:::: "+srcBucket);
			// Object key may have spaces or unicode non-ASCII characters.
			String inputKey = record.getS3().getObject().getKey()
					.replace('+', ' ');
			logger.log("input key "+inputKey );
			inputKey = URLDecoder.decode(inputKey, "UTF-8");

			logger.log("input key after decoding----"+inputKey );

			AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(Configurations.ACCESS_KEY, Configurations.SECRET_KEY));
			S3Object s3Object = s3Client.getObject(new GetObjectRequest(
					srcBucket, inputKey));

			InputStream is = s3Object.getObjectContent();

			Scanner scanner = new Scanner(is);
			StringBuilder fileContent = new StringBuilder();
			String newLine = System.getProperty("line.separator");

			try{
				while(scanner.hasNextLine()){
					fileContent.append(scanner.nextLine()+newLine);
				}
			}finally{
				if(scanner!= null){
					scanner.close();
				}
			}
			logger.log("file contents: "+fileContent);
			String creationTime = s3Object.getObjectMetadata().getUserMetadata().get("CreationTime");
			
			ImageRekognitionHandler rekognitionHandler = new ImageRekognitionHandler();
			String rekognitionJson = rekognitionHandler.getLabels(inputKey, srcBucket);
			String matchedBabyName = rekognitionHandler.getMactchingRegisteredBaby(inputKey,srcBucket);
			logger.log("matchedBabyName: "+matchedBabyName);
			new DynamoDbHandler().uploadStaistics(rekognitionJson, srcBucket, inputKey,creationTime, matchedBabyName);
			
			
			return "file processed";
		}catch(IOException ioe){
			throw new RuntimeException(ioe);
		}
	}
}


