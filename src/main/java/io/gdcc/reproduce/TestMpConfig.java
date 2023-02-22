package io.gdcc.reproduce;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class TestMpConfig {
    
    Config config = ConfigProvider.getConfig();
    
    public String getSetting() {
        return config.getValue("test.setting", String.class);
    }
    
    public String getSetting2() {
        return config.getValue("test.setting.2", String.class);
    }
    
    public String getSetting3() {
        return config.getValue("test.setting.3", String.class);
    }
    
    public String getSettingSource() {
        return config.getConfigValue("test.setting").getSourceName();
    }
    
    public String getSetting2Source() {
        return config.getConfigValue("test.setting.2").getSourceName();
    }
    
    public String getSetting3Source() {
        return config.getConfigValue("test.setting.3").getSourceName();
    }
    
}
