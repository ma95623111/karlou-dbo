package com.karlou.dbo.autoconfigure;

import org.apache.ibatis.session.Configuration;

/**
 * @author mzc
 * @mail 95623111@qq.com
 * @date 2020/9/11 08:46
 */
@FunctionalInterface
public interface ConfigurationCustomizer {

    /**
     * Customize the given a {@link Configuration} object.
     *
     * @param configuration the configuration object to customize
     */
    void customize(Configuration configuration);

}
