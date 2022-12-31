package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetFormatter;
import org.springframework.samples.petclinic.vet.VetService;
import org.springframework.samples.petclinic.watch.ExceptionHandlingControllerAdvice;
import org.springframework.samples.petclinic.watch.LocalTimeFormatter;
import org.springframework.samples.petclinic.watch.UnfeasibleWatchException;
import org.springframework.samples.petclinic.watch.Watch;
import org.springframework.samples.petclinic.watch.WatchController;
import org.springframework.samples.petclinic.watch.WatchService;
import org.springframework.samples.petclinic.watch.WatchType;
import org.springframework.samples.petclinic.watch.WatchTypeFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = WatchController.class,
		includeFilters = {@ComponentScan.Filter(value = WatchTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
                          @ComponentScan.Filter(value = LocalTimeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
                          @ComponentScan.Filter(value = VetFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
                          @ComponentScan.Filter(value = ExceptionHandlingControllerAdvice.class, type = FilterType.ASSIGNABLE_TYPE)},
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class Test9 {
    @MockBean
    WatchService watchService;
    @MockBean
	VetService vetService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    WatchTypeFormatter feedingTypeFormatter;

    private static final String WATCH_TYPE_NAME = "High Protein Puppy Food";
    private static final String INVALID_WATCH = "watch/InvalidWatch";
    private static final Boolean OVERTIME = true;
    private static final Integer A_VET_ID = 1;
    private static final Integer ANOTHER_VET_ID = 2;

    @BeforeEach
    public void configureMock() throws UnfeasibleWatchException, ParseException{
        WatchType watchType=new WatchType();
        watchType.setName(WATCH_TYPE_NAME);
        watchType.setOvertime(OVERTIME);
        Vet dogVet = new Vet();
        dogVet.setId(A_VET_ID);
        Vet catVet = new Vet();
        catVet.setId(ANOTHER_VET_ID);
        List<WatchType> watchTypes=new ArrayList<WatchType>();
        watchTypes.add(watchType);

        Mockito.when(watchService.save(any(Watch.class))).thenReturn(null);
        Mockito.when(watchService.getWatchType(WATCH_TYPE_NAME)).thenReturn(watchType);
        Mockito.when(watchService.getAllWatchTypes()).thenReturn(watchTypes);
        Mockito.when(vetService.getById(ANOTHER_VET_ID)).thenReturn(catVet);
        Mockito.when(vetService.getById(A_VET_ID)).thenReturn(dogVet);
        Mockito.when(feedingTypeFormatter.parse(eq(WATCH_TYPE_NAME), any())).thenReturn(watchType);
    }    

    @WithMockUser(value = "spring", authorities = {"admin"})
    @Test
    void test9()  throws Exception {
        testWatchCreationControllerOK();
        testWatchCreationControllerUnfeasibleWatchException();
        testAnnotations();
    }

	void testWatchCreationControllerOK() throws Exception {
        mockMvc.perform(post("/watch/create")
                            .with(csrf())
                            .param("vet", String.valueOf(A_VET_ID))
                            .param("type", WATCH_TYPE_NAME)
                            .param("date", "01/01/2021")
                            .param("beginTime", "08:00:00")
                            .param("finishTime","15:00:00"))
        .andExpect(redirectedUrl("/welcome"));
    }	
	
	void testWatchCreationControllerUnfeasibleWatchException() throws Exception {
		Mockito.when(watchService.save(any(Watch.class))).thenThrow(new UnfeasibleWatchException());
        mockMvc.perform(post("/watch/create")
                            .with(csrf())
                            .param("vet", String.valueOf(A_VET_ID))
                            .param("type", WATCH_TYPE_NAME)
                            .param("date", "05/01/2022")
                            .param("beginTime", "08:00:00")
                            .param("finishTime","11:00:00"))
                        .andExpect(status().isOk())				
                        .andExpect(view().name(INVALID_WATCH));
    }
	
	
    void testWatchCreationControllerUnfeasibleWatchType() throws Exception {                
        Mockito.when(watchService.save(any(Watch.class))).thenThrow(new UnfeasibleWatchException());        
        mockMvc.perform(post("/watch/create")
                            .with(csrf())                        
                            .param("vet", String.valueOf(ANOTHER_VET_ID))
                            .param("type", WATCH_TYPE_NAME)    
                            .param("date", "2021/12/31")
                            .param("startTime", "08:00:00")
                            .param("endTime", "15:00:00"))                
                .andExpect(status().isOk())				
				.andExpect(view().name(INVALID_WATCH));
    }

    public void testAnnotations() {
        Method handleInvalidWatchException=null;
        try {
            handleInvalidWatchException = ExceptionHandlingControllerAdvice.class.getDeclaredMethod("handleInvalidWatchException",HttpServletRequest.class, Exception.class);
        } catch (NoSuchMethodException e) {
           fail("ExceptionHandlingControllerAdvice does not have a handleInvalidCareException method");
        } catch (SecurityException e) {
            fail("ExceptionHandlingControllerAdvice method is not accessible in FeedingService");
        }
        ExceptionHandler exceptionHandlerAnnotation=handleInvalidWatchException.getAnnotation(ExceptionHandler.class);
        assertNotNull(exceptionHandlerAnnotation,"The method handleInvalidWatchException is not annotated as ExceptionHandler");
        List<Class<? extends Throwable>> exceptionsWithRollbackFor=Arrays.asList(exceptionHandlerAnnotation.value());
        assertTrue(exceptionsWithRollbackFor.contains(UnfeasibleWatchException.class),"UnfeasibleWatchExceptions are not handled!");
        
    }
}


