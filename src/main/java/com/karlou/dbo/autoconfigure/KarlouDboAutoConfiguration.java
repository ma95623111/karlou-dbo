package com.karlou.dbo.autoconfigure;

import com.karlou.dbo.injector.SqlInjector;
import com.karlou.dbo.util.KarlouUtil;
import com.karlou.dbo.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.*;

/**
 * 自动扫描根包下（含jar中符合条件的类，并注入到bean中）
 *
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/25 09:08
 */
@org.springframework.context.annotation.Configuration
public class KarlouDboAutoConfiguration {
    private final static Logger logger = LoggerFactory.getLogger(KarlouDboAutoConfiguration.class);

    /**
     * 自动注入SqlInjector的实现类bean
     */
    public static class AutoConfiguredRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

        private BeanFactory beanFactory;
        private final Set<Class> res = new HashSet<>();
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            /**
             * 因springboot的 Application类会自动扫描加载其package 及其子package的类,但是其他package 或 jar中的类不会自动被扫描到
             * 所以测试阶段 暂时屏蔽自动获取路径
             */
            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                logger.info("Could not determine auto-configuration package, automatic SqlInjector scanning disabled.");
                return;
            }

            logger.info("Searching for SqlInjector SubClass");
            //----包加载
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
//            List<String> packages = new ArrayList<>();
            packages.add("com.karlou.dbo");
            if (logger.isDebugEnabled()) {
                packages.forEach(pkg -> logger.debug("Using auto-configuration base package '{}'", pkg));
            }
            packages.stream().forEach(k -> {
                KarlouUtil.getJarPackageClass(k, SqlInjector.class,this.res);

            });
            Iterator<Class> $sqli = this.res.iterator();
            while ($sqli.hasNext()) {
                Class nClass = $sqli.next();
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(nClass);
                logger.debug("injector bean :" + nClass.getName());
                registry.registerBeanDefinition(nClass.getName(), builder.getBeanDefinition());
                Object bean = this.beanFactory.getBean(nClass.getName());
                SpringContextUtil.put(nClass, bean);
            }

        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }
    }

    /**
     * 自动扫描入口
     */
    @org.springframework.context.annotation.Configuration
    @Import(AutoConfiguredRegistrar.class)
    public static class AutuConfigurationEntrance implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            logger.debug(
                    "Not found SqlInjector subClass for configuration.");
        }

    }
}
