package com.comic.service.impl;

import com.comic.entity.ReadHistory;
import com.comic.entity.User;
import com.comic.mapper.ReadHistoryMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.ReadHistoryService;
import com.comic.vo.HistoryVO;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // 新增：检查“用户-漫画-章节”是否已存在
        ReadHistory existing = historyMapper.findByUserComicChapter(
                user.getId(), comicId, chapterId
        );
        if (existing != null) {
            // 若已存在，可直接返回“已记录”（或更新时间等操作）
            return ResultVO.success("阅读历史已记录");
        }

        // 新增阅读历史（仅当记录不存在时插入）
        ReadHistory history = new ReadHistory();
        history.setUserId(user.getId());
        history.setComicId(comicId);
        history.setChapterId(chapterId);
        historyMapper.insert(history);

        return ResultVO.success("记录阅读历史成功");
    }

    @Override
    public ResultVO getUserReadHistories(String username) {
        try {
            // 获取当前用户ID
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                return ResultVO.error("用户不存在");
            }

            // 查询用户阅读历史（已关联漫画和章节信息）
            List<HistoryVO> histories = historyMapper.selectUserHistories(user.getId());
            return ResultVO.success(histories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.error("获取阅读历史失败");
        }
    }

}