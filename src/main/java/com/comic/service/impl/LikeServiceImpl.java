package com.comic.service.impl;

import com.comic.dto.LikeCreateDTO;
import com.comic.entity.Like;
import com.comic.entity.User;
import com.comic.mapper.LikeMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.LikeService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeMapper likeMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public ResultVO like(LikeCreateDTO likeDTO, String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 检查是否已点赞
        Integer count = likeMapper.checkLike(user.getId(), likeDTO.getTargetType(), likeDTO.getTargetId());
        if (count > 0) {
            return ResultVO.error("已点赞该内容");
        }

        // 新增点赞
        Like like = new Like();
        like.setUserId(user.getId());
        like.setTargetType(likeDTO.getTargetType());
        like.setTargetId(likeDTO.getTargetId());
        likeMapper.insert(like);

        return ResultVO.success("点赞成功");
    }

    @Override
    public ResultVO cancelLike(LikeCreateDTO likeDTO, String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 取消点赞
        int rows = likeMapper.deleteByUserAndTarget(user.getId(), likeDTO.getTargetType(), likeDTO.getTargetId());
        if (rows == 0) {
            return ResultVO.error("未点赞该内容");
        }

        return ResultVO.success("取消点赞成功");
    }

    @Override
    public ResultVO checkLike(LikeCreateDTO likeDTO, String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        Integer count = likeMapper.checkLike(user.getId(), likeDTO.getTargetType(), likeDTO.getTargetId());
        return ResultVO.success(count > 0);
    }
}