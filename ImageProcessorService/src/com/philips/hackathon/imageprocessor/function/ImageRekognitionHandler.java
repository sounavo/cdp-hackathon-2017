package com.philips.hackathon.imageprocessor.function;

import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageRekognitionHandler {

	AmazonRekognitionClient rekognitionClient;
	public ImageRekognitionHandler(){
		 rekognitionClient = new AmazonRekognitionClient(new BasicAWSCredentials(Configurations.ACCESS_KEY, Configurations.SECRET_KEY))
			.withEndpoint(Configurations.REKOGNITION_ENDPOINT);
	}
	public String getLabels(String s3ObjectKey, String bucketName) {

		DetectFacesRequest request = new DetectFacesRequest().withImage(
				new Image().withS3Object(new S3Object().withName(s3ObjectKey)
						.withBucket(bucketName))).withAttributes(
				Attribute.ALL);

		//rekognitionClient.setSignerRegionOverride("us-east-1");
		try {
			DetectFacesResult result = rekognitionClient.detectFaces(request);
			ObjectMapper objectMapper = new ObjectMapper();
			// System.out.println("Result = " +
			 return objectMapper.writeValueAsString(result);
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public String getMactchingRegisteredBaby(String s3ObjectKey, String srcBucketName){	
		AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(Configurations.ACCESS_KEY, Configurations.SECRET_KEY));
		
		ObjectListing listing = s3Client.listObjects( Configurations.REGISTRATION_BUCKET);
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		CompareFacesRequest compareFacesRequest = new CompareFacesRequest();
		compareFacesRequest.setTargetImage(getImageUtil(srcBucketName, s3ObjectKey));
		for(S3ObjectSummary s3summary: summaries){						     
	        compareFacesRequest.setSourceImage(getImageUtil(Configurations.REGISTRATION_BUCKET, s3summary.getKey()));
	        CompareFacesResult compareFacesResult = rekognitionClient.compareFaces(compareFacesRequest);
	        if(compareFacesResult.getFaceMatches().size()== 0){
	        	return "";
	        }
	        if(compareFacesResult.getFaceMatches().get(0)!=null 
	        		&& (compareFacesResult.getFaceMatches().get(0).getSimilarity()>70)){
	        	com.amazonaws.services.s3.model.S3Object s3Object = s3Client.getObject(s3summary.getBucketName(), s3summary.getKey());
        		return s3Object.getObjectMetadata().getUserMetadata().get("BabyName");
	        }        
		}
		return "";

	}
	
	private static Image getImageUtil(String bucket, String key) {
        return new Image()
                .withS3Object(new S3Object()
                        .withBucket(bucket)
                        .withName(key));
    }

}
