package com.comic.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private String username;  // 用户名（非ID）
    private String nickname;  // 用户昵称（展示用）
    private String avatar;    // 用户头像（展示用）
    private Long comicId;
    private Long chapterId;
    private String content;
    private LocalDateTime createTime;
}