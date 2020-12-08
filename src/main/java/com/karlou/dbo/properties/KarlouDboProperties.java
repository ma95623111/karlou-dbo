package com.karlou.dbo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "karlou.dbo")
public class KarlouDboProperties {
    //----是否自动配置
    private boolean enabled;
    //----检索第三方jar中自定义Injectors
    private String autoScannerInjectors;

    public KarlouDboProperties() {
        System.out.println("ss");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAutoScannerInjectors() {
        return autoScannerInjectors;
    }

    public void setAutoScannerInjectors(String autoScannerInjectors) {
        this.autoScannerInjectors = autoScannerInjectors;
    }
}
