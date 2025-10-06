package com.comic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);

    // 从配置文件读取上传路径（默认上传到项目根目录的upload文件夹）
    @Value("${upload.path}")
    private String uploadPath;

    // 上传图片并返回访问URL
    public String uploadImage(MultipartFile file) throws IOException {
        logger.info("读取到的上传路径为: {}", uploadPath);
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("只能上传图片文件");
        }

        // 2. 创建固定上传目录（如果不存在）
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean mkdirsResult = uploadDir.mkdirs();
            logger.info("创建目录结果: {}", mkdirsResult);
        }

        // 3. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        // 4. 保存文件到固定目录
        File destFile = new File(uploadDir, fileName);
        file.transferTo(destFile);

        // 5. 返回图片访问URL（实际项目中应使用域名，这里简化为相对路径）
        return "/upload/" + fileName;
    }
}
