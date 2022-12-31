package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.watch.Watch;
import org.springframework.samples.petclinic.watch.WatchService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test10 {
    @Autowired
	WatchService feedingService;
    

    @Test
	public void testPaginationWorks() {
		Pageable p=PageRequest.of(0, 1);
		Page<Watch> page=feedingService.getPaginatedWatches(p);
		assertNotNull(page);
		assertEquals(1,page.getSize(),"The size of the page should be 1 (as requested)");
		assertEquals(2, page.getTotalPages(),"There should be two pages of one element");
		p=PageRequest.of(0, 2);
		page=feedingService.getPaginatedWatches(p);
		assertNotNull(page);
		assertEquals(2,page.getSize(),"The size of the page should be 2 (as requested)");
		assertEquals(1, page.getTotalPages(),"There should be one page of two elements");
	}
}