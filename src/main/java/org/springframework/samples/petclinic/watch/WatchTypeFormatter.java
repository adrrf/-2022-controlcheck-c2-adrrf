package org.springframework.samples.petclinic.watch;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class WatchTypeFormatter implements Formatter<WatchType> {
	
	private WatchService watchService;
	
	@Autowired
	public WatchTypeFormatter(WatchService watchService) {
		this.watchService = watchService;
	}
	
    @Override
    public String print(WatchType object, Locale locale) {        
        return object.getName();
    }

    @Override
    public WatchType parse(String text, Locale locale) throws ParseException {  
    	WatchType wt = this.watchService.getWatchType(text);
    	if (wt == null) {
    		throw new ParseException("notfound", 0);
    	}
        return wt;
    }
    
}
