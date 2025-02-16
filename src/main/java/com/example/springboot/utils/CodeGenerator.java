package com.example.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

/**
 * Code generator
 * by ada
 * @since 2023-05-22
 */

public class CodeGenerator {
    public static void main(String[] args){
           generate();
    }
    private static void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/yang?serverTimezone=GMT%2b2", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("ada")
                            .enableSwagger()
                            .fileOverride()
                            .outputDir("C:\\Users\\TFG2023\\springboot\\src\\main\\java\\");
                })
                .packageConfig(builder -> {
                    builder.parent("com.example.springboot")
                            .moduleName(null)
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "C:\\Users\\TFG2023\\springboot\\src\\main\\resources\\mapper\\"));
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
//                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle()
                            .enableRestStyle();
                    builder.addInclude("sys_product")
                            .addTablePrefix("t_", "sys_");
                })
//                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
