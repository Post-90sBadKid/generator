package com.wry.generator.config;


import com.wry.generator.exception.CustomException;
import com.wry.generator.mapper.*;
import com.wry.generator.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 代码生成器Mapper工厂，根据数据库类型，返回对应的 Mapper接口
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
@Component
public class GeneratorMapperFactory {

    @Autowired
    private MySQLGeneratorMapper mySQLGeneratorMapper;
    @Autowired
    private OracleGeneratorMapper oracleGeneratorMapper;
    @Autowired
    private SQLServerGeneratorMapper sqlServerGeneratorMapper;
    @Autowired
    private PostgreSQLGeneratorMapper postgreSQLGeneratorMapper;


    public GeneratorMapper getGeneratorMapper(String dbType, String pollName) {
        if ("mysql".equalsIgnoreCase(dbType)) {
            return mySQLGeneratorMapper;
        } else if ("oracle".equalsIgnoreCase(dbType)) {
            return oracleGeneratorMapper;
        } else if ("sqlserver".equalsIgnoreCase(dbType)) {
            return sqlServerGeneratorMapper;
        } else if ("postgresql".equalsIgnoreCase(dbType)) {
            return postgreSQLGeneratorMapper;
        } else {
            throw new CustomException(ResultEnum.CURRENT_DATASOURCE_NOT_SUPPORTED);
        }
    }
}
