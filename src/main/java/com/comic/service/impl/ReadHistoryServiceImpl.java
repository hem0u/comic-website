package com.comic.service.impl;

import com.comic.entity.ReadHistory;
import com.comic.entity.User;
import com.comic.mapper.ReadHistoryMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.ReadHistoryService;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ReadHistoryServiceImpl implements ReadHistoryService {

    @Resource
    private ReadHistoryMapper historyMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public ResultVO recordReadHistory(Long comicId, Long chapterId, String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 新增阅读历史
        ReadHistory history = new ReadHistory();
        history.setUserId(user.getId());
        history.setComicId(comicId);
        history.setChapterId(chapterId);
        historyMapper.insert(history);

        return ResultVO.success("记录阅读历史成功");
    }

    @Override
    public ResultVO getUserReadHistories(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        return ResultVO.success(historyMapper.selectByUserId(user.getId()));
    }
}