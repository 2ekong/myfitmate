package com.myfitmate.myfitmate.domain.meal.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class MealImageUtil {

    private static final String IMAGE_DIR = "src/main/resources/static/uploads/meal-images";

    public String saveImage(MultipartFile file) throws IOException {
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String savePath = Paths.get(IMAGE_DIR, uniqueName).toString();
        file.transferTo(new File(savePath));

        return "/uploads/meal-images/" + uniqueName; // 웹에서 접근할 경로
    }

    public void deleteImage(String filePath) {
        File file = new File("src/main/resources/static" + filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
