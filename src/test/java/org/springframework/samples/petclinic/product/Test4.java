package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;

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
public class Test4 {
    @Autowired(required = false)
    WatchService ws;

    @Autowired
    VetService vs;

    @Autowired
    EntityManager em;

    @Test public void test4() throws UnfeasibleWatchException{
        WatchServiceIsInjected();
        WatchServiceCanGetWatchs();
        WatchServiceCanGetWatchTypes();
        WatchServiceCanSaveWatchs();
        checkTransactional("save", Watch.class);
        checkTransactional("getAllWatchTypes");
        checkTransactional("getAll");
    }
    
    public void WatchServiceIsInjected() {
        assertNotNull(ws,"WatchService was not injected by spring");       
    }
    
    public void WatchServiceCanGetWatchs(){
        assertNotNull(ws,"WatchService was not injected by spring");
        List<Watch> Watchs=ws.getAll();
        assertNotNull(Watchs,"The list of Watchs found by the WatchService was null");
        assertFalse(Watchs.isEmpty(),"The list of Watchs found by the WatchService was empty");       
    }

    /**
     * 
     */
    private void WatchServiceCanGetWatchTypes() {
        assertNotNull(ws,"WatchService was not injected by spring");
        List<WatchType> WatchTypes=ws.getAllWatchTypes();
        assertNotNull(WatchTypes,"The list of Watch types found by the WatchService was null");
        assertFalse(WatchTypes.isEmpty(),"The list of Watch types found by the WatchService was empty");       
    }

    private void WatchServiceCanSaveWatchs() throws UnfeasibleWatchException {        
        Watch f4=new Watch();
        f4.setId(7);
        f4.setDate(LocalDate.of(2021, 12, 31));
        f4.setBeginTime(LocalTime.of(10,00));
        f4.setFinishTime(LocalTime.of(15,00));

        try{
            Field WatchTypeField = Watch.class.getDeclaredField("type");
            WatchTypeField.setAccessible(true);
            Field vetField = Watch.class.getDeclaredField("vet");
            vetField.setAccessible(true);
            WatchType ft = em.find(WatchType.class, 2);
            Vet v = vs.findVets().iterator().next();
            WatchTypeField.set(f4, ft);
            vetField.set(f4, v);
            Watch f4Saved = ws.save(f4);
            assertNotNull(f4Saved, "Watchs cannot be saved by WatchService");
        }catch(Exception ex){
            fail("Exception thrown while saving watch:"+ex.getMessage() );
        }        
        
        
    }

    private void checkTransactional(String methodName, Class<?>... parameterTypes) {
        Method method=null;
        try {
            method = WatchService.class.getDeclaredMethod(methodName, parameterTypes);
            Transactional transactionalAnnotation=method.getAnnotation(Transactional.class);
            assertNotNull(transactionalAnnotation,"The method "+methodName+" is not annotated as transactional");
        } catch (NoSuchMethodException e) {
            fail("WatchService does not have a "+methodName+" method");
        } catch (SecurityException e) {
            fail(methodName+" method is not accessible in WatchService");
        }
    }    



}
