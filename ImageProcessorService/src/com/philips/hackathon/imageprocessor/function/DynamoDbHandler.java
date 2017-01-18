package com.philips.hackathon.imageprocessor.function;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;

public class DynamoDbHandler {
	// DynamoDB table name for storing image metadata.
   
    // DynamoDB table attribute name for storing image id.
    private static final String IMAGE_TABLE_ID_NAME = "id";
    // DynamoDB table attribute name for storing the bucket name where holds the image's content.
    private static final String IMAGE_TABLE_BUCKET_NAME = "bucket";
    // DynamoDB table attribute name for the s3 key
    private static final String IMAGE_TABLE_KEY = "key";
    // DynamoDB table attribute name for storing image id.
    private static final String IMAGE_TABLE_VALUE = "value";
 // DynamoDB table attribute name for storing creation time
    private static final String IMAGE_TABLE_CREATION_TIME = "creationTime";
 // Optional DynamoDB table attribute name for storing baby name.
    private static final String IMAGE_TABLE_BABY_NAME = "babyname";
    AmazonDynamoDB dynamoDb;
    public DynamoDbHandler(){
    	dynamoDb =  new AmazonDynamoDBClient(new BasicAWSCredentials(Configurations.ACCESS_KEY, Configurations.SECRET_KEY));
    	dynamoDb.setEndpoint(Configurations.DYNAMO_DB_ENDPOINT);
    }
	
	public void uploadStaistics(String rekognitionJson,String bucketName, String s3Key, String creationTime, String matchedBabyName){
		Map<String, AttributeValue> attributes = new HashMap<String, AttributeValue>();
        attributes.putIfAbsent(IMAGE_TABLE_ID_NAME, new AttributeValue().withS(UUID.randomUUID().toString()));
        attributes.put(IMAGE_TABLE_BUCKET_NAME, new AttributeValue().withS(bucketName));
        attributes.put(IMAGE_TABLE_KEY, new AttributeValue().withS(s3Key));
        attributes.put(IMAGE_TABLE_VALUE, new AttributeValue().withS(rekognitionJson));
        attributes.put(IMAGE_TABLE_CREATION_TIME, new AttributeValue().withS(creationTime));
        if(!matchedBabyName.equals("")){
        	 attributes.put(IMAGE_TABLE_BABY_NAME, new AttributeValue().withS(matchedBabyName));
        }       
        dynamoDb.putItem(new PutItemRequest()
                .withTableName(Configurations.IMAGE_TABLE_NAME)
                .withItem(attributes));
	}

}
