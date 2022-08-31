package cn.hyy.common.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

public class DBGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://.com:8306/111?useUnicode=true&useSSL=false&characterEncoding=utf8", "111", "111")
                .globalConfig(builder -> {
                    builder.author("hyy") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir() // 生成后不打开目录
//                            .dateType(DateType.ONLY_DATE) // 设置时间格式,默认LocalDateTime
                            .outputDir(System.getProperty("user.dir") + "/activity-db/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.hyy.activity.db") // 设置父包名
                            .entity("domain")
                            .mapper("mapper")
                            .service("temp.service")
                            .serviceImpl("temp.service.impl")
                            .xml("temp.xml");
//                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/activity-db/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("md_test") // 设置需要生成的表名,逗号分隔
                            .addTablePrefix("md_", "c_") // 设置过滤表前缀
                            .enableCapitalMode()
                            .entityBuilder().enableLombok().fileOverride(); // 实体类进行覆盖，其他不覆盖
                })
                .templateConfig(builder -> {
                    builder.controller(""); // 不生成controller文件
                })
                .execute();
    }
}
