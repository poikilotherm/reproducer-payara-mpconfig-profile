package io.gdcc.reproduce;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import static org.junit.jupiter.api.Assertions.*;

class TestMpConfigTest {
    
    @Test
    void tryGetSettingWithoutProfile() {
        var sut = new TestMpConfig();
        assertEquals("foobar", sut.getSetting());
    }
    
    @Test
    void tryGetSettingSourceWithMPCFile() {
        var sut = new TestMpConfig();
        assertTrue(sut.getSettingSource().contains("PropertiesConfigSource"));
    }
    
    @Test
    @SetSystemProperty(key = "test.setting", value = "sysprop")
    void tryGetSettingWithoutProfileFromSysProp() {
        var sut = new TestMpConfig();
        assertEquals("sysprop", sut.getSetting());
    }
    
    @Test
    @SetSystemProperty(key = "test.setting", value = "sysprop")
    void tryGetSettingSourceWithSysProp() {
        var sut = new TestMpConfig();
        assertTrue(sut.getSettingSource().contains("SysPropConfigSource"));
    }
}