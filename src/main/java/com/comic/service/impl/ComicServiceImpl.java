package com.comic.service.impl;

import com.comic.dto.ComicCreateDTO;
import com.comic.dto.ComicQueryDTO;
import com.comic.entity.Category;
import com.comic.entity.Comic;
import com.comic.entity.Image;
import com.comic.entity.User;
import com.comic.mapper.CategoryMapper;
import com.comic.mapper.ComicMapper;
import com.comic.mapper.ImageMapper;
import com.comic.mapper.UserMapper;
import com.comic.service.ChapterService;
import com.comic.service.ComicService;
import com.comic.vo.ChapterVO;
import com.comic.vo.ComicVO;
import com.comic.vo.PageVO;
import com.comic.vo.ResultVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComicServiceImpl implements ComicService {

    @Resource
    private ComicMapper comicMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private ChapterService chapterService;

    @Override
    public ResultVO<ComicVO> createComic(ComicCreateDTO comicDTO, String username) {
        // 保持原有逻辑不变
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ResultVO.error("用户不存在");
        }

        Category category = categoryMapper.selectById(comicDTO.getCategoryId());
        if (category == null) {
            return ResultVO.error("分类不存在");
        }

        Comic comic = new Comic();
        BeanUtils.copyProperties(comicDTO, comic);
        comic.setAuthorId(user.getId());
        comic.setIsApproved((byte) 0);
        comic.setViewCount(0);
        comic.setCollectCount(0);

        comicMapper.insert(comic);

        ComicVO comicVO = new ComicVO();
        BeanUtils.copyProperties(comic, comicVO);
        comicVO.setAuthorName(user.getNickname());
        comicVO.setCategoryName(category.getName());

        return ResultVO.success(comicVO);
    }

    @Override
    public ResultVO<ComicVO> getComicById(Long id) {
        // 保持原有逻辑不变
        Comic comic = comicMapper.selectById(id);
        if (comic == null) {
            return ResultVO.error("漫画不存在");
        }

        User author = userMapper.selectById(comic.getAuthorId());
        Category category = categoryMapper.selectById(comic.getCategoryId());

        ComicVO comicVO = new ComicVO();
        BeanUtils.copyProperties(comic, comicVO);

        comicVO.setAuthorName(author != null ? author.getNickname() : "未知作者");
        comicVO.setCategoryName(category != null ? category.getName() : "未知分类");

        return ResultVO.success(comicVO);
    }

    @Override
    public ResultVO<ComicVO> getComicDetail(Long id) {
        // 保持原有逻辑不变
        Comic comic = comicMapper.selectById(id);
        if (comic == null) {
            return ResultVO.error("漫画不存在");
        }

        User author = userMapper.selectById(comic.getAuthorId());
        Category category = categoryMapper.selectById(comic.getCategoryId());

        ComicVO comicVO = new ComicVO();
        comicVO.setId(comic.getId());
        comicVO.setTitle(comic.getTitle());
        comicVO.setAuthorName(author.getNickname());
        comicVO.setCover(comic.getCover());
        comicVO.setDescription(comic.getDescription());
        comicVO.setCategoryName(category.getName());
        comicVO.setStatus(comic.getStatus());
        comicVO.setViewCount(comic.getViewCount());
        comicVO.setCollectCount(comic.getCollectCount());
        comicVO.setCreateTime(comic.getCreateTime());

        List<ChapterVO> chapters = chapterService.getChaptersByComicId(id);
        comicVO.setChapters(chapters);

        for (ChapterVO chapter : chapters) {
            List<Image> images = imageMapper.selectByChapterId(chapter.getId());
            chapter.setImages(images);
        }

        return ResultVO.success(comicVO);
    }

    @Override
    public ResultVO<PageVO<ComicVO>> listComics(ComicQueryDTO queryDTO) {
        Integer page = queryDTO.getPage();
        Integer size = queryDTO.getSize();
        Integer offset = (page - 1) * size;

        // 查询总数（保持不变）
        Integer total = comicMapper.countByCondition(queryDTO);

        // 直接查询 ComicVO 列表（SQL 已关联查询）
        List<ComicVO> comicVOList = comicMapper.listByCondition(queryDTO, offset, size);

        // 封装分页结果
        PageVO<ComicVO> pageVO = new PageVO<>();
        pageVO.setTotal(total);
        pageVO.setPage(page);
        pageVO.setSize(size);
        pageVO.setList(comicVOList);

        return ResultVO.success(pageVO);
    }
}
