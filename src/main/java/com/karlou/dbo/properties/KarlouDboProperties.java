package com.karlou.dbo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("karlou.dbo.route")
public class KarlouDboProperties {
    private boolean enabled;
}
