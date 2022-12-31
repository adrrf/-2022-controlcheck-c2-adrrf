package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.pet.PetRepository;
import org.springframework.samples.petclinic.watch.Watch;
import org.springframework.samples.petclinic.watch.WatchRepository;
import org.springframework.samples.petclinic.watch.WatchType;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
public class Test2 {
    @Autowired(required = false)
    WatchRepository fr;
    @Autowired
    PetRepository pr;
    @Autowired(required = false)
    EntityManager em;
    
    @Test
    public void test2(){
        entityExists();
        repositoryContainsMethod();
        testConstraints();
        testAnnotations();
        testAnnotationsWatch();
        testConstraintsWatch(); 
    }

   
    public void entityExists() {
        WatchType p=new WatchType();
        p.setId(7);
        p.setName("Telephonic");        
        p.setOvertime(false);
    }  
    
    public void repositoryContainsMethod() {
        try {
            Method findAllWatchTypes = WatchRepository.class.getDeclaredMethod("findAllWatchTypes");
            if(fr!=null){
                List<WatchType> pts= (List<WatchType>) findAllWatchTypes.invoke(fr);
                assertNotNull(pts,"We can not query all the Watch types through the repository");
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
    }

    @Transactional
    public void testConstraints(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        

        

        
        WatchType watchType=new WatchType();       
        watchType.setName("ja");
        watchType.setOvertime(false);        
        assertFalse(validator.validate(watchType).isEmpty(), "It should not allow names shorter than 5");

        watchType=new WatchType();       
        watchType.setName("En un lugar de la mancha, de cuyo nombre no quiero acordarme, no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor.");
        watchType.setOvertime(false);
        
        assertFalse(validator.validate(watchType).isEmpty(), "It should not allow names longer than 30");

        watchType=new WatchType();       
        watchType.setOvertime(false);
        
        assertFalse(validator.validate(watchType).isEmpty(), "It should not allow empty names");  

        watchType=new WatchType();
        watchType.setName("Localised");        
        watchType.setOvertime(true);        
        em.persist(watchType);
        
/*      WatchType ft2=new WatchType();       
        ft2.setName("Localised");        
        ft2.setOvertime(true);        
        assertThrows(Exception.class,()->em.persist(ft2),"It should not allow two Watch types with the same name");        */
    }

    void testAnnotations(){
        try{
            Field nameField = WatchType.class.getDeclaredField("name");
            Column annotationColumn = nameField.getAnnotation(Column.class);
            assertNotNull(annotationColumn,"The name property is not properly annotated to make it unique in the DB");
            assertTrue(annotationColumn.unique(), "The name property is not properly annotated to make it unique in the DB");
        }catch(NoSuchFieldException ex){
            fail("The WatchType class should have a field that is not present: "+ex.getMessage());
        }
    }
    
    void testAnnotationsWatch(){
        try{
            Field watchType=Watch.class.getDeclaredField("type");
            ManyToOne annotationManytoOne=watchType.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The type property is not properly annotated");
        }catch(NoSuchFieldException ex){
            fail("The Watch class should have an attribute called type that is not present: "+ex.getMessage());
        }

        try{
            Field petField=Watch.class.getDeclaredField("vet");
            ManyToOne annotationManytoOne=petField.getAnnotation(ManyToOne.class);
            assertNotNull(annotationManytoOne,"The vet property is not properly annotated");
        }catch(NoSuchFieldException ex){
            fail("The Watch class should have an attribute called vet that is not present: "+ex.getMessage());
        }
    }    
 

    private void testConstraintsWatch() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Watch f2=new Watch();
        f2.setDate(LocalDate.of(2021, 12, 31));
        f2.setBeginTime(LocalTime.of(9, 00));
        f2.setFinishTime(LocalTime.of(15, 00));

        assertFalse(validator.validate(f2).isEmpty(), "You are not constraining the vet and/or the watchType to be mandatory");
    }

}
