package com.philips.hackathon.customskill;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class BabyInfoQuery {

    static DynamoDB dynamoDB = new DynamoDB(new AmazonDynamoDBClient(new ProfileCredentialsProvider()));

    static String tableName = "ImageAnalyticsDB";

    
   /* private static void findWhatIsBabyDointAtTime(Date dateTime) {

    	DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime().withZoneUTC(); String isoDate = dateFormatter.print(currentTime);
        Table table = dynamoDB.getTable(tableName);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":startTime", new AttributeValue().withS(dateFormatter.print(dateTime.getTime()-3600)));
        eav.put(":endTime", new AttributeValue().withS(dateFormatter.print(dateTime.getTime()-3600)));
        
     
        DynamoDBQueryExpression<Reply> queryExpression = new DynamoDBQueryExpression<Reply>()
                .withKeyConditionExpression("Id = :val1 and ReplyDateTime between :val2 and :val3")
                .withExpressionAttributeValues(eav);
        
        ItemCollection<QueryOutcome> items = table.query(spec);
        
        }
        
    }*/

   
    
}