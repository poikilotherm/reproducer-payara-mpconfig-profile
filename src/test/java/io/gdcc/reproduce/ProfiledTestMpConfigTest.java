package io.gdcc.reproduce;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SetSystemProperty(key = "mp.config.profile", value = "testprofile")
class ProfiledTestMpConfigTest {

    @Test
    void tryGetSettingWithProfile() {
        var sut = new TestMpConfig();
        assertEquals("barbecue", sut.getSetting2());
    }
    
    @Test
    void tryGetSettingSourceWithMPCFile() {
        var sut = new TestMpConfig();
        assertTrue(sut.getSetting2Source().contains("PropertiesConfigSource"));
    }
    
    @Test
    @SetSystemProperty(key = "test.setting.2", value = "sysprop")
    void tryGetSettingWithoutProfileFromSysProp() {
        var sut = new TestMpConfig();
        assertEquals("sysprop", sut.getSetting2());
    }
    
    @Test
    @SetSystemProperty(key = "test.setting.2", value = "sysprop")
    void tryGetSettingSourceWithSysProp() {
        var sut = new TestMpConfig();
        assertTrue(sut.getSetting2Source().contains("SysPropConfigSource"));
    }
}