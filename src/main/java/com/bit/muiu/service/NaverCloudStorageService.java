package com.bit.muiu.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.bit.muiu.config.NaverConfiguration;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NaverCloudStorageService {

    private final AmazonS3 s3Client;
    private final String bucketName;

    public NaverCloudStorageService(NaverConfiguration naverConfiguration) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(naverConfiguration.getAccessKey(), naverConfiguration.getSecretKey());
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(naverConfiguration.getEndPoint(), naverConfiguration.getRegionName()))
                .build();
        this.bucketName = "bitcamp126";
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
        }

        return s3Client.getUrl(bucketName, fileName).toString();
    }
}
