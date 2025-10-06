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
    private ImageMapper imageMapper;  // 注入图片Mapper

    @Resource
    private ChapterService chapterService;

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

    @Override
    public ResultVO<ComicVO> getComicDetail(Long id) {
        // 1. 查询漫画基本信息
        Comic comic = comicMapper.selectById(id);
        if (comic == null) {
            return ResultVO.error("漫画不存在");
        }

        // 2. 查询作者和分类信息
        User author = userMapper.selectById(comic.getAuthorId());
        Category category = categoryMapper.selectById(comic.getCategoryId());

        // 3. 封装漫画VO
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

        // 4. 查询章节列表（假设ChapterService有获取章节的方法）
        List<ChapterVO> chapters = chapterService.getChaptersByComicId(id);
        comicVO.setChapters(chapters);

        // 5. 遍历章节，查询每个章节的图片
        for (ChapterVO chapter : chapters) {
            List<Image> images = imageMapper.selectByChapterId(chapter.getId());
            chapter.setImages(images);  // 假设ChapterVO有images字段
        }

        return ResultVO.success(comicVO);
    }

    @Override
    public ResultVO<PageVO<ComicVO>> listComics(ComicQueryDTO queryDTO) {
        // 1. 计算分页参数
        Integer page = queryDTO.getPage();
        Integer size = queryDTO.getSize();
        Integer offset = (page - 1) * size;

        // 2. 查询符合条件的漫画总数
        Integer total = comicMapper.countByCondition(queryDTO);

        // 3. 查询漫画列表（带分页）
        List<Comic> comics = comicMapper.listByCondition(queryDTO, offset, size);

        // 4. 转换为VO列表
        List<ComicVO> comicVOList = comics.stream().map(comic -> {
            ComicVO vo = new ComicVO();
            vo.setId(comic.getId());
            vo.setTitle(comic.getTitle());
            vo.setCover(comic.getCover());
            // 补充作者、分类等信息（可通过Mapper关联查询优化）
            return vo;
        }).collect(Collectors.toList());

        // 5. 封装分页结果
        PageVO<ComicVO> pageVO = new PageVO<>();
        pageVO.setTotal(total);
        pageVO.setPage(page);
        pageVO.setSize(size);
        pageVO.setList(comicVOList);

        return ResultVO.success(pageVO);
    }
}