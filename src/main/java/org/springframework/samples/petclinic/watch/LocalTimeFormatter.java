package org.springframework.samples.petclinic.watch;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class LocalTimeFormatter implements Formatter<LocalTime>{

    @Override
    public String print(LocalTime object, Locale locale) {
        
        return String.valueOf(object.getHour())+":"+String.valueOf(object.getMinute())+":"+String.valueOf(object.getSecond());
    }

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return LocalTime.parse(text,DateTimeFormatter.ofPattern("HH:mm:ss", Locale.getDefault()));
    }
    
}
