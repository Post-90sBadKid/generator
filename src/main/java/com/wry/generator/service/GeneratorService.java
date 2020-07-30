package com.wry.generator.service;


import com.github.pagehelper.PageInfo;
import com.wry.generator.dto.GeneratorCodeDTO;
import com.wry.generator.dto.GeneratorSearchDTO;


/**
 * <p>
 *
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
public interface GeneratorService {

    PageInfo findTableList(GeneratorSearchDTO generatorDTO);

    byte[] generatorCode(GeneratorCodeDTO generatorDTO);

}
