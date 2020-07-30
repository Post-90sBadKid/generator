package com.wry.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wry.generator.entity.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/24
 */
@Mapper
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {
    List<ProjectInfo> selectProjectInfoList(@Param("searchValue") String searchValue);
}