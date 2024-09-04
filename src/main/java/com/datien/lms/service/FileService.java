package com.datien.lms.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.io.File.separator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private String saveFile(
            @Nonnull MultipartFile file,
            @Nonnull Long bookId,
            @Nonnull Long userId
    ) {
        final String fileUploadSubPath = "users" + separator + userId;
        return null;
    }

}
