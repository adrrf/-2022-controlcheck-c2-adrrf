package org.springframework.samples.petclinic.watch;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice {
	@ExceptionHandler({UnfeasibleWatchException.class})
    public String handleInvalidWatchException(HttpServletRequest request, Exception ex){
        return "watch/InvalidWatch";
    }
}
