package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.watch.Watch;
import org.springframework.samples.petclinic.watch.WatchRepository;
import org.springframework.samples.petclinic.watch.WatchType;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test3 {
    @Autowired(required = false)
    WatchRepository wr;
    
    @Test
    public void test3(){
        testInitialWatch();
        testInitialWatchTypes();
        testInitialWatchsLinkedWithWatchType();
        testInitialWatchsLinkedWithVet();
    }
    
    public void testInitialWatch(){
        List<Watch> products=wr.findAll();
        assertTrue(products.size()==2, "Exactly two Watchs should be present in the DB");

        Optional<Watch> p1=wr.findById(1);
        assertTrue(p1.isPresent(),"There should exist a Watch with id:1");
        assertEquals(p1.get().getDate(),LocalDate.of(2022, 01, 05), "The  date of the Watch with id:1 should be 05/01/2022");
        assertEquals(p1.get().getBeginTime(),LocalTime.of(07, 00), "The start time of the watch id:2 should be 07:00:00");
        assertEquals(p1.get().getFinishTime(),LocalTime.of(15, 00), "The end time of the watch id:2 should be 15:00:00");

        Optional<Watch> p2=wr.findById(2);
        assertTrue(p2.isPresent(),"There should exist a Watch with id:2");
        assertEquals(p2.get().getDate(),LocalDate.of(2022,1,4),"The date of the Watch with id:2 should be 04/01/2022");
        assertEquals(p2.get().getBeginTime(),LocalTime.of(16, 30), "The start time of the watch id:2 should be 16:30:00");
        assertEquals(p2.get().getFinishTime(),LocalTime.of(23, 30), "The end time of the watch id:2 should be 23:30:00");
    }

    public void testInitialWatchTypes()
    {
        List<WatchType> watchTypes = new ArrayList<WatchType>();
        try {
            Method findAllWatchTypes = WatchRepository.class.getDeclaredMethod("findAllWatchTypes");
            if(wr!=null){
                watchTypes = (List<WatchType>) findAllWatchTypes.invoke(wr);
            }else
                fail("The repository was not injected into the tests, its autowired value was null");
        } catch(NoSuchMethodException e) {
            fail("There is no method findAllWatchTypes in WatchRepository", e);
        } catch (IllegalAccessException e) {
            fail("There is no public method findAllWatchTypes in WatchRepository", e);
        } catch (IllegalArgumentException e) {
            fail("There is no method findAllWatchTypes() in WatchRepository", e);
        } catch (InvocationTargetException e) {
            fail("There is no method findAllWatchTypes() in WatchRepository", e);
        }

        assertTrue(watchTypes.size()==2,"Exactly two Watch types should be present in the DB");

        for(WatchType v:watchTypes) {
            if(v.getId()==1){
                assertEquals(v.getName(),"Localised watch","The name of the Watch type with id:1 should be 'Localised watch'");
                assertTrue(v.getOvertime(),"The overtime of the Watch type with id:1 is not correct");
            }else if(v.getId()==2){
                assertEquals(v.getName(),"Telephonic watch","The name of the Watch type with id:2 should be 'Telephonic watch'");
                assertFalse(v.getOvertime(),"The overtime of the Watch type with id:2 is not correct");
            }else {
                fail("The id of the Watch types should be either 1 or 2");
            }
        }
    }
    
    private void testInitialWatchsLinkedWithWatchType() {
        Field watchType;
        WatchType ft;

        try {
            watchType = Watch.class.getDeclaredField("type");
            watchType.setAccessible(true);
        
            Optional<Watch> f1=wr.findById(1);
            assertTrue(f1.isPresent(),"Watch with id:1 not found");
            ft = (WatchType) watchType.get(f1.get());
            assertNotNull(ft,"The watch with id:1 has not a watch type associated");
            assertEquals(2,ft.getId(),"The id of the watch type associated to the watch with id:1 is not 2");

            Optional<Watch> f2=wr.findById(2);
            assertTrue(f2.isPresent(),"Watch with id:2 not found");
            ft = (WatchType) watchType.get(f2.get());
            assertNotNull(ft,"The watch with id:2 has not a watch type associated");
            assertEquals(1, ft.getId(),"The id of the watch type associated to the watch with id:2 is not 1");
        } catch (NoSuchFieldException e) {
            fail("The Watch class should have an attribute called watchType that is not present: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("The Watch class should have an attribute called watchType that is not present: "+e.getMessage());
        } catch (IllegalAccessException e) {
            fail("The Watch class should have an attribute called watchType that is not present: "+e.getMessage());
        }
    }

    private void testInitialWatchsLinkedWithVet() {        
        Vet v;

        try {
            Field vet = Watch.class.getDeclaredField("vet");
            vet.setAccessible(true);
        
            Optional<Watch> w1=wr.findById(1);
            assertTrue(w1.isPresent(),"Watch with id:1 not found");
            v = (Vet) vet.get(w1.get());
            assertNotNull(v,"The watch with id:1 has not a vet associated");
            assertEquals(1, v.getId(),"The id of the vet associated to the watch with id:1 is not 1");

            Optional<Watch> f2=wr.findById(2);
            assertTrue(f2.isPresent(),"Watch with id:2 not found");
            v = (Vet) vet.get(f2.get());
            assertNotNull(v,"The watch with id:2 has not a pet associated");
            assertEquals(2, v.getId(),"The id of the pet associated to the watch with id:2 is not 2");
        } catch (NoSuchFieldException e) {
            fail("The Watch class should have an attribute called pet that is not present: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("The Watch class should have an attribute called pet that is not present: "+e.getMessage());
        } catch (IllegalAccessException e) {
            fail("The Watch class should have an attribute called pet that is not present: "+e.getMessage());
        }
    }    
}
