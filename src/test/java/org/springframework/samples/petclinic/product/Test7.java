package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.watch.WatchService;
import org.springframework.samples.petclinic.watch.WatchType;
import org.springframework.samples.petclinic.watch.WatchTypeFormatter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class Test7 {

    @Autowired
    WatchTypeFormatter formatter;

    @Autowired
    WatchService fs;

    @Test
    public void test7(){
        validateFindWatchTypeByName();
        validateNotFoundWatchTypeByName();
        testFormatterIsInjected();
        testFormatterObject2String();
        testFormatterString2Object();
        testFormatterString2ObjectNotFound();
    }
    
    private void testFormatterIsInjected(){
        assertNotNull(formatter);
    }
    
    private void testFormatterObject2String(){
        WatchType watchType=new WatchType();
        watchType.setName("Prueba");
        String result=formatter.print(watchType, null);
        assertEquals("Prueba",result, "The method print of the formatter is not working properly.");
    }
    
    private void testFormatterString2Object(){
        String name="Localised watch";
        WatchType watchType;
        try {
            watchType = formatter.parse(name, null);
            assertNotNull(watchType, "The method parse of the formatter is not working properly.");
            assertEquals(name, watchType.getName(), "The method parse of the formatter is not working properly.");
        } catch (ParseException e) {           
            fail("The method parse of the formatter is not working properly.", e);
        }        
    }
    
    private void testFormatterString2ObjectNotFound(){
        String name="This is not a watch type";
        assertThrows(ParseException.class, () -> formatter.parse(name, null), "The method parse of the formatter is not working properly.");          
    }

    private void validateFindWatchTypeByName(){
        String typeName="Localised watch";
        WatchType watchType=fs.getWatchType(typeName);
        assertNotNull(watchType, "getWatchType by name is returning null");
        assertEquals(typeName,watchType.getName(), "getWatchType by name is not returning an existing feeding type");
    }
    
    private void validateNotFoundWatchTypeByName(){
        String typeName="This is not a valid watch type name";
        WatchType feedingType=fs.getWatchType(typeName);
        assertNull(feedingType, "getWatchType by name is returning a watch type that does not exist");
    }

}