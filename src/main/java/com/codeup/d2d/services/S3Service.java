package com.codeup.d2d.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonServiceException;


import java.util.List;

@Service("S3Service")
@PropertySource("classpath:application.properties")
public class S3Service  {

    @Value("${amazon.s3.accessKey}")
    private String accessKey;

    @Value("${amazon.s3.secretKey}")
    private String secretKey;

    private AmazonS3 s3;

    public void copyObject(String object_key,String from_bucket,String to_bucket,String object_key2){
        s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        accessKey,
                        secretKey
                )))
                .withRegion(Regions.US_EAST_2)
                .build();
        try {
            s3.copyObject(from_bucket,object_key,to_bucket,object_key2);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
    public void deleteObject(String bucket_name, String object_key){
        s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                        accessKey,
                        secretKey
                )))
                .withRegion(Regions.US_EAST_2)
                .build();
        try {
            s3.deleteObject(bucket_name, object_key);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
    public void moveObject(String object_key,String from_bucket,String to_bucket,String object_key2){
        try{
            copyObject(object_key,from_bucket,to_bucket, object_key2);
            deleteObject(from_bucket,object_key);
        }catch(Exception e) {
            System.out.println("Failed to move object: "+object_key);
        }
    }
}
