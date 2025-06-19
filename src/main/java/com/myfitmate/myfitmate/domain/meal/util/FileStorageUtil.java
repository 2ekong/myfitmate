package com.myfitmate.myfitmate.domain.meal.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Component
public class FileStorageUtil {

    private static final String BASE_PATH = "src/main/resources/static/uploads";

    public static String saveImage(MultipartFile file, String subDir) throws IOException {
        File directory = new File(BASE_PATH, subDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String newFilename = uuid + "_" + originalFilename;

        File dest = Paths.get(directory.getAbsolutePath(), newFilename).toFile();
        file.transferTo(dest);

        // return 웹에서 접근 가능한 경로
        return "/uploads/" + subDir + "/" + newFilename;
    }

    public static String generateFileHash(MultipartFile file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] fileBytes = file.getBytes();
            byte[] hash = digest.digest(fileBytes);

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해시 생성 실패: SHA-256 not supported", e);
        }
    }
}
