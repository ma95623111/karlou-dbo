package com.karlou.dbo.test.enableAuto;

import com.alibaba.druid.pool.DruidDataSource;
import com.karlou.dbo.builder.DruidDataSourceBuilder;
import com.karlou.dbo.handle.DynamicDataSourceHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DyDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DyDataSource.class);



    @Bean(name = "karlou")
    @ConfigurationProperties("spring.datasource.druid.karlou")
    public DataSource dataSourceTwo() {
        LOGGER.info("DyDataSource init");
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        DynamicDataSourceHandle.setTargetDataSource("karlou", druidDataSource);

        return druidDataSource;
    }

}
