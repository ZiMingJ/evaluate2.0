package com.eddy.evaluate.config;

import com.baomidou.mybatisplus.extension.spring.MybatisMapperRefresh;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;


@SpringBootConfiguration
@ConditionalOnProperty(name = "mybatis-plus.global-config.refresh-mapper", havingValue = "true")
public class DevFlushMyBatisXml {
    /**
     * 热部署
     *
     * @param factory
     * @return
     */
    @Bean
    public MybatisMapperRefresh mapperRefresh(@Autowired SqlSessionFactory factory) throws IOException {
        System.out.println("  __  __                                           ____                 \n" +
                " |  \\/  |   __ _   _ __    _ __     ___   _ __    |  _ \\    ___  __   __\n" +
                " | |\\/| |  / _` | | '_ \\  | '_ \\   / _ \\ | '__|   | | | |  / _ \\ \\ \\ / /\n" +
                " | |  | | | (_| | | |_) | | |_) | |  __/ | |      | |_| | |  __/  \\ V / \n" +
                " |_|  |_|  \\__,_| | .__/  | .__/   \\___| |_|      |____/   \\___|   \\_/  \n" +
                "                  |_|     |_|                                           \n");
        ClassPathResource resource = new ClassPathResource("mappers");
        File root = resource.getFile();

        List<File> fileList = Lists.newArrayList();

        addXmlFile(fileList, root);

        Resource[] res = new Resource[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            res[i] = new FileUrlResource(fileList.get(i).getAbsolutePath());
        }
        MybatisMapperRefresh refresh = new MybatisMapperRefresh(res, factory, 5, 2, true);
        return refresh;
    }


    public void addXmlFile(List<File> list, File root) {
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                addXmlFile(list, file);
            } else {
                if (file.getName().endsWith(".xml")) {
                    list.add(file);
                }
            }
        }
    }

}
