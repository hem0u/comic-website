package com.comic.mapper;

import com.comic.entity.Comment;
import com.comic.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    // 新增评论
    int insert(Comment comment);

    // 根据漫画ID查询评论（支持分页）
    List<CommentVO> selectByComicId(Long comicId, Integer offset, Integer size);

    // 根据章节ID查询评论（支持分页）
    List<CommentVO> selectByChapterId(Long chapterId, Integer offset, Integer size);

    // 查询漫画评论总数
    Integer countByComicId(Long comicId);

    // 查询章节评论总数
    Integer countByChapterId(Long chapterId);
}