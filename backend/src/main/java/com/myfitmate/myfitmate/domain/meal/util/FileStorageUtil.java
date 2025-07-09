package com.myfitmate.myfitmate.domain.meal.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStorageUtil {

    private final String uploadDir = "src/main/resources/static/uploads/meal-images";

    public String saveMealImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID() + extension;
        File saveFile = new File(uploadDir, uniqueFileName);

        // 🔍 디버깅 로그
        System.out.println("=== [파일 저장 디버깅] ===");
        System.out.println("파일 이름: " + originalFilename);
        System.out.println("확장자: " + extension);
        System.out.println("저장될 파일 이름: " + uniqueFileName);
        System.out.println("파일 크기: " + file.getSize());
        System.out.println("파일 비었는가?: " + file.isEmpty());
        System.out.println("저장 경로: " + saveFile.getAbsolutePath());

        if (!saveFile.getParentFile().exists()) {
            boolean dirCreated = saveFile.getParentFile().mkdirs();
            System.out.println("디렉토리 생성됨? " + dirCreated);
        }

        // 실제 저장 시도
        try {
            file.transferTo(saveFile);
            System.out.println("파일 저장 성공");
        } catch (IOException e) {
            System.out.println("파일 저장 실패: " + e.getMessage());
            e.printStackTrace();
            throw e; // 상위에서 MealException으로 wrapping
        }

        return "/uploads/meal-images/" + uniqueFileName;
    }
}
