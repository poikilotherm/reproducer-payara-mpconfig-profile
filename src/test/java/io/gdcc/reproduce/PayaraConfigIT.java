package io.gdcc.reproduce;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import java.io.File;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ArquillianExtension.class)
class PayaraConfigIT {

    @Deployment
    public static WebArchive getDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class);
        
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        war.addAsManifestResource(new File("src/main/resources/META-INF", "microprofile-config.properties"));
        war.addClass(TestMpConfig.class);
        
        return war;
    }
    
    @Inject
    TestMpConfig sut;
    
    @Test
    void mpcFileHasItButNoProfileThrows() {
        assertNotNull(sut);
        assertThrows(NoSuchElementException.class, () -> sut.getSetting2());
    }
    
    @Test
    void sysPropOverridesMpcFile() {
        assertEquals("override", sut.getSetting());
    }
    
    @Test
    void sysPropOverridesProfiledMpcFile() {
        assertEquals("override", sut.getSetting3());
    }
}
