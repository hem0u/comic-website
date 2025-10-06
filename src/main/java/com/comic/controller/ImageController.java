package com.comic.controller;

import com.comic.service.ChapterService;
import com.comic.utils.FileUploadUtils;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private ChapterService chapterService;

    // 上传章节图片：POST /api/image/chapter/{chapterId}
    @PostMapping("/chapter/{chapterId}")
    public ResultVO uploadChapterImage(
            @PathVariable Long chapterId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("sort") Integer sort
    ) throws IOException {
        // 1. 验证当前用户是否有权限上传该章节的图片
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean hasPermission = chapterService.checkChapterPermission(chapterId, username);
        if (!hasPermission) {
            return ResultVO.error("没有权限上传该章节的图片");
        }

        // 2. 上传图片并获取URL
        String imageUrl = fileUploadUtils.uploadImage(file);

        // 3. 保存图片记录到数据库
        chapterService.addChapterImage(chapterId, imageUrl, sort);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("url", imageUrl);

        return ResultVO.success(resultMap);
    }
}
