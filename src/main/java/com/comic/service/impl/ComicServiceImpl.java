package com.comic.service.impl;

import com.comic.dto.ComicCreateDTO;
import com.comic.entity.Category;
import com.comic.entity.Comic;
import com.comic.entity.User;
import com.comic.mapper.CategoryMapper;
import com.comic.mapper.ComicMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.ComicService;
import com.comic.vo.ComicVO;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ComicServiceImpl implements ComicService {

    @Resource
    private ComicMapper comicMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResultVO<ComicVO> createComic(ComicCreateDTO comicDTO, String username) {
        // 1. 查询当前用户信息（获取用户ID）
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        // 2. 验证分类是否存在
        Category category = categoryMapper.selectById(comicDTO.getCategoryId());
        if (category == null) {
            return ResultVO.error("分类不存在");
        }

        // 3. 转换DTO为实体类并保存
        Comic comic = new Comic();
        BeanUtils.copyProperties(comicDTO, comic);
        comic.setAuthorId(user.getId());  // 设置作者ID
        comic.setIsApproved((byte) 0);  // 默认待审核
        comic.setViewCount(0);
        comic.setCollectCount(0);

        comicMapper.insert(comic);

        // 4. 转换为VO返回
        ComicVO comicVO = new ComicVO();
        BeanUtils.copyProperties(comic, comicVO);
        comicVO.setAuthorName(user.getNickname());  // 作者昵称
        comicVO.setCategoryName(category.getName());  // 分类名称

        return ResultVO.success(comicVO);
    }
    
    @Override
    public ResultVO<ComicVO> getComicById(Long id) {
        // 1. 查询漫画信息
        Comic comic = comicMapper.selectById(id);
        if (comic == null) {
            return ResultVO.error("漫画不存在");
        }

        // 2. 查询作者和分类信息
        User author = userMapper.selectById(comic.getAuthorId());
        Category category = categoryMapper.selectById(comic.getCategoryId());

        // 3. 封装VO
        ComicVO comicVO = new ComicVO();
        BeanUtils.copyProperties(comic, comicVO);

        // 处理作者信息（添加非空判断）
        if (author != null) {
            comicVO.setAuthorName(author.getNickname());
        } else {
            comicVO.setAuthorName("未知作者"); // 或空字符串，根据业务需求处理
        }

        // 处理分类信息（建议同样添加非空判断）
        if (category != null) {
            comicVO.setCategoryName(category.getName());
        } else {
            comicVO.setCategoryName("未知分类");
        }

        return ResultVO.success(comicVO);
    }
}