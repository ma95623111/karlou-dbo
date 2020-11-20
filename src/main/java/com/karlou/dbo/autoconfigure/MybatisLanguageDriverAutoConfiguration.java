package com.karlou.dbo.autoconfigure;

import org.apache.ibatis.scripting.LanguageDriver;
import org.mybatis.scripting.freemarker.FreeMarkerLanguageDriver;
import org.mybatis.scripting.freemarker.FreeMarkerLanguageDriverConfig;
import org.mybatis.scripting.thymeleaf.ThymeleafLanguageDriver;
import org.mybatis.scripting.thymeleaf.ThymeleafLanguageDriverConfig;
import org.mybatis.scripting.velocity.VelocityLanguageDriver;
import org.mybatis.scripting.velocity.VelocityLanguageDriverConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/11 08:46
 */
@Configuration
@ConditionalOnClass(LanguageDriver.class)
public class MybatisLanguageDriverAutoConfiguration {

    private static final String CONFIGURATION_PROPERTY_PREFIX = "mybatis.scripting-language-driver";

    /**
     * Configuration class for mybatis-freemarker 1.1.x or under.
     */
    @Configuration
    @ConditionalOnClass(FreeMarkerLanguageDriver.class)
    @ConditionalOnMissingClass("org.mybatis.scripting.freemarker.FreeMarkerLanguageDriverConfig")
    public static class LegacyFreeMarkerConfiguration {
        @Bean
        @ConditionalOnMissingBean
        FreeMarkerLanguageDriver freeMarkerLanguageDriver() {
            return new FreeMarkerLanguageDriver();
        }
    }

    /**
     * Configuration class for mybatis-freemarker 1.2.x or above.
     */
    @Configuration
    @ConditionalOnClass({FreeMarkerLanguageDriver.class, FreeMarkerLanguageDriverConfig.class})
    public static class FreeMarkerConfiguration {
        @Bean
        @ConditionalOnMissingBean
        FreeMarkerLanguageDriver freeMarkerLanguageDriver(FreeMarkerLanguageDriverConfig config) {
            return new FreeMarkerLanguageDriver(config);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties(CONFIGURATION_PROPERTY_PREFIX + ".freemarker")
        public FreeMarkerLanguageDriverConfig freeMarkerLanguageDriverConfig() {
            return FreeMarkerLanguageDriverConfig.newInstance();
        }
    }

    /**
     * Configuration class for mybatis-velocity 2.0 or under.
     */
    @Configuration
    @ConditionalOnClass(org.mybatis.scripting.velocity.Driver.class)
    @ConditionalOnMissingClass("org.mybatis.scripting.velocity.VelocityLanguageDriverConfig")
    @SuppressWarnings("deprecation")
    public static class LegacyVelocityConfiguration {
        @Bean
        @ConditionalOnMissingBean
        org.mybatis.scripting.velocity.Driver velocityLanguageDriver() {
            return new org.mybatis.scripting.velocity.Driver();
        }
    }

    /**
     * Configuration class for mybatis-velocity 2.1.x or above.
     */
    @Configuration
    @ConditionalOnClass({VelocityLanguageDriver.class, VelocityLanguageDriverConfig.class})
    public static class VelocityConfiguration {
        @Bean
        @ConditionalOnMissingBean
        VelocityLanguageDriver velocityLanguageDriver(VelocityLanguageDriverConfig config) {
            return new VelocityLanguageDriver(config);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties(CONFIGURATION_PROPERTY_PREFIX + ".velocity")
        public VelocityLanguageDriverConfig velocityLanguageDriverConfig() {
            return VelocityLanguageDriverConfig.newInstance();
        }
    }

    @Configuration
    @ConditionalOnClass(ThymeleafLanguageDriver.class)
    public static class ThymeleafConfiguration {
        @Bean
        @ConditionalOnMissingBean
        ThymeleafLanguageDriver thymeleafLanguageDriver(ThymeleafLanguageDriverConfig config) {
            return new ThymeleafLanguageDriver(config);
        }

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties(CONFIGURATION_PROPERTY_PREFIX + ".thymeleaf")
        public ThymeleafLanguageDriverConfig thymeleafLanguageDriverConfig() {
            return ThymeleafLanguageDriverConfig.newInstance();
        }

        // This class provides to avoid the https://github.com/spring-projects/spring-boot/issues/21626 as workaround.
        @SuppressWarnings("unused")
        private static class MetadataThymeleafLanguageDriverConfig extends ThymeleafLanguageDriverConfig {

            @ConfigurationProperties(CONFIGURATION_PROPERTY_PREFIX + ".thymeleaf.dialect")
            @Override
            public DialectConfig getDialect() {
                return super.getDialect();
            }

            @ConfigurationProperties(CONFIGURATION_PROPERTY_PREFIX + ".thymeleaf.template-file")
            @Override
            public TemplateFileConfig getTemplateFile() {
                return super.getTemplateFile();
            }

        }

    }

}
