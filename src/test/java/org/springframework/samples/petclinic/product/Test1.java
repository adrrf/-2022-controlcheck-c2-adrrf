package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.watch.Watch;
import org.springframework.samples.petclinic.watch.WatchRepository;
import org.springframework.samples.petclinic.pet.PetRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test1 {

    @Autowired(required = false)
    WatchRepository repo;

    @Autowired
    PetRepository pr;
    

    @Test
    public void test1(){
        repositoryExists();
        repositoryContainsMethod();
        testConstraints();
        testAnnotations();
    }

    
    public void repositoryExists(){
        assertNotNull(repo,"The repository was not injected into the tests, its autowired value was null");
    }

    
    public void repositoryContainsMethod(){
        if(repo!=null){
            Optional<Watch> v=repo.findById(12);
            assertFalse(v.isPresent(),"No result (null) should be returned for a Watch that does not exist");
        }else
            fail("The repository was not injected into the tests, its autowired value was null");
    }

    void testConstraints(){
        Watch f=new Watch();
        f.setId(7);        
        f.setBeginTime(LocalTime.of(8, 0));
        f.setFinishTime(LocalTime.of(16, 0));        
        assertThrows(ConstraintViolationException.class,() -> repo.save(f),
            "You are not constraining the date property, it should be mandatory");

        Watch f3=new Watch();
        f3.setId(7);
        f3.setDate(LocalDate.of(2022,11,1));    
        f3.setBeginTime(LocalTime.of(16, 0));
        assertThrows(Exception.class,() -> repo.save(f3),
        "You are not constraining the endtTime to be mandatory");

        Watch f4=new Watch();
        f3.setId(7);
        f3.setDate(LocalDate.of(2022,11,1));
        f3.setFinishTime(LocalTime.of(8, 0));        
        assertThrows(Exception.class,() -> repo.save(f4),
        "You are not constraining the beginTime to be mandatory");
    }

    void testAnnotations(){
        try{
            Field date=Watch.class.getDeclaredField("date");
            DateTimeFormat dateformat=date.getAnnotation(DateTimeFormat.class);
            assertNotNull(dateformat, "The date property is not annotated with a DateTimeFormat");
            assertEquals(dateformat.pattern(),"dd/MM/yyyy");            
            
            Field start=Watch.class.getDeclaredField("beginTime");
            DateTimeFormat startformat=start.getAnnotation(DateTimeFormat.class);
            assertNotNull(startformat, "The beginTime property is not annotated with a DateTimeFormat");
            assertEquals(startformat.pattern(),"HH:mm:ss");            
            Column annotationColumn=start.getAnnotation(Column.class);
            assertNotNull(annotationColumn,"The beginTime property is not properly annotated");
            assertEquals("start",annotationColumn.name(),"The name of the attribute startTime is not properly customized in the database");

            Field end=Watch.class.getDeclaredField("finishTime");
            DateTimeFormat endformat=end.getAnnotation(DateTimeFormat.class);
            assertNotNull(endformat, "The finishTime property is not annotated with a DateTimeFormat");
            assertEquals(endformat.pattern(),"HH:mm:ss");            
            Column endAnnotationColumn=end.getAnnotation(Column.class);
            assertNotNull(endAnnotationColumn,"The finisTime property is not properly annotated");
            assertEquals("end",endAnnotationColumn.name(),"The name of the attribute finishTime is not properly customized in the database");


            
        }catch(NoSuchFieldException ex){
            fail("The Watch class should have a field that is not present: "+ex.getMessage());
        }
    }
}
