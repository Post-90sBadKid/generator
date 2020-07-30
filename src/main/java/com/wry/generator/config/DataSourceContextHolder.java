package com.wry.generator.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.wry.generator.exception.CustomException;
import com.wry.generator.result.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.lang.reflect.Method;

/**
 * <p>
 * 使用ThreadLocal切换当前使用的数据源
 * </p>
 *
 * @author wangruiyu
 * @since 2020/7/20
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setPollName(String pollName) {
        contextHolder.set(pollName);
        //切换分页插件方言
        try {
            DynamicDataSource dynamicDataSource = SpringContextBeanService.getBean(DynamicDataSource.class);
            Method determineTargetDataSourceMethod = BeanUtils.findDeclaredMethod(AbstractRoutingDataSource.class, "determineTargetDataSource");
            determineTargetDataSourceMethod.setAccessible(true);
            String type = ((DruidDataSource) determineTargetDataSourceMethod.invoke(dynamicDataSource)).getDbType();
            //切换分页方言
            SpringContextBeanService.getBean(PaginationInterceptor.class).setDbType(DbType.getDbType(type));
        } catch (Exception e) {
            throw new CustomException(ResultEnum.DIALECT_SWITCHING_FAILED);
        }
    }

    public static String getPollName() {
        return contextHolder.get();
    }

    public static void clearPollName() {
        contextHolder.remove();
    }
}