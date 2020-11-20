package com.karlou.dbo.test.enableAuto;

import com.alibaba.druid.pool.DruidDataSource;
import com.karlou.dbo.builder.DruidDataSourceBuilder;
import com.karlou.dbo.handle.DynamicDataSourceHandle;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DyDataSource {

    @ConfigurationProperties("spring.datasource.druid.karlou")
    @Bean(name = "karlou")
    public DataSource dataSourceTwo() {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        DynamicDataSourceHandle.setTargetDataSource("karlou", druidDataSource);

        return druidDataSource;
    }

}
