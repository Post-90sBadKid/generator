package com.wry.generator.util;

import org.apache.commons.lang.WordUtils;

/**
 * <p>
 * 字符串操作类
 * </p>
 *
 * @author wangruiyu
 * @since 2020/8/7
 */
public class StrUtils {
    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String[] tablePrefixArray) {
        if (null != tablePrefixArray && tablePrefixArray.length > 0) {
            for (String tablePrefix : tablePrefixArray) {
                tableName = tableName.replace(tablePrefix, "");
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 判断是否包含
     *
     * @param str
     * @param keywords
     * @return
     */
    public static boolean contains(String str, String... keywords) {
        if (str == null) {
            return false;
        }
        for (String keyword : keywords) {
            if (str.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
