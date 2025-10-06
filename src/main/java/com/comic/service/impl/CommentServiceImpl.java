package com.comic.service.impl;

import com.comic.dto.CommentCreateDTO;
import com.comic.entity.Comment;
import com.comic.entity.User;
import com.comic.mapper.CommentMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.CommentService;
import com.comic.vo.CommentVO;
import com.comic.vo.PageVO;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public ResultVO submitComment(CommentCreateDTO commentDTO, String username) {
        // 1. 验证用户存在
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 2. 验证评论内容长度（500字内）
        if (commentDTO.getContent().length() > 500) {
            return ResultVO.error("评论内容不能超过500字");
        }

        // 3. 保存评论
        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setComicId(commentDTO.getComicId());
        comment.setChapterId(commentDTO.getChapterId());  // 可为null
        comment.setContent(commentDTO.getContent());
        commentMapper.insert(comment);

        return ResultVO.success("评论提交成功");
    }

    @Override
    public ResultVO<PageVO<CommentVO>> getComicComments(Long comicId, Integer page, Integer size) {
        // 1. 计算分页参数
        Integer offset = (page - 1) * size;
        // 2. 查询评论列表和总数
        List<CommentVO> comments = commentMapper.selectByComicId(comicId, offset, size);
        Integer total = commentMapper.countByComicId(comicId);
        // 3. 封装分页结果
        PageVO<CommentVO> pageVO = new PageVO<>();
        pageVO.setList(comments);
        pageVO.setTotal(total);
        pageVO.setPage(page);
        pageVO.setSize(size);
        return ResultVO.success(pageVO);
    }

    @Override
    public ResultVO<PageVO<CommentVO>> getChapterComments(Long chapterId, Integer page, Integer size) {
        // 1. 计算分页参数
        Integer offset = (page - 1) * size;
        // 2. 查询评论列表和总数
        List<CommentVO> comments = commentMapper.selectByChapterId(chapterId, offset, size);
        Integer total = commentMapper.countByChapterId(chapterId);
        // 3. 封装分页结果
        PageVO<CommentVO> pageVO = new PageVO<>();
        pageVO.setList(comments);
        pageVO.setTotal(total);
        pageVO.setPage(page);
        pageVO.setSize(size);
        return ResultVO.success(pageVO);
    }
}