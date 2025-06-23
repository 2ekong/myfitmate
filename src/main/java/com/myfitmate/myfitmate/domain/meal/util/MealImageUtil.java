package com.myfitmate.myfitmate.domain.meal.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class MealImageUtil {

    private static final String IMAGE_DIR = System.getProperty("user.dir") + "/uploads/meal-images";

    public String saveImage(MultipartFile file) throws IOException {
        System.out.println("🔥 [MealImageUtil] saveImage 진입");
        System.out.println("🔥 [파일 이름] " + file.getOriginalFilename());
        System.out.println("🔥 [파일 크기] " + file.getSize());

        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("✅ [디렉토리 생성] " + created);
        } else {
            System.out.println("✅ [디렉토리 존재함]");
        }

        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String savePath = Paths.get(IMAGE_DIR, uniqueName).toString();
        System.out.println("🔥 [최종 저장 경로] " + savePath);

        try {
            file.transferTo(new File(savePath));
            System.out.println("✅ [파일 저장 성공]");
        } catch (IOException e) {
            System.out.println("❌ [파일 저장 실패]: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return "/uploads/meal-images/" + uniqueName; // 웹에서 접근할 경로
    }

    public void deleteImage(String filePath) {
        File file = new File("src/main/resources/static" + filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("🔥 [이미지 삭제] 성공 여부: " + deleted);
        } else {
            System.out.println("🔥 [삭제 실패] 파일 없음: " + filePath);
        }
    }
}
