package com.skillspace.user.service;

import com.skillspace.user.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileUploadResponse uploadFile(MultipartFile multipartFile);
}
