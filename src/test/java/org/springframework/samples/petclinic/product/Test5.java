package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.watch.WatchService;
import org.springframework.samples.petclinic.watch.Watch;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test5 {
    @Autowired
    WatchService ws;
    

    @Test
    public void test5(){
        validatefindOverlappedWatches();
        validatefindOverlappedWatchesNotFound();
    }
    //List<Watch> findOverlappedWatches(LocaDate date, LocalTime time, int vetId);

    public void validatefindOverlappedWatches(){
    	int vetId = 1;
    	LocalDate date = LocalDate.of(2022,1,5);    
        LocalTime time = LocalTime.of(8, 0);
        List<Watch> watches = ws.getOverlappedWatches(date, time, vetId);
        assertNotNull(watches, "getOverlappedWatches is returning null");
        assertFalse(watches.isEmpty(), "The set of opverlapped watches for vetId '1', date '05-01-2022', and time '08:00:00' is empty, but it should have 1 element");
    }
    
    public void validatefindOverlappedWatchesNotFound(){
    	int vetId = 1;
    	LocalDate date = LocalDate.of(2022,1,4);    
        LocalTime time = LocalTime.of(16, 0);
        List<Watch> watches = ws.getOverlappedWatches(date, time, vetId);
        assertNotNull(watches, "getOverlappedWatches is returning null");
        assertTrue(watches.isEmpty(), "The set of opverlapped watches for vetId '1', date '04-01-2022', and time '16:00:00' should be empty");
    }

}
