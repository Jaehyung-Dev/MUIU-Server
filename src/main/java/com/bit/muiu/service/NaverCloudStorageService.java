package com.bit.muiu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class NaverCloudStorageService {

    private final AmazonS3 amazonS3; // AWS S3를 사용하는 경우 예시입니다.
    private final String bucketName = "bitcamp126";

    public NaverCloudStorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        // 파일을 로컬에 저장하지 않고, 바로 네이버 클라우드에 업로드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3 또는 Naver Cloud의 Object Storage에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 업로드된 파일의 URL 반환
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
