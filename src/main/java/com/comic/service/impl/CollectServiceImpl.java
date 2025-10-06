package com.comic.service.impl;

import com.comic.entity.Collect;
import com.comic.entity.User;
import com.comic.mapper.CollectMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.CollectService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private UserMapper userMapper;

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
    public ResultVO cancelCollect(Long comicId, String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 取消收藏
        int rows = collectMapper.deleteByUserAndComic(user.getId(), comicId);
        if (rows == 0) {
            return ResultVO.error("未收藏该漫画");
        }

        return ResultVO.success("取消收藏成功");
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

    @Override
    public ResultVO getUserCollects(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        return ResultVO.success(collectMapper.selectByUserId(user.getId()));
    }
}