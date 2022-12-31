package org.springframework.samples.petclinic.product;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetService;
import org.springframework.samples.petclinic.watch.UnfeasibleWatchException;
import org.springframework.samples.petclinic.watch.Watch;
import org.springframework.samples.petclinic.watch.WatchService;
import org.springframework.samples.petclinic.watch.WatchType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test6 {

    @Autowired
    WatchService ws;
    @Autowired
    VetService vs;

    @Test
    public void test6(){
        testSaveWatchSuccessfull();
        testSaveUnfeasibleWatch();
        testTransactionalRollback();
    }

    @Transactional
    private void testSaveWatchSuccessfull()
    {
        Vet vet=vs.getById(1); 
        WatchType wt=ws.getAllWatchTypes().get(0); 
        Watch f = new Watch();
        setVet(f, vet);
        f.setDate(LocalDate.of(2022,11,11));
        f.setBeginTime(LocalTime.of(8, 0));
        f.setFinishTime(LocalTime.of(15, 0));
        setWatchType(f, wt);
        try {
            ws.save(f);
        } catch (Exception e) {
            fail("The excepcion should not be thrown, the watch is unfeasible!");
        }
    }

    @Transactional
    private void testSaveUnfeasibleWatch()   {
        Vet pet=vs.getById(1);
        WatchType ft=ws.getAllWatchTypes().get(0); 
        Watch f = new Watch();
		setVet(f, pet);
        f.setDate(LocalDate.of(2022,11,11));
        f.setBeginTime(LocalTime.of(9, 0)); // This time is not compatible with the previous watch
        f.setFinishTime(LocalTime.of(12, 0));
        setWatchType(f, ft);
        // Thus, the save should throw an exception:
        assertThrows(UnfeasibleWatchException.class,
            ()-> ws.save(f));
    }

    private void testTransactionalRollback() {
        Method save=null;
        try {
            save = WatchService.class.getDeclaredMethod("save", Watch.class);
        } catch (NoSuchMethodException e) {
           fail("WatchService does not have a save method");
        } catch (SecurityException e) {
            fail("save method is not accessible in WatchService");
        }
        Transactional transactionalAnnotation=save.getAnnotation(Transactional.class);
        assertNotNull(transactionalAnnotation,"The method save is not annotated as transactional");
        List<Class<? extends Throwable>> exceptionsWithRollbackFor=Arrays.asList(transactionalAnnotation.rollbackFor());
        assertTrue(exceptionsWithRollbackFor.contains(UnfeasibleWatchException.class));
    }

    private void setWatchType(Watch f, WatchType ft) {
        try {
            Field watchType = Watch.class.getDeclaredField("type");
            watchType.setAccessible(true);
            watchType.set(f, ft);
        } catch (NoSuchFieldException e) {
            fail("The Watch class should have an attribute called watchType that is not present: ", e);
        } catch (IllegalArgumentException e) {
            fail("The Watch class should have an attribute called watchType that is not present: ", e);
        } catch (IllegalAccessException e) {
            fail("The Watch class should have an attribute called watchType that is not present: ", e);
        }        
    }

    private void setVet(Watch f, Vet v) {
        try {
            Field vet = Watch.class.getDeclaredField("vet");
            vet.setAccessible(true);
            vet.set(f, v);
        } catch (NoSuchFieldException e) {
            fail("The Watch class should have an attribute called vet that is not present: ", e);
        } catch (IllegalArgumentException e) {
            fail("The Watch class should have an attribute called vet that is not present: ", e);
        } catch (IllegalAccessException e) {
            fail("The Watch class should have an attribute called vet that is not present: ", e);
        }        
    }

}

