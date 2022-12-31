package org.springframework.samples.petclinic.watch;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.vet.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WatchController {
    
    private WatchService watchService;
    
    @Autowired
    public WatchController(WatchService watchService) {
    	this.watchService = watchService;
    }
    
    @GetMapping("/watch/create")
    public ModelAndView createGet(ModelMap map) {
    	ModelAndView mav = new ModelAndView();
    	mav.addObject("watch", new Watch());
    	mav.setStatus(HttpStatus.OK);
    	mav.setViewName("watch/createOrUpdateWatchForm");
    	return mav;
    }
    
    @PostMapping("/watch/create")
    public ModelAndView createPost(@Valid Watch watch, BindingResult br) throws UnfeasibleWatchException {
    	ModelAndView mav = new ModelAndView();
    	if (br.hasErrors()) {
    		mav.setViewName("watch/createOrUpdateWatchForm");
    	}
    	else {
    		this.watchService.save(watch);
    		mav.setViewName("redirect:/welcome");
    	}
		return mav;
    }
    
}
