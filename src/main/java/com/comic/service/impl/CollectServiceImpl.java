package com.comic.service.impl;

import com.comic.entity.Collect;
import com.comic.entity.User;
import com.comic.mapper.CollectMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.CollectService;
import com.comic.vo.ComicVO;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public ResultVO getUserCollects(String username) {
        try {
            // 获取当前用户ID
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                return ResultVO.error("用户不存在");
            }

            // 查询用户收藏的漫画列表（已关联漫画信息）
            List<ComicVO> collects = collectMapper.selectUserCollects(user.getId());
            return ResultVO.success(collects);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("获取收藏列表失败");
        }
    }

    @Override
    public ResultVO cancelCollect(Long comicId, String username) {
        try {
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                return ResultVO.error("用户不存在");
            }

            // 删除收藏记录
            collectMapper.deleteByUserIdAndComicId(user.getId(), comicId);
            return ResultVO.success("取消收藏成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("取消收藏失败");
        }
    }

    @Override
    public ResultVO collectComic(Long comicId, String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 检查是否已收藏
        Integer count = collectMapper.checkCollect(user.getId(), comicId);
        if (count > 0) {
            return ResultVO.error("已收藏该漫画");
        }

        // 新增收藏
        Collect collect = new Collect();
        collect.setUserId(user.getId());
        collect.setComicId(comicId);
        collectMapper.insert(collect);

        return ResultVO.success("收藏成功");
    }

    @Override
    public ResultVO checkCollect(Long comicId, String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        Integer count = collectMapper.checkCollect(user.getId(), comicId);
        return ResultVO.success(count > 0);
    }
}