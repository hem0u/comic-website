package com.comic.mapper;

import com.comic.dto.ComicQueryDTO;
import com.comic.entity.Comic;
import com.comic.vo.ComicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ComicMapper {
    // 新增漫画
    int insert(Comic comic);

    // 根据ID查询漫画
    Comic selectById(Long id);

    // 根据作者ID查询漫画列表
    List<Comic> selectByAuthorId(Long authorId);

    // 根据分类ID查询漫画列表
    List<Comic> selectByCategoryId(Integer categoryId);

    Integer countByCondition(ComicQueryDTO queryDTO);

    // 关键修改：添加 @Param 注解指定参数名
    List<ComicVO> listByCondition(
            @Param("queryDTO") ComicQueryDTO queryDTO,  // 命名为 queryDTO
            @Param("offset") Integer offset,            // 命名为 offset
            @Param("size") Integer size                 // 命名为 size
    );


}