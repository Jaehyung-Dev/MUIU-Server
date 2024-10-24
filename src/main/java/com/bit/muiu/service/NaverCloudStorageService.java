package com.bit.muiu.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class NaverCloudStorageService {

    private final AmazonS3 amazonS3Client;

    public NaverCloudStorageService(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    // 파일 업로드 메서드
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        File convertedFile = convertMultipartFileToFile(file);

        // 파일을 업로드할 때 public-read 권한 설정
        PutObjectRequest request = new PutObjectRequest("bitcamp126", fileName, convertedFile)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3Client.putObject(request);
        return amazonS3Client.getUrl("bitcamp126", fileName).toString();
    }

    // MultipartFile을 File로 변환하는 메서드
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
