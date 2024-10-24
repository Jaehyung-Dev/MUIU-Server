package com.bit.muiu.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    private final NaverConfiguration naverConfiguration;

    public AwsS3Config(NaverConfiguration naverConfiguration) {
        this.naverConfiguration = naverConfiguration;
    }

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                naverConfiguration.getAccessKey(),
                naverConfiguration.getSecretKey()
        );

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        naverConfiguration.getEndPoint(),
                        naverConfiguration.getRegionName()
                ))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
