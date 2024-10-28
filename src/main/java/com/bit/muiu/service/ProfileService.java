package com.bit.muiu.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    String uploadProfileImage(MultipartFile file, Long userId) throws IOException;
}
