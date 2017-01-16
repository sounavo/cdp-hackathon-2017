package com.philips.hackathon.image;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class ImageUploader implements Runnable {
	private static String bucketName = "webcam-image-bucket-eu-west";
	private static String registrationBucketName = "registration-image-bucket";
	public static final String accessKey = System.getenv("ACCESS_KEY");
	public static final String secretKey = System.getenv("SECRET_KEY");
	private static String uploadFilePath = "./images";
	private static String registrationFilePath = "./registration";

	public ImageUploader() {

	}

	@Override
	public void run() {
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
		Map<String, String> metadata = new HashMap<String, String>();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		while (true) {

			try {
				System.out
						.println("Uploading a new images to S3");
				//Upload registration images
				for (File file:getAllFilesToUpload(registrationFilePath)){
					ObjectMetadata objectMetadata = new ObjectMetadata();
					objectMetadata.addUserMetadata("BabyName", file.getName().split("\\.")[0]);
					s3client.putObject(new PutObjectRequest(registrationBucketName, UUID.randomUUID().toString(),
							new FileInputStream(file),objectMetadata));
			
					file.delete();
				}
				//Upload webcam images
				for (File file:getAllFilesToUpload(uploadFilePath)){
					ObjectMetadata objectMetadata = new ObjectMetadata();
					objectMetadata.addUserMetadata("CreationTime", df.format(new java.util.Date(file.lastModified())));
					s3client.putObject(new PutObjectRequest(bucketName, UUID.randomUUID().toString(),
							new FileInputStream(file),objectMetadata));
			
					file.delete();
				}
				
				TimeUnit.MINUTES.sleep(2);

			} catch (AmazonServiceException ase) {
				System.out
						.println("Caught an AmazonServiceException, which "
								+ "means your request made it "
								+ "to Amazon S3, but was rejected with an error response"
								+ " for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
			} catch (AmazonClientException ace) {
				System.out.println("Caught an AmazonClientException, which "
						+ "means the client encountered "
						+ "an internal error while trying to "
						+ "communicate with S3, "
						+ "such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
			} catch (InterruptedException e) {
				System.out.println("Error in thread sleep:       " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error in reading image file:       " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private List<File> getAllFilesToUpload(String path) throws IOException {
		return Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

	}

	

	public static void main(String args[]) {
		(new Thread(new ImageUploader())).start();
	}

}
