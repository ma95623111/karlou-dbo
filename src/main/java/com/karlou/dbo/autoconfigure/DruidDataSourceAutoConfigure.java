package com.karlou.dbo.autoconfigure;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

import com.karlou.dbo.config.*;
import com.karlou.dbo.properties.DruidStatProperties;
import com.karlou.dbo.properties.KarlouDboProperties;
import com.karlou.dbo.route.DynamicDataSourceRoute;
import com.karlou.dbo.wrapper.DruidDataSourceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/11 08:46
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class, KarlouDboProperties.class})
@Import({DruidSpringAopConfiguration.class,
        DruidStatViewServletConfiguration.class,
        DruidWebStatFilterConfiguration.class,
        DruidFilterConfiguration.class})
public class DruidDataSourceAutoConfigure {

    private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceAutoConfigure.class);

    @Bean(name = "defualtDataSource", initMethod = "init")
    public DataSource dataSource() {
        LOGGER.info("Druid Init DruidDataSource ");
        return new DruidDataSourceWrapper();
    }

    @Bean
    @Primary
    public DataSource setTargetDataSource(DataSource defualtDataSource) {
        DynamicDataSourceRoute dynamicDataSourceRoute = DynamicDataSourceRoute.getInstance();
        HashMap<Object, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("defualt", defualtDataSource);
        dynamicDataSourceRoute.setDefaultDataSource(defualtDataSource, objectHashMap);
        LOGGER.info("Kailou Init DynamiceDataDource  ");
        return dynamicDataSourceRoute;
    }

}
