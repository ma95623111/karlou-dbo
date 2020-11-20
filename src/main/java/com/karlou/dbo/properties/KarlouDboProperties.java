package com.karlou.dbo.properties;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("karlou.dbo.route")
@Data
public class KarlouDboProperties {
    private boolean enabled;
}
