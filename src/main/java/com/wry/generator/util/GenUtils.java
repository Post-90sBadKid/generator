package com.wry.generator.util;


import com.wry.generator.entity.Column;
import com.wry.generator.entity.ProjectInfo;
import com.wry.generator.entity.Table;
import com.wry.generator.exception.CustomException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

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

    /**
     * 生成代码
     *
     * @param table        数据库表对象
     * @param projectInfo  项目配置信息
     * @param dataTypeMap  java类型和数据库类型对照
     * @param templatePath 模板路径
     * @param zip          压缩工具类
     */
    public static void generatorCode(Table table, ProjectInfo projectInfo, Map<String, String> dataTypeMap, String templatePath, ZipOutputStream zip) {


        boolean hasBigDecimal = false;
        boolean hasList = false;
        //表信息

        //表名转换成Java类名
        String className = StrUtils.tableToJava(table.getTableName(), projectInfo.getTablePrefix().split(","));
        table.setClassName(className);
        table.setClassJavaName(StringUtils.uncapitalize(className));

        //列信息
        List<Column> columsList = new ArrayList<>();

        for (Column col : table.getColumns()) {

            //列名转换成Java属性名
            String attrName = StrUtils.columnToJava(col.getColumnName());
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
            //是否是主键
            if (StrUtils.contains(col.getColumnName(), table.getKey().split(","))) {
                table.setPk(col);
            }
            columsList.add(col);
        }

        //没主键，则第一个字段为主键
        if (table.getPk() == null) {
            table.setPk(table.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = projectInfo.getMainPath();
        mainPath = StringUtils.isBlank(mainPath) ? "com.wry" : mainPath;
        String packageStr = mainPath;
        String packageName = projectInfo.getPackageName();
        if (!Objects.isNull(packageName) && !packageName.isEmpty()) {
            packageStr += "." + packageName;
        }
        String moduleName = projectInfo.getModuleName();
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("comments", table.getComments() == null || table.getComments().isEmpty() ? "" : table.getComments());
        map.put("pk", table.getPk());
        map.put("className", table.getClassName());
        map.put("classJavaName", table.getClassJavaName());
        map.put("pathName", table.getClassJavaName());
        map.put("columns", table.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("hasList", hasList);
        map.put("mainPath", mainPath);
        map.put("package", packageStr);
        map.put("moduleName", moduleName);
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
                zip.putNextEntry(new ZipEntry(getFileName(template, table.getClassName(), packageStr, moduleName)));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new CustomException("123", "渲染模板失败，表名：" + table.getTableName() + e);
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
    public static String getFileName(String template, String className, String packageStr, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packagePath)) {
            packagePath += packageStr.replace(".", File.separator) + File.separator;
        }
        if (StringUtils.isNotBlank(moduleName)) {
            packagePath += moduleName + File.separator;
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
