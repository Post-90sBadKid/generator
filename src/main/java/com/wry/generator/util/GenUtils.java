package com.wry.generator.util;


import com.wry.generator.entity.Column;
import com.wry.generator.entity.DataType;
import com.wry.generator.entity.ProjectInfo;
import com.wry.generator.entity.Table;
import com.wry.generator.exception.CustomException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GenUtils {

    private static String currentTableName;

    /**
     * 生成代码
     *
     * @param table        数据库表对象
     * @param columns      表的列信息
     * @param projectInfo  项目配置信息
     * @param dataTypeMap  java类型和数据库类型对照
     * @param templatePath 模板路径
     * @param zip          压缩工具类
     */
    public static void generatorCode(Map<String, String> table, List<Map<String, String>> columns, ProjectInfo projectInfo, Map<String, String> dataTypeMap, String templatePath, ZipOutputStream zip) {

        boolean hasBigDecimal = false;
        boolean hasList = false;
        //表信息
        Table Table = new Table();
        Table.setTableName(table.get("tableName"));
        Table.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(Table.getTableName(), projectInfo.getTablePrefix().split(","));
        Table.setClassName(className);
        Table.setClassJavaName(StringUtils.uncapitalize(className));

        //列信息
        List<Column> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            Column col = new Column();
            col.setColumnName(column.get("columnName"));
            col.setDataType(column.get("dataType"));
            col.setComments(column.get("columnComment"));
            col.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(col.getColumnName());
            col.setAttrName(attrName);
            col.setAttrJavaName(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = dataTypeMap.get(col.getDataType());
            col.setAttrType(attrType);


            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            if (!hasList && "array".equals(col.getExtra())) {
                hasList = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && Table.getPk() == null) {
                Table.setPk(col);
            }

            columsList.add(col);
        }
        Table.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (Table.getPk() == null) {
            Table.setPk(Table.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = projectInfo.getMainPath();
        mainPath = StringUtils.isBlank(mainPath) ? "com.wry" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", Table.getTableName());
        map.put("comments", Table.getComments());
        map.put("pk", Table.getPk());
        map.put("className", Table.getClassName());
        map.put("classname", Table.getClassJavaName());
        map.put("pathName", Table.getClassJavaName().toLowerCase());
        map.put("columns", Table.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("hasList", hasList);
        map.put("mainPath", mainPath);
        map.put("package", mainPath + projectInfo.getPackageName());
        map.put("moduleName", projectInfo.getModuleName());
        map.put("author", projectInfo.getAuthor());
        map.put("email", projectInfo.getEmail());
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates(templatePath);
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, Table.getClassName(), projectInfo.getPackageName(), projectInfo.getModuleName())));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new CustomException("123", "渲染模板失败，表名：" + Table.getTableName() + e);
            }
        }
    }

    public static List<String> getTemplates(String templatePath) {
        List<String> templates = new ArrayList<>();
        //resource路径
        String resourcePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //模板路径
        String srcTemplatePath = "template" + File.separator + templatePath;
        File file = new File(resourcePath + srcTemplatePath);
        if (!file.isDirectory()) {
            throw new CustomException("2", "模板路径填写错误！");
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                templates.add(srcTemplatePath + File.separator + filelist[i]);
            }
        }

        return templates;
    }

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
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new CustomException("3", "获取配置文件失败，" + e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }
        if (template.contains("Entity.java.vm")) {
            return packagePath + "entity" + File.separator + className + ".java";
        }

        if (template.contains("Mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }
        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Mapper.xml";
        }
        if (template.contains("Dao.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "dao" + File.separator + moduleName + File.separator + className + "Dao.xml";
        }

        if (template.contains("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }

        if (template.contains("index.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + ".vue";
        }

        if (template.contains("add-or-update.vue.vm")) {
            return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views" + File.separator + "modules" +
                    File.separator + moduleName + File.separator + className.toLowerCase() + "-add-or-update.vue";
        }

        return null;
    }

    private static String splitInnerName(String name) {
        name = name.replaceAll("\\.", "_");
        return name;
    }
}
